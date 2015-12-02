/*
 * @project: Openfire 
 * @package: im.with
 * @title:   MessageStoreServiceInterceptor.java
 *
 * Copyright (c) 2015 Hyfay Limited, Inc.
 * All rights reserved.
 */
package im.with;

import com.jeeframework.util.httpclient.v4.HttpClientHelper;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jivesoftware.openfire.PresenceManager;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.session.Session;
import org.jivesoftware.openfire.user.UserManager;
import org.jivesoftware.util.XMPPDateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.*;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 用于存储用户的消息，并发送消息确认防止消息丢失
 *
 * @author lance
 * @version 1.0 2015-09-25 16:56
 */
public class MessageStoreServiceInterceptor implements PacketInterceptor {

    private static final Logger log = LoggerFactory.getLogger(MessageStoreServiceInterceptor.class);

    private InterceptorManager interceptorManager;
    private UserManager userManager;
    private PresenceManager presenceManager;
    private HttpClientHelper httpClientHelper;
    private XMPPServer server;

    public MessageStoreServiceInterceptor() {
        interceptorManager = InterceptorManager.getInstance();
        interceptorManager.addInterceptor(this);

        server = XMPPServer.getInstance();
        userManager = server.getUserManager();
        presenceManager = server.getPresenceManager();
        httpClientHelper = new HttpClientHelper();
    }

    public void interceptPacket(Packet packet, Session session, boolean incoming, boolean processed) throws PacketRejectedException {
        try {
            this.doAction(packet, incoming, processed, session);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private void doAction(Packet packet, boolean incoming, boolean processed, Session session) throws IOException {
        Packet copyPacket = packet.createCopy();

        Document doc = null;
        if (packet instanceof Message) {
            Message message = (Message) copyPacket;
            if (message.getType() == Message.Type.chat) {
//				log.info("单人聊天信息：", message.toXML());
                // 服务端只接收第一次请求的消息进行离线处理

                String messageID = message.getID();

                JID recipientJID = message.getTo();
                String toUid = "";
                if (recipientJID != null) {
                    toUid = recipientJID.getNode();
                }
                String body = message.getBody();
                JID fromJID = message.getFrom();
                String fromUid = "";
                if (fromJID != null) {
                    fromUid = fromJID.getNode();
                }
                try {
                    //处理过后
                    if (processed == true && incoming == true) {
                        log.info("ORIGINAL-OFFLINE-原始消息: " + message.toXML() + " 时间:" + new Date());

                        doc = DocumentHelper.parseText(message.toXML());
                        Element rootElt = doc.getRootElement();

                        String received1 = rootElt.attributeValue("received");

                        if (received1 != null && received1.equals("true")) {
                            // 收到回执消息删除离线消息
                            //String username = element.getParent().attributeValue("from").substring(0,element.getParent().attributeValue("from").indexOf("@"));
                            //Redis离线策略
//								System.out.println("------------------------------- received delete  message key : " + element.attributeValue("id") + "  ---------");

                            RedisOfflineMessageStore.getInstance().deleteUserMessage(fromUid, messageID);
                            log.info("RECEIVED-回执接收成功 messageId: " + messageID + " 时间:" + new Date());
                        } else {
                            //不是回执消息，就发送收到消息确认
                            Message receiptMessage = new Message();
                            receiptMessage.setTo(message.getFrom());
                            receiptMessage.setFrom(message.getTo());

                            if (messageID != null && messageID.length() > 0) {
                                receiptMessage.setID(messageID);
                            }

                            Element msgElement = receiptMessage.getElement();
                            msgElement.addAttribute("received", "true");

//                        Element received = receiptMessage.addChildElement("received", "urn:xmpp:receipts");
                            try {
//                                server.getPacketDeliverer().deliver(receiptMessage);
                                session.process(receiptMessage);
                                RedisOfflineMessageStore.getInstance().addMessageValue(toUid, messageID, message);

                                log.info("发送回执成功 messageId: " + messageID + " 时间:" + new Date() + " msg.xml = " + message.toXML());
                            } catch (Exception e) {
                                log.error("发送消息 id= " + messageID + "  出现异常: " + e.getMessage() + " 时间:" + new Date());
                            }
                        }

                    }
                } catch (DocumentException e) {
                    log.error("Message-dom4j 解析异常: " + e.getMessage() + " 时间:" + new Date());
                }
            } else if (message.getType() == Message.Type.groupchat) {
                List<?> els = message.getElement().elements("x");
                if (els != null && !els.isEmpty()) {
//					log.info("群聊天信息：", message.toXML());
                } else {
//					log.info("群系统信息：", message.toXML());
                }

            } else {
//				log.info("其他信息：", message.toXML());
            }
        } else if (packet instanceof IQ) {
            IQ iq = (IQ) copyPacket;
            Jedis jedis = RedisClient.getInstance().jedis;
            if (iq.getType() == IQ.Type.set && iq.getChildElement() != null && "session".equals(iq.getChildElement().getName())) {
                if (processed == true && incoming == true) {
//					log.info("用户登录成功：", iq.toXML());
                    String from = iq.getFrom().toString();
                    String username = from.substring(0, from.indexOf("@"));
                    log.info("IQ-用户登录成功: " + username + " 时间:" + new Date());
                    //获取所有离线消息, 第二个参数：是否清除缓存，这里暂时保留，待收到回执一并清除。
                    Set<String> msgKeyList = RedisOfflineMessageStore.getInstance().getMessageIDList(username);
                    if (msgKeyList.size() != 0) {
                        for (String msgKey : msgKeyList) {
                            log.info("IQ-离线消息数量: " + msgKeyList.size() + " 用户: " + username + " 时间:" + new Date());
                            try {
                                String messageKey = "of_msg_" + msgKey;
                                String uid = "";
                                String body = "";
                                String jid = "";
                                String stamp = "";
                                //从缓存获取对应该key的离线消息
                                if (null != jedis.get(messageKey)) {
                                    doc = DocumentHelper.parseText(jedis.get(messageKey));
//									System.out.println("-------------------------------iq message value : " + jedis.get(msgKey) + "  ---------");
                                    Element rootElt = doc.getRootElement();
                                    for (Iterator<Element> it = rootElt.elementIterator(); it.hasNext(); ) {
                                        Element element = (Element) it.next();
                                        if (element.getName().equals("body")) {
                                            uid = element.getParent().attributeValue("from");
                                            jid = element.getParent().attributeValue("to");
                                            body = element.getText();
                                        }
                                        if (element.getName().equals("delay")) {
                                            stamp = element.attributeValue("stamp");
//											System.out.println("------------------------------- stamp : " + stamp + "  ---------");
                                        }
                                    }
//                                    MessageRouter messageRouter = server.getMessageRouter();
                                    Message message = new Message();
                                    message.setBody(body);
                                    message.setFrom(uid);
                                    message.setTo(jid);
                                    message.setType(Message.Type.chat);
                                    message.setID(msgKey);
                                    Element delay = message.addChildElement("delay", "urn:xmpp:delay");
                                    delay.addAttribute("from", XMPPServer.getInstance().getServerInfo().getXMPPDomain());
                                    delay.addAttribute("stamp", XMPPDateTimeFormat.format(new Date(Long.parseLong(stamp))));
//                                    messageRouter.route(message);
                                    session.process(message);

                                    log.info("IQ-消息拉取成功 " + message.toXML() + " 时间:" + new Date());
                                }
                            } catch (DocumentException e) {
//								log.info("用户登录成功，离线消息发送失败：", username);
                                log.info("IQ-dom4j 解析异常: " + e.getMessage() + ", 导致离线消息发送失败：" + username + " 时间:" + new Date());
                            }
                            //删除messageKey的缓存消息
                            jedis.del(msgKey);
                            log.info("IQ-删除离线消息  msgKey: " + msgKey + " 时间:" + new Date());
                        }
                        //清空该用户的所有离线消息缓存
                        jedis.del(username);
                        log.info("IQ-清空离线消息 username: " + username + " 时间:" + new Date());
                    }

                }
            }
        } else if (packet instanceof Presence) {
            Presence presence = (Presence) copyPacket;
            if (presence.getType() == Presence.Type.unavailable) {
//				log.info("用户退出服务器成功：", presence.toXML());
            }
        }
    }


}

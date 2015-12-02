package im.with;

import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.user.UserManager;
import org.jivesoftware.util.LocaleUtils;
import org.jivesoftware.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * RedisOfflineMessageStore
 *
 * @author lanceyan
 * @Description 使用redis存储消息，保证了消息的读写速度。
 */
public class RedisOfflineMessageStore {

    private static final Logger Log = LoggerFactory.getLogger(RedisOfflineMessageStore.class);

    private static RedisOfflineMessageStore instance = null;

    public static RedisOfflineMessageStore getInstance() {
        if (instance == null)
            instance = new RedisOfflineMessageStore();
        return instance;
    }


    /**
     * 修改离线消息缓存策略，存储Value时使用String数据格式，key = messageID； value = message.
     *
     * @param messageID
     * @param message
     */
    public void addMessageValue(String userID, String messageID, Message message) {
        if (message == null) {
            return;
        }
        if (message.getBody() == null || message.getBody().length() == 0) {
            if (message.getChildElement("event", "http://jabber.org/protocol/pubsub#event") == null) {
                return;
            }
        }
        JID recipient = message.getTo();
        String username = recipient.getNode();
        if (username == null || !UserManager.getInstance().isRegisteredUser(recipient)) {
            return;
        } else if (!XMPPServer.getInstance().getServerInfo().getXMPPDomain().equals(recipient.getDomain())) {
            return;
        }

        Jedis jedis = null;

        Date currentDate = new Date();

        try {
            //添加离线Delay消息时间
            message.addChildElement("delay", "urn:xmpp:delay")
                    .addAttribute("from", XMPPServer.getInstance().getServerInfo().getXMPPDomain())
                    .addAttribute("stamp", StringUtils.dateToMillis(currentDate));

            jedis = RedisClient.getInstance().jedis;
            jedis.set("of_msg_" + messageID, message.toXML()); //保存消息
            jedis.zadd("of_umsg_" + userID, currentDate.getTime(), messageID); //保存用户对应的消息id到sortset中去

        } catch (Exception e) {
            Log.error(LocaleUtils.getLocalizedString("admin.error") + " ", e);
        } finally {
            RedisClient.getInstance().destroy();
        }

    }

    /**
     * 返回该用户名下的所有消息ID
     *
     * @param userID
     * @return messageId
     */
    public Set<String> getMessageIDList(String userID) {
        Set<String> msgIDList = new LinkedHashSet<String>();
        Jedis jedis = null;
        try {
            jedis = RedisClient.getInstance().jedis;
            //按照时间先后顺序获取消息，最早收到的消息最先弹出来
            msgIDList = jedis.zrange("of_umsg_" + userID, 0, -1);
        } catch (Exception e) {
            Log.error("Error retrieving offline messages of userID: " + "of_umsg_" + userID, e);
        } finally {
            RedisClient.getInstance().destroy();
        }
        return msgIDList;
    }


    public void deleteUserMessage(String userID, String messageID) {
        Jedis jedis = null;
        try {
            jedis = RedisClient.getInstance().jedis;
            jedis.zrem("of_umsg_" + userID, messageID);
            jedis.del("of_msg_" + messageID);

        } catch (Exception e) {
            Log.error("Error deleteUserMessage offline message of messageKey: " + "of_umsg_" + userID, e);
        } finally {
            RedisClient.getInstance().destroy();
        }
    }

}

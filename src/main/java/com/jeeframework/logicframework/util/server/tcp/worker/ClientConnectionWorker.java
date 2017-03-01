/*
 * @project: webdemo 
 * @package: com.jeeframework.logicframework.util.server.tcp.worker
 * @title:   ClientConnectionWorker.java 
 *
 * Copyright (c) 2016 Hyfay Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.logicframework.util.server.tcp.worker;

import com.jeeframework.core.exception.BaseException;
import com.jeeframework.logicframework.util.server.tcp.BaseNetController;
import com.jeeframework.logicframework.util.server.tcp.MinaTcpServer;
import com.jeeframework.logicframework.util.server.tcp.protocol.NetData;
import com.jeeframework.logicframework.util.server.tcp.protocol.ProtocolParser;
import com.jeeframework.util.io.BSPkgHead;
import org.apache.mina.core.session.IoSession;
import org.springframework.context.ApplicationContext;

import java.util.Vector;

/**
 * 客户端连接的数据处理工人
 *
 * @author lance
 * @version 1.0 2016-03-09 17:16
 */
public class ClientConnectionWorker extends Thread {
    /*
     * 网络事件队列
	 */
    private Vector<MinaDataEvent> eventQueue = new Vector<MinaDataEvent>();
    private MinaTcpServer minaTcpServer;
    private ApplicationContext applicationContext;

    public ClientConnectionWorker(MinaTcpServer minaTcpServer) {
        this.minaTcpServer = minaTcpServer;
        applicationContext = this.minaTcpServer.getApplicationContext();
    }

    /**
     * 向网络事件队列中增加一个消息
     *
     * @param session
     * @param message
     */
    public void addEvent(IoSession session, NetData message) {
        synchronized (eventQueue) {
            eventQueue.add(new MinaDataEvent(session, message));
            eventQueue.notify();
        }
    }

    public void run() {
        MinaDataEvent event = null;

        while (true) {
            // 尝试去获取一个消息
            synchronized (eventQueue) {
                while (eventQueue.isEmpty()) {
                    try {
                        eventQueue.wait();
                    } catch (Exception e) {
                    }
                }
                // 获取到一个事件，并且从队列中移除
                event = eventQueue.remove(0);
            }

            if (event != null) {
                long cmdId = event.message.getCmdId();
                // 获取一个网络层controller的处理类
                String controllerName = ProtocolParser.getBaseNetControllerByCmdId(cmdId);


                // 直接调用类的对应服务
                try {

                    if (applicationContext != null) {

                        BaseNetController controller = (BaseNetController) applicationContext.getBean(controllerName);
                        if (controller == null) {
                            throw new BaseException("没有 cmd = " + cmdId + " 对应的controller处理类");
                        }

                        BSPkgHead pkgHead = event.message.getPkgHead();
                        Object response = controller.callMethod(ProtocolParser.getRequestMethodByCmdId(cmdId), ProtocolParser.getRequestClazzByCmdId(cmdId).getClass(), event.message);
                        if (response != null) {
                            if (pkgHead != null) {
                                ((NetData) response).setPkgHead(pkgHead);
                            }
                            event.session.write(response).join();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

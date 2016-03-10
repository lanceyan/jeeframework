/*
 * @project: webdemo 
 * @package: com.jeeframework.logicframework.util.server.tcp.worker
 * @title:   ClientConnectionHandler.java 
 *
 * Copyright (c) 2016 Hyfay Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.logicframework.util.server.tcp.worker;

import com.jeeframework.logicframework.util.server.tcp.protocol.NetData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 * 处理客户端发过来的数据请求
 *
 * @author lance
 * @version 1.0 2016-03-09 17:14
 */
public class ClientConnectionHandler extends IoHandlerAdapter {
    /*
	 * 工作子线程
	 */
    private Log logger = LogFactory.getLog(ClientConnectionHandler.class);
    private ClientConnectionWorker[] clientConnectionWorkers;

    public ClientConnectionHandler(int workscount) {
        super();

        // 创建线程组
        clientConnectionWorkers = new ClientConnectionWorker[workscount];
        for (int i = 0; i < clientConnectionWorkers.length; i++) {
            clientConnectionWorkers[i] = new ClientConnectionWorker();
            clientConnectionWorkers[i].setDaemon(true); // 父线程的子线程存在，当父线程退出时，子线程也可以退出
            clientConnectionWorkers[i].start();
        }
    }

    public void sessionOpened(IoSession session) {
        System.out.println("session opened");
    }

    public void messageReceived(IoSession session, Object message) {
        // 将请求分派到不同的工作线程中去.
        // 是新的mina协议则通过NetMessage
        clientConnectionWorkers[message.hashCode() % clientConnectionWorkers.length].addEvent(
                session, (NetData) message);

    }

    public void sessionIdle(IoSession session, IdleStatus status) {
    }

    public void exceptionCaught(IoSession session, Throwable cause) {
        // 不处理客户连接终断的异常
        if (!session.isConnected())
            return;

        // 输出异常
        logger.error("ClientConnectionHandler.exceptionCaught",cause);

    }

    public void messageSent(IoSession session, Object message) throws Exception {
        super.messageSent(session, message);
    }

    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
    }

    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);
    }
}

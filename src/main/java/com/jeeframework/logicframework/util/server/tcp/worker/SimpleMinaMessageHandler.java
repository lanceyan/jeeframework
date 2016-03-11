/*
 * @project: webdemo 
 * @package: com.jeeframework.logicframework.util.server.tcp.worker
 * @title:   BexnHandler.java 
 *
 * Copyright (c) 2016 Hyfay Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.logicframework.util.server.tcp.worker;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 * 描述
 *
 * @author lance
 * @version 1.0 2016-03-10 12:05
 */
public class SimpleMinaMessageHandler extends IoHandlerAdapter {
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        System.out.println("messageReceived " + message);
        super.messageReceived(session, message);
        session.write("messageReceived "+message);
        session.closeOnFlush();
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
        if (session.isConnected()) {
            session.close();
        }
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        session.close();
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
        System.out.println("sessionClosed");
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 30000);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status)
            throws Exception {
        session.close();
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
    }
}
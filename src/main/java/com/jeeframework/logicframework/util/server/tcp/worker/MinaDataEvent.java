/*
 * @project: webdemo 
 * @package: com.jeeframework.logicframework.util.server.tcp.worker
 * @title:   MinaDataEvent.java 
 *
 * Copyright (c) 2016 Hyfay Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.logicframework.util.server.tcp.worker;

import com.jeeframework.logicframework.util.server.tcp.protocol.NetData;
import org.apache.mina.core.session.IoSession;

/**
 * 网络数据的事件对象，包括数据和session
 *
 * @author lance
 * @version 1.0 2016-03-09 17:25
 */
public class MinaDataEvent {

    public NetData message;
    public IoSession session;

    public MinaDataEvent()
    {
    }
    public MinaDataEvent(IoSession session, NetData message)
    {
        this.message = message;
        this.session = session;
    }
}

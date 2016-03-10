/*
 * @project: webdemo 
 * @package: com.jeeframework.logicframework.util.server.tcp.codec
 * @title:   MinaCodecFactory.java 
 *
 * Copyright (c) 2016 Hyfay Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.logicframework.util.server.tcp.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * mina服务器的解码协议
 *
 * @author lance
 * @version 1.0 2016-03-09 16:09
 */
public class MinaServerCodecFactory implements ProtocolCodecFactory {

    private final MinaServerEncoder encoder;
    private final MinaServerDecoder decoder;

    public MinaServerCodecFactory() {
        encoder = new MinaServerEncoder();
        decoder = new MinaServerDecoder();
    }

    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return encoder;
    }

    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return decoder;
    }

}

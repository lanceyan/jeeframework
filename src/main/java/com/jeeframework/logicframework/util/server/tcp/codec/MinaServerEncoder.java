/*
 * @project: webdemo 
 * @package: com.jeeframework.logicframework.util.server.tcp.codec
 * @title:   MinaEncoder.java 
 *
 * Copyright (c) 2016 Hyfay Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.logicframework.util.server.tcp.codec;

import com.jeeframework.logicframework.util.server.tcp.protocol.MinaProtocolHeader;
import com.jeeframework.logicframework.util.server.tcp.protocol.NetData;
import com.jeeframework.util.io.ByteStream;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * socket mina编码器
 *
 * @author lance
 * @version 1.0 2016-03-09 16:10
 */
public class MinaServerEncoder extends ProtocolEncoderAdapter {
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out)
            throws Exception {
        // Ignore. Do nothing. Content being sent is already a bytebuffer (of strings)

        NetData msg = (NetData) message;
        try
        {
            byte[] msgdata = new byte[msg.getSize() + MinaProtocolHeader.HEADER_LEN];
            ByteStream bs = new ByteStream();
            bs.resetBuffer(msgdata, msgdata.length);
            MinaProtocolHeader header = new MinaProtocolHeader();
            header.setCmdId(msg.getCmdId());
            header.setTotalLen(msgdata.length);
            header.setRetcode(msg.getResult());

            header.serialize(bs);

            msg.serialize(bs);
            IoBuffer buf = IoBuffer.allocate(msgdata.length);
            buf.put(msgdata);
            buf.flip();

            out.write(buf);
            out.flush();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

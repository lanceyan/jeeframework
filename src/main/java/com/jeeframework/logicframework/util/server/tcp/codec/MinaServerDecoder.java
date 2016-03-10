/*
 * @project: webdemo 
 * @package: com.jeeframework.logicframework.util.server.tcp.codec
 * @title:   MinaDecoder.java 
 *
 * Copyright (c) 2016 Hyfay Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.logicframework.util.server.tcp.codec;

import com.jeeframework.logicframework.util.server.tcp.protocol.NetData;
import com.jeeframework.logicframework.util.server.tcp.protocol.MinaProtocolHeader;
import com.jeeframework.logicframework.util.server.tcp.protocol.ProtocolParser;
import com.jeeframework.util.io.ByteStream;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * socket mina解码器
 *
 * @author lance
 * @version 1.0 2016-03-09 16:10
 */
public class MinaServerDecoder extends CumulativeProtocolDecoder {

    private MinaProtocolHeader header	= new MinaProtocolHeader();
    private byte[]							headBuf	= new byte[MinaProtocolHeader.HEADER_LEN];
    private byte[]							bodyBuf	= new byte[2097152];
    private int									recvlen	= 0;

    @Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
            throws Exception {
        ByteStream bs = new ByteStream();
        int blen = 0;

        if (header.getTotalLen() == 0)
        {
            recvlen = 0;
            in.get(headBuf);
            bs.resetBuffer(headBuf, MinaProtocolHeader.HEADER_LEN);
            header.unSerialize(bs);
            blen = MinaProtocolHeader.HEADER_LEN;
        }

        if (recvlen < header.getBodyLen())
        {
            blen = in.limit() - blen;
            in.get(bodyBuf, recvlen, blen);
            recvlen += blen;

            if (recvlen < header.getBodyLen())
                return false;
        }
        bs.resetBuffer(bodyBuf, recvlen);
        NetData req = ProtocolParser.getRequestClazzByCmdId(header.getCmdId());
        if (req != null)
        {
            try
            {
                req.unSerialize(bs);
                out.write(req);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return false;
    }
}

/*
 * @project: webdemo 
 * @package: com.jeeframework.logicframework.util.server.tcp.codec
 * @title:   MinaProtocolHeader.java 
 *
 * Copyright (c) 2016 Hyfay Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.logicframework.util.server.tcp.protocol;

import com.jeeframework.util.io.ByteStream;

/**
 * socket协议的头部处理文件
 *
 * @author lance
 * @version 1.0 2016-03-09 16:15
 */
public class MinaProtocolHeader {
    public static final int HEADER_LEN = 12;
    private long totalLen;
    private long cmdId;
    private long retcode;

    public int unSerialize(ByteStream bs) throws Exception {
        this.totalLen = bs.popUInt();
        this.cmdId = bs.popUInt();
        this.retcode = bs.popUInt();
        return bs.getReadLength();
    }

    public long getCmdId() {
        return cmdId;
    }

    public void setCmdId(long cmdId) {
        this.cmdId = cmdId;
    }

    public long getRetcode() {
        return retcode;
    }

    public void setRetcode(long retcode) {
        this.retcode = retcode;
    }

    public long getTotalLen() {
        return totalLen;
    }

    public long getBodyLen() {
        return totalLen - MinaProtocolHeader.HEADER_LEN;
    }

    public void setTotalLen(long totalLen) {
        this.totalLen = totalLen;
    }

    public int serialize(ByteStream bs) throws Exception {
        bs.pushUInt(totalLen);
        bs.pushUInt(cmdId);
        bs.pushUInt(retcode);
        return bs.getWrittenLength();
    }

}

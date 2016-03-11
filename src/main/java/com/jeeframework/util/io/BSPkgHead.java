/*
 * @project: webdemo 
 * @package: com.jeeframework.util.io
 * @title:   BSPkgHead.java 
 *
 * Copyright (c) 2016 Hyfay Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.util.io;

/**
 * 网络协议的包头
 *
 * @author lance
 * @version 1.0 2016-03-09 17:27
 */
public class BSPkgHead {
    private long dwLength = 0;
    private long dwSerialNo = 0;
    private int wVersion = 2;
    private int wCommand = 0;
    private long dwUin = 0;

    private long dwFlag = 0;
    private long dwResult = 0;
    private long dwClientIP = 0;
    private int wClientPort = 0;
    private long dwAccessServerIP = 0;
    private int wAccessServerPort = 0;
    private long dwAppInterfaceIP = 0;
    private int wAppInterfacePort = 0;
    private long dwAppServerIP = 0;
    private int wAppServerPort = 0;

    private long dwOperatorId = 0;
    private byte[] sPassport;

    private int wSeconds = 0;
    private long dwUSeconds = 0;

    private int wCookie2Length = 35;
    private byte cLegacy = 0;
    private int iSockfd = 0;
    private long dwSockChannel = 0;
    private int wPeerPort = 0;
    private long dwCommand = 0;
    private int wSvrLevelIndex = 0;
    private byte[] bCookie2;

    private final static int PASSPORT_LEN = 10;
    private final static int COOKIE2_LEN = 18;

    public final static int iPkgHeadLength = 105;

    public BSPkgHead() {
        sPassport = new byte[PASSPORT_LEN];
        bCookie2 = new byte[COOKIE2_LEN];
    }

    public long getDwSerialNo() {
        return dwSerialNo;
    }

    public void setDwSerialNo(long dwSerialNo) {
        this.dwSerialNo = dwSerialNo;
    }

    public long getDwResult() {
        return dwResult;
    }

    public void setDwResult(long dwResult) {
        this.dwResult = dwResult;
    }

    public int Serialize(ByteStream bs) throws Exception {
        bs.pushUInt(this.dwLength);
        bs.pushUInt(this.dwSerialNo);
        bs.pushUShort(this.wVersion);
        bs.pushUShort(this.wCommand);
        bs.pushUInt(this.dwUin);
        bs.pushUInt(this.dwFlag);
        bs.pushUInt(this.dwResult);
        bs.pushUInt(this.dwClientIP);
        bs.pushUShort(this.wClientPort);
        bs.pushUInt(this.dwAccessServerIP);
        bs.pushUShort(this.wAccessServerPort);
        bs.pushUInt(this.dwAppInterfaceIP);
        bs.pushUShort(this.wAppInterfacePort);
        bs.pushUInt(this.dwAppServerIP);
        bs.pushUShort(this.wAppServerPort);
        bs.pushUInt(this.dwOperatorId);
        bs.pushBytes(this.sPassport, PASSPORT_LEN);
        bs.pushUShort(this.wSeconds);
        bs.pushUInt(this.dwUSeconds);
        bs.pushUShort(this.wCookie2Length);
        bs.pushByte(this.cLegacy);
        bs.pushInt(this.iSockfd);
        bs.pushUInt(this.dwSockChannel);
        bs.pushUShort(this.wPeerPort);
        bs.pushUInt(this.dwCommand);
        bs.pushUShort(this.wSvrLevelIndex);
        bs.pushBytes(this.bCookie2, COOKIE2_LEN);

        return bs.getWrittenLength();
    }

    public int UnSerialize(ByteStream bs) throws Exception {
        this.dwLength = bs.popUInt();
        this.dwSerialNo = bs.popUInt();
        this.wVersion = bs.popUShort();
        this.wCommand = bs.popUShort();
        this.dwUin = bs.popUInt();
        this.dwFlag = bs.popUInt();
        this.dwResult = bs.popUInt();
        this.dwClientIP = bs.popUInt();
        this.wClientPort = bs.popUShort();
        this.dwAccessServerIP = bs.popUInt();
        this.wAccessServerPort = bs.popUShort();
        this.dwAppInterfaceIP = bs.popUInt();
        this.wAppInterfacePort = bs.popUShort();
        this.dwAppServerIP = bs.popUInt();
        this.wAppServerPort = bs.popUShort();
        this.dwOperatorId = bs.popUInt();
        this.sPassport = bs.popBytes(PASSPORT_LEN);
        this.wSeconds = bs.popUShort();
        this.dwUSeconds = bs.popUInt();
        this.wCookie2Length = bs.popUShort();
        this.cLegacy = bs.popByte();
        this.iSockfd = bs.popInt();
        this.dwSockChannel = bs.popUInt();
        this.wPeerPort = bs.popUShort();
        this.dwCommand = bs.popUInt();
        this.wSvrLevelIndex = bs.popUShort();
        this.bCookie2 = bs.popBytes(COOKIE2_LEN);

        return bs.getReadLength();
    }

    public long getDwClientIP() {
        return dwClientIP;
    }

    public void setDwClientIP(long dwClientIP) {
        this.dwClientIP = dwClientIP;
    }

    public long getDwCommand() {
        return dwCommand;
    }

    public void setDwCommand(long dwCommand) {
        this.dwCommand = dwCommand;
        this.wCommand = (int)(dwCommand >> 16);
    }

    public long getDwOperatorId() {
        return dwOperatorId;
    }

    public void setDwOperatorId(long dwOperatorId) {
        this.dwOperatorId = dwOperatorId;
    }

    public byte[] getSPassport() {
        return sPassport;
    }

    public void setSPassport(byte[] passport) {
        sPassport = passport;
    }

    public int getWClientPort() {
        return wClientPort;
    }

    public void setWClientPort(int clientPort) {
        wClientPort = clientPort;
    }

    public long getDwLength() {
        return dwLength;
    }

    public void setDwLength(long dwLength) {
        this.dwLength = dwLength;
    }

    public void dump() {
        System.out.println("dwLength = " + this.dwLength);
        System.out.println("dwSerialNo = " + this.dwSerialNo);
        System.out.println("wVersion = " + this.wVersion);
        System.out.println("wCommand = 0x" + Integer.toString(this.wCommand, 16));
        System.out.println("dwUin = " + this.dwUin);
        System.out.println("dwResult = 0x" + Long.toString(this.dwResult, 16));
        System.out.println("dwClientIP = 0x" + Long.toString(this.dwClientIP, 16));
        System.out.println("wClientPort = 0x" + Integer.toString(this.wClientPort, 16));
        System.out.println("dwOperatorId = " + this.dwOperatorId);
        System.out.println("sPassport = " + new String(this.sPassport));
        System.out.println("dwCommand = 0x" + Long.toString(this.dwCommand, 16));
    }

    public long getDwUin() {
        return dwUin;
    }

    public void setDwUin(long dwUin) {
        this.dwUin = dwUin;
    }

    public int getWVersion() {
        return wVersion;
    }

    public void setWVersion(int version) {
        wVersion = version;
    }

    public long getCmdId() {
        // NO-OP
        return 0;
    }

}

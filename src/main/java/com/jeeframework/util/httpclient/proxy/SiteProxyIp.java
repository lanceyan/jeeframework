/*
 * @project: hyfaycrawler 
 * @package: com.hyfaycrawler.integration.bo
 * @title:   ProxyIpBO.java 
 *
 * Copyright (c) 2014 Hyfay Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.util.httpclient.proxy;

import java.util.Calendar;
import java.util.Date;

/**
 * 数据源对应的代理IP
 *
 * @author lance
 * @version 1.0 2014/7/8 11:09
 */
public class SiteProxyIp  {

    private Long id;
    protected String host;
    protected Integer port;
    protected Integer dataSourceId;
    protected Integer valid;
    protected String errorMsg;

    public Integer getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(Integer dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Date createTime;
    private Date lastModifyTime;

    private boolean isLocal = false;

    private long lastReqTime;// 上一次发出请求时间

    private int reqCountPerDay;//每天发出请求的次数
    private int reqCountPerHour;//每小时发出请求的次数

    /**
     * 清除次数的请求记录
     */
    public void reset() {
        if (lastReqTime > 0L) {
            Calendar c = Calendar.getInstance();
            int d1 = c.get(Calendar.DAY_OF_MONTH);// 当前天

            c.setTimeInMillis(lastReqTime);
            int d2 = c.get(Calendar.DAY_OF_MONTH);// 上一次请求的天值

            boolean isSameDay = d2 != d1 ? false : true;
            if (!isSameDay) {
                reqCountPerDay = 0;
            }
        }
        reqCountPerHour = 0;
    }

    public synchronized void incReqNum() {
        reqCountPerDay++;
        reqCountPerHour++;

    }

    public long getLastReqTime() {
        return lastReqTime;
    }

    public void setLastReqTime(long lastReqTime) {
        this.lastReqTime = lastReqTime;
    }

    public SiteProxyIp() {

    }

    public int getReqCountPerHour() {
        return reqCountPerHour;
    }

    public int getReqCountPerDay() {
        return reqCountPerDay;
    }

    /**
     * 根据代理IP策略判断是否当前代理可以用
     * @param proxyIpStrategy
     * @return
     */
    public boolean isLimited(ProxyIpStrategy proxyIpStrategy) {

        int interval = proxyIpStrategy.getInterval();
        int countPerHour = proxyIpStrategy.getCountPerHour();

        //检验当前连接中的IP是否受限制
        long ipLastReqTime = getLastReqTime();
        boolean ipRequestIntervalValid = false;

        long ipRequestInterval = interval;
        long now = System.currentTimeMillis();
        ipRequestIntervalValid = (now - ipLastReqTime) > ipRequestInterval * 1000;

        if (!ipRequestIntervalValid) {
            // 上一次请求时间离现在还在设定时间间隔内，不能请求
            return true;
        }

        boolean ipRequestTimesValid = true;

        //ip小时请求方式，0：连续1小时，1：每小时
        int ipRequestCountPerHour = countPerHour;
        if (ipRequestCountPerHour > 0) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);

            if (reqCountPerHour >= ipRequestCountPerHour) {
                ipRequestTimesValid = false;
            }
        }
        if (!ipRequestTimesValid) {
            // 达到1小时内请求次数，不能再请求
            return true;
        }
        return false;
    }

    public SiteProxyIp(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public boolean isLocal() {
        if (isLocal || this.host.equals("127.0.0.1")) {
            return true;

        }
        return false;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getValid() {
        return valid;
    }

    public void setValid(Integer valid) {
        this.valid = valid;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public boolean equals(SiteProxyIp that) {
        if (this.host.equals(that.host) && this.port == that.port)
            return true;
        else
            return false;
    }

}
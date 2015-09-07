/*
 * @project: hyfaycrawler 
 * @package: com.hyfaycrawler.integration.bo
 * @title:   ProxyIpBO.java 
 *
 * Copyright (c) 2014 Hyfay Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.util.httpclient.proxy;

import com.jeeframework.logicframework.integration.bo.AbstractBO;

import java.util.Date;

/**
 * 数据源对应的代理IP使用策略
 *
 * @author lance
 * @version 1.0 2014/7/8 11:09
 */
public class ProxyIpStrategy {

    private Long id;
    protected Integer dataSourceId;
    protected Integer loadCountOnce; //增量获取代理IP的个数
    protected Integer countPerHour;//每小时IP访问数据源的最大次数，0是无限制
    protected Integer countPerDay;//每小时IP访问数据源的最大次数，0是无限制
    protected Integer interval;    //毫秒  访问间隔，0是没有间隔
    protected Integer errorsPerHour; // 一个小时网络错误超过这个值应该告警

    public Integer getCountPerDay() {
        return countPerDay;
    }

    public void setCountPerDay(Integer countPerDay) {
        this.countPerDay = countPerDay;
    }

    public Integer getLoadCountOnce() {
        return loadCountOnce;
    }

    public void setLoadCountOnce(Integer loadCountOnce) {
        this.loadCountOnce = loadCountOnce;
    }

    public Integer getCountPerHour() {
        return countPerHour;
    }

    public void setCountPerHour(Integer countPerHour) {
        this.countPerHour = countPerHour;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public Integer getErrorsPerHour() {
        return errorsPerHour;
    }

    public void setErrorsPerHour(Integer errorsPerHour) {
        this.errorsPerHour = errorsPerHour;
    }

    public Integer getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(Integer dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Date createTime;
    private Date lastModifyTime;

    public ProxyIpStrategy() {

    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }


}
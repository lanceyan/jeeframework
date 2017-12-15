/*
 * @project: com.jeeframework 
 * @package: com.jeeframework.logicframework.integration.dao.hbase
 * @title:   HbaseAccessor.java 
 *
 * Copyright (c) 2017 jeeframework Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.logicframework.integration.dao.hbase;

import org.springframework.beans.factory.InitializingBean;

/**
 * hbase访问类
 *
 * @author lance
 * @version 1.0 2017-11-27 12:05
 */
public abstract class HbaseAccessor implements InitializingBean {

    private HbaseDataSource dataSource;

    public HbaseDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(HbaseDataSource dataSource) {
        this.dataSource = dataSource;
    }


    public void afterPropertiesSet() throws Exception {
        if (getDataSource() == null) {
            throw new IllegalArgumentException("Property 'dataSource' is required");
        }
    }
}

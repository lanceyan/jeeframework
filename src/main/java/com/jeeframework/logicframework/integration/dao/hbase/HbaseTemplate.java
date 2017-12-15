/*
 * @project: com.jeeframework 
 * @package: com.jeeframework.logicframework.integration.dao.hbase
 * @title:   HbaseTemplate.java 
 *
 * Copyright (c) 2017 jeeframework Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.logicframework.integration.dao.hbase;


import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * hbase 操作访问 模板类
 *
 * @author lance
 * @version 1.0 2017-12-04 13:48
 */
public class HbaseTemplate extends HbaseAccessor implements HbaseOperations {


    @Override
    public <T> boolean createTable(Class<T> clazz) throws DataAccessException {
        return false;
    }

    @Override
    public <T> boolean deleteTable(Class<T> clazz) throws DataAccessException {
        return false;
    }

    @Override
    public <T> boolean hasTable(Class<T> clazz) throws DataAccessException {
        return false;
    }

    @Override
    public <T> boolean put(T t) throws DataAccessException {
        return false;
    }

    @Override
    public <T> boolean putBatch(Class<T> clazz, List<T> list) throws DataAccessException {
        return false;
    }

    @Override
    public <T> T get(Object rowkey, Class<T> clazz) throws DataAccessException {
        return null;
    }

    @Override
    public <T> T get(Object rowkey, Class<T> clazz, List<String> columns) throws DataAccessException {
        return null;
    }

    @Override
    public <T> List<T> scan(Object startrowkey, Object stoprowkey, Class<T> clazz) throws DataAccessException {
        return null;
    }

    @Override
    public <T> List<T> scan(Object startrowkey, Object stoprowkey, Class<T> clazz, List<String> columns) throws
            DataAccessException {
        return null;
    }

    @Override
    public <T> boolean delete(Object rowkey, Class<T> clazz) throws DataAccessException {
        return false;
    }

    @Override
    public <T> boolean deleteBatch(Class<T> clazz, List<T> list) throws DataAccessException {
        return false;
    }

    @Override
    public <T> boolean hasRow(Object rowkey, Class<T> clazz) throws DataAccessException {
        return false;
    }
}

/*
 * @project: com.jeeframework 
 * @package: com.jeeframework.logicframework.integration.dao.hbase
 * @title:   BaseDaoHBase.java 
 *
 * Copyright (c) 2017 jeeframework Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.logicframework.integration.dao.hbase;

import com.jeeframework.logicframework.beans.ContextManageBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * hbase的数据访问对象基础工具类
 *
 * @author lance
 * @version 1.0 2017-12-04 13:46
 */
public class BaseDaoHbase implements ContextManageBean {

    @Autowired
    protected HbaseTemplate hbaseTemplate;

    protected BeanFactory context;

    protected String beanName = null;


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void setBeanName(String name) {

    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public <T> boolean createTable(Class<T> clazz) throws DataAccessException {
        return hbaseTemplate.createTable(clazz);
    }

    public <T> boolean deleteTable(Class<T> clazz) throws DataAccessException {
        return hbaseTemplate.deleteTable(clazz);
    }

    public <T> boolean hasTable(Class<T> clazz) throws DataAccessException {
        return hbaseTemplate.hasTable(clazz);
    }

    public <T> boolean put(T t) throws DataAccessException {
        return hbaseTemplate.put(t);
    }

    public <T> boolean putBatch(Class<T> clazz, List<T> list) throws DataAccessException {
        return hbaseTemplate.putBatch(clazz, list);
    }

    public <T> T get(Object rowKey, Class<T> clazz) throws DataAccessException {
        return hbaseTemplate.get(rowKey, clazz);
    }

    public <T> T get(Object rowKey, Class<T> clazz, List<String> columns) throws DataAccessException {
        return hbaseTemplate.get(rowKey, clazz, columns);
    }

    public <T> List<T> scan(Object startRowKey, Object stopRowKey, Class<T> clazz) throws DataAccessException {
        return hbaseTemplate.scan(startRowKey, stopRowKey, clazz);
    }

    public <T> List<T> scan(Object startRowKey, Object stopRowKey, Class<T> clazz, List<String> columns) throws
            DataAccessException {
        return hbaseTemplate.scan(startRowKey, stopRowKey, clazz, columns);
    }

    public <T> boolean delete(Object rowKey, Class<T> clazz) throws DataAccessException {
        return hbaseTemplate.delete(rowKey, clazz);
    }

    public <T> boolean deleteBatch(Class<T> clazz, List<T> list) throws DataAccessException {
        return hbaseTemplate.deleteBatch(clazz, list);
    }

    public <T> boolean hasRow(Object rowKey, Class<T> clazz) throws DataAccessException {
        return hbaseTemplate.delete(rowKey, clazz);
    }


}

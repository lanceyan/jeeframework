package com.jeeframework.logicframework.integration.dao.hbase;

import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * hbase 操作接口定义
 *
 * @author lance
 */
public interface HbaseOperations {

    <T> boolean createTable(Class<T> clazz) throws DataAccessException;

    <T> boolean deleteTable(Class<T> clazz) throws DataAccessException;

    <T> boolean hasTable(Class<T> clazz) throws DataAccessException;

    <T> boolean put(T t) throws DataAccessException;

    <T> boolean putBatch(Class<T> clazz, List<T> list) throws DataAccessException;

    <T> T get(Object rowkey, Class<T> clazz) throws DataAccessException;

    <T> T get(Object rowkey, Class<T> clazz, List<String> columns) throws DataAccessException;

    <T> List<T> scan(Object startrowkey, Object stoprowkey, Class<T> clazz) throws DataAccessException;

    <T> List<T> scan(Object startrowkey, Object stoprowkey, Class<T> clazz, List<String> columns) throws
            DataAccessException;

    <T> boolean delete(Object rowkey, Class<T> clazz) throws DataAccessException;

    <T> boolean deleteBatch(Class<T> clazz, List<T> list) throws DataAccessException;

    <T> boolean hasRow(Object rowkey, Class<T> clazz) throws DataAccessException;

}

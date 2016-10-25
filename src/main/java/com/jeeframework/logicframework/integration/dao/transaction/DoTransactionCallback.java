/*
 * @project: com.jeeframework 
 * @package: com.jeeframework.logicframework.integration.dao.transaction
 * @title:   TransactionCallback.java 
 *
 * Copyright (c) 2016 jeeframework Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.logicframework.integration.dao.transaction;

/**
 * 事务回调类
 *
 * @author lance
 * @version 1.0 2016-10-25 16:34
 */
public interface DoTransactionCallback<T> {
    T doInTransaction();
}

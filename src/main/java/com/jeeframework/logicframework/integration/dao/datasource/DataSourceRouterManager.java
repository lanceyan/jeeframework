/**
 * Copyright (C) 2015-2015 JEEFRAMEWORK Inc.All Rights Reserved.
 * 																	
 * FileName��PartitionTableSynchronizationManager.java					
 *			
 * Description����Ҫ�������ļ�������							 												
 * History��
 * �汾��    ����           ����          ��Ҫ������ز���
 *  1.0   lanceyan        2008-8-12           Create	
 */

package com.jeeframework.logicframework.integration.dao.datasource;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

/**
 * ���ڱ������Դ��ѡ����Դ�Ĺ�����
 * 
 * @author lanceyan�������޸��ߣ�
 * @version 1.0���°汾�ţ�
 * 
 */

public class DataSourceRouterManager
{
    private static final Log logger = LogFactory.getLog(DataSourceRouterManager.class);

    // ���Դ��ѡ�����
    private static final Map<String, String> resources = new HashMap<String, String>();

    public static Object getResource(Object key)
    {
        Assert.notNull(key, "Key must not be null");

        Object value = resources.get(key);
        if (value != null && logger.isDebugEnabled())
        {
            logger.debug("Retrieved value [" + value + "] for key [" + key + "] bound to thread ["
                    + Thread.currentThread().getName() + "]");
        }
        return value;
    }

    public static void bindResource(String key, String value) throws IllegalStateException
    {
        Assert.notNull(key, "Key must not be null");
        Assert.notNull(value, "Value must not be null");

        resources.put(key, value);
        if (logger.isDebugEnabled())
        {
            logger.debug("Bound value [" + value + "] for key [" + key + "] to thread ["
                    + Thread.currentThread().getName() + "]");
        }
    }

    public static Object unbindResource(String key) throws IllegalStateException
    {
        Assert.notNull(key, "Key must not be null");

        Object value = resources.remove(key);
        // remove entire ThreadLocal if empty
        if (logger.isDebugEnabled())
        {
            logger.debug("Removed value [" + value + "] for key [" + key + "] from thread ["
                    + Thread.currentThread().getName() + "]");
        }
        return value;
    }

}

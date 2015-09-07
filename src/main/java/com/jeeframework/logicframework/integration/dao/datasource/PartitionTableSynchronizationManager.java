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
 * ���ڱ��ֱַ���Դ�Ĺ�����
 * 
 * @author lanceyan�������޸��ߣ�
 * @version 1.0���°汾�ţ�
 * 
 */

public class PartitionTableSynchronizationManager
{
    private static final Log logger = LogFactory.getLog(PartitionTableSynchronizationManager.class);

    // ���ֱַ���Դ��TreadLocal��
    private static final ThreadLocal<Map<String, String>> resources = new ThreadLocal<Map<String, String>>();

    public static Map<String, String> getResourceMap()
    {
        Map<String, String> map = resources.get();
        return map;
    }

    public static boolean hasResource(Object key)
    {
        Assert.notNull(key, "Key must not be null");
        Map<String, String> map = resources.get();
        return (map != null && map.containsKey(key));
    }

    public static Object getResource(Object key)
    {
        Assert.notNull(key, "Key must not be null");
        Map<String, String> map = resources.get();
        if (map == null)
        {
            return null;
        }
        Object value = map.get(key);
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
        Map<String, String> map = resources.get();
        // set ThreadLocal Map if none found
        if (map == null)
        {
            map = new HashMap<String, String>();
            resources.set(map);
        }

        map.put(key, value);
        if (logger.isDebugEnabled())
        {
            logger.debug("Bound value [" + value + "] for key [" + key + "] to thread ["
                    + Thread.currentThread().getName() + "]");
        }
    }

    public static Object unbindResource(String key) throws IllegalStateException
    {
        Assert.notNull(key, "Key must not be null");
        Map<String, String> map = resources.get();
        if (map == null || !map.containsKey(key))
        {
            throw new IllegalStateException("No value for key [" + key + "] bound to thread ["
                    + Thread.currentThread().getName() + "]");
        }
        Object value = map.remove(key);
        // remove entire ThreadLocal if empty
        if (map.isEmpty())
        {
            resources.set(null);
        }
        if (logger.isDebugEnabled())
        {
            logger.debug("Removed value [" + value + "] for key [" + key + "] from thread ["
                    + Thread.currentThread().getName() + "]");
        }
        return value;
    }

}

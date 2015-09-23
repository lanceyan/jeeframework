/**
 * Copyright (C) 2015-2015 JEEFRAMEWORK Inc.All Rights Reserved.
 * 																	
 * FileName��TypeUtil.java					
 *			
 * Description����Ҫ�������ļ�������							 												
 * History��
 * �汾��    ����           ����          ��Ҫ������ز���
 *  1.0   lanceyan        2008-6-30           Create	
 */

package com.jeeframework.util.validate;

import java.util.List;

/**
 * ����
 * 
 * @author lanceyan�������޸��ߣ�
 * @version 1.0���°汾�ţ�
 * @see �ο���JavaDoc
 */

public class TypeUtil
{
    public static boolean isString(Object obj)
    {
        return obj instanceof String;
    }

    public static boolean isList(Object obj)
    {
        return obj instanceof List;
    }
}

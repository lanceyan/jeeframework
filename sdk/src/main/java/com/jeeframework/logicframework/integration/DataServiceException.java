/**
 * Copyright (C) 1998-2007 JEEFRAMEWORK Inc.All Rights Reserved.
 * 																	
 * FileName��DataServiceException.java					
 *			
 * Description����Ҫ�������ļ�������							 * 												 * History��
 * �汾��    ����      ����       ��Ҫ������ز���
 *  1.0   lanceyan  2008-5-15  Create	
 */

package com.jeeframework.logicframework.integration;

import com.jeeframework.core.exception.BaseException;

/**
 * ��ݷ������쳣�ӿ�
 * 
 * @author lanceyan�������޸��ߣ�
 * @version 1.0���°汾�ţ�
 * 
 */

public class DataServiceException extends BaseException
{

    public DataServiceException(Throwable cause)
    {
        super(cause);
    }

    public DataServiceException(String message)
    {
        super(message);
    }

    public DataServiceException(String message, Throwable cause)
    {
        super(message, cause);
    }
}

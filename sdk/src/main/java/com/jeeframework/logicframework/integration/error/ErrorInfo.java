/**
 * Copyright (C) 2015-2015 JEEFRAMEWORK Inc.All Rights Reserved.
 * 																	
 * FileName��ErrorInfo.java					
 *			
 * Description����Ҫ�������ļ�������							 												
 * History��
 * �汾��    ����           ����          ��Ҫ������ز���
 *  1.0   lanceyan        2008-8-6           Create	
 */

package com.jeeframework.logicframework.integration.error;

import java.util.Formatter;

/**
 * ������Ϣ�Ĵ����࣬��װ������ʹ�����Ϣ��
 * 
 * @author lanceyan�������޸��ߣ�
 * @version 1.0���°汾�ţ�
 * 
 */

public class ErrorInfo
{
    /**
     * ������
     */
    private long errorCode; // ������

    /**
     * ��������
     */
    private String errorMessage; // ������Ϣ

    /**
     * ���ô�����Ϣ
     * @param errorCode ������
     * @param errorMessage  ������Ϣ
     */
    public void setErrorInfo(long errorCode, String errorMessage)
    {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    /**
     * ���ô�����Ϣ��֧�ָ�ʽ���ַ�
     * @param errorCode ������
     * @param errorMessage ��ʽ���ַ�
     * @param args ��ʽ���ַ����
     * @author hokyhu
     */
    public void setErrorInfo(long errorCode, String errorMessage, Object ... args)
    {
    	setErrorInfo(errorCode, new Formatter().format(errorMessage, args).toString());
    }

    /**
     * ��ȡ��ǰ������Ϣ��Ĵ�����
     * @return the errorCode  ������
     */
    public long getErrorCode()
    {
        return errorCode;
    }

    /**
     * ���ô�����
     * @param errorCode
     *            the errorCode to set
     */
    public void setErrorCode(long errorCode)
    {
        this.errorCode = errorCode;
    }

    /**
     * �õ�������Ϣ
     * @return the errorMessage
     */
    public String getErrorMessage()
    {
        return errorMessage;
    }
    /**
     * ���ô�����Ϣ
     * @param errorMessage  ������Ϣ
     */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}

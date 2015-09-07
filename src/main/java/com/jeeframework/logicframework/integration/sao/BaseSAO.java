/**
 * Copyright (C) 1998-2007 JEEFRAMEWORK Inc.All Rights Reserved.
 * 																	
 * FileName��BaseSAO.java					
 *			
 * Description����Ҫ�������ļ�������							 * 												 * History��
 * �汾��    ����      ����       ��Ҫ������ز���
 *  1.0   lanceyan  2008-2-2  Create	
 */

package com.jeeframework.logicframework.integration.sao;

import com.jeeframework.logicframework.beans.ContextManageBean;
import com.jeeframework.logicframework.integration.error.ErrorInfo;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;


/**
 * ����SAO�Ļ��࣬������ʶ���
 * ����SAO�������ⲿ�������̳�BaseSAO��
 * 
 * @author lanceyan�������޸��ߣ�
 * @version 1.0{�°汾�ţ�
 * 
 */

public abstract class BaseSAO implements ContextManageBean
{

    /**
     * ��ǰbean��������
     */
    protected BeanFactory context;

    /**
     * �õ����õ�SAO bean������
     */
    protected String beanName = null;

    // private long invokeBeginTime = 0; // �������ÿ�ʼʱ��
    //
    // private long invokeEndTime = 0; // �������ý���ʱ��


    /**
     * SAO�Ƿ������ײ㣬����new ��һ�� errorinfo���󴫵���Action���ֲ�
     */
    protected ErrorInfo errorInfo = new ErrorInfo();

    /**
     * �û��Զ���context����
     * @param beanFactory  �����Զ���������
     * @exception  org.springframework.beans.BeansException
     * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
     */
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException
    {
        this.context = beanFactory;
    }

    /**
     * �õ��̳�SAO�ľ���bean
     * @return bean������
     */
    public String getBeanName()
    {
        return beanName;
    }

    /**
     * ����bean������
     * @param beanName bean������
     * @see org.springframework.beans.factory.BeanNameAware#setBeanName(java.lang.String)
     */
    public void setBeanName(String beanName)
    {
        this.beanName = beanName;
    }

    /**
     * spring����ٷ���
     * ���������Լ������Զ���
     * @see org.springframework.beans.factory.DisposableBean#destroy()
     */
    public void destroy()
    {
    }

    /**
     * spring�ĳ�ʼ�������û�������д����ʼ������
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception
    {
        // TODO Auto-generated method stub

    }



    /**
     * �����࣬����ʵ�ֻ�ȡ��ǰ����ʵ��ĸ���
     * 
     * ֮�����ǳ��󷽷�������ΪҪ����������ʵ�֣���ȡ��������ĸ���
     * @return ���ص�ǰʵ����ľ���ĸ���
     */
    public abstract int getObjCount();


}

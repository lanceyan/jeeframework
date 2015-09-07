/**
 * Copyright (C) 2015-2015 JEEFRAMEWORK Inc.All Rights Reserved.
 * 																	
 * FileName��BizContext.java					
 *			
 * Description����Ҫ�������ļ�������							 												
 * History��
 * �汾��    ����           ����          ��Ҫ������ز���
 *  1.0   lanceyan        2009-2-16           Create	
 */

package com.jeeframework.logicframework.context.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * ���ڻ�ȡҵ����context������
 * 
 * @author lanceyan�������޸��ߣ�
 * @version 1.0���°汾�ţ�
 * 
 */

public class BizContext implements BeanFactoryAware
{

    private static BeanFactory context;

    /**
     * ����spring��bean���칤����
     * 
     * @param BeanFactory
     * @exception org.springframework.beans.BeansException
     * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
     */
    public void setBeanFactory(BeanFactory beanFac) throws BeansException
    {
        context = beanFac;

    }

    /**
     * ���bean�����ַ���һ�����ʵ��
     * @param name  ���bean�����ֻ�ȡ���ʵ��
     * @return ����һ������Ķ���
     */
    public static Object getBean(String name)
    {
        if (context == null)
        {
            throw new RuntimeException("����biz-context-core.xml����BizContext�࣡");
        }
        return context.getBean(name);
    }
}

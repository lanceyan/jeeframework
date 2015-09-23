package com.jeeframework.core.context.support;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.jeeframework.core.context.IocContext;
import com.jeeframework.core.context.IocContextHelper;

/**
 * ���� tomcat ��ioccontextʵ����
 * 
 * @author lanceyan
 *
 */
public class TomcatContextListener implements ServletContextListener, IocContextHelper
{
	private WebApplicationContext	context;	//spring �� webapplication context

	public void contextDestroyed(ServletContextEvent event)
	{
	}

	public void contextInitialized(ServletContextEvent event)
	{
		//��ȡtomcat��������
		ServletContext sc = event.getServletContext();
		//�õ�spring��������
		context = WebApplicationContextUtils.getWebApplicationContext(sc);
		//����context����
		IocContext.setHelper(this);
	}

	/* (non-Javadoc)
	 * @see com.jeeframework.netsdk.IocContextHelper#getClassInstance(java.lang.String)
	 */
	public Object getClassInstance(String beanName)
	{
		return context.getBean(beanName);
	}
}

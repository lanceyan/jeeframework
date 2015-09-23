package com.jeeframework.core.context.support;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jeeframework.core.context.IocContext;
import com.jeeframework.core.context.IocContextHelper;

/**
 * 
 *
 */
public class ConsoleContextHelper implements IocContextHelper
{
	private BeanFactory	factory	= null;

	public Object getClassInstance(String beanName)
	{
		return factory.getBean(beanName);
	}

	public static void load()
	{
		ConsoleContextHelper helper = new ConsoleContextHelper();
		helper.factory = new ClassPathXmlApplicationContext(new String[] { "classpath*:biz-context-core.xml", "classpath*:conf-spring/biz-context-*.xml" });
		IocContext.setHelper(helper);
	}
}

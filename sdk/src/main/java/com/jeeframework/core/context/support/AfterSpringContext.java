package com.jeeframework.core.context.support;

import org.springframework.context.support.AbstractApplicationContext;

public class AfterSpringContext {

	public void doAfter(Object applicationContext) {
		
		Thread.currentThread().setContextClassLoader(applicationContext.getClass().getClassLoader());
		
		AbstractApplicationContext xac = (AbstractApplicationContext) applicationContext;

		AfterLoadContextEvent afterLoadContextEvent = new AfterLoadContextEvent("afterLoadContext");
		// 在ApplicationContext中发布一个 ApplicationEvent
		xac.publishEvent(afterLoadContextEvent);

	}

}

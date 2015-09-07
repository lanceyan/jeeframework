package com.jeeframework.core.context;

public interface IocContextHelper
{
	/**
	 * @param beanName
	 * @return
	 */
	public Object getClassInstance(String beanName);
}

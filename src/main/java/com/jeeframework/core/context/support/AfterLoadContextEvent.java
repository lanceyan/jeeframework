package com.jeeframework.core.context.support;

import org.springframework.context.ApplicationEvent;

public class AfterLoadContextEvent extends ApplicationEvent {

	private String message="容器加载后事件";

	public AfterLoadContextEvent(Object source) {
		super(source);


	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}

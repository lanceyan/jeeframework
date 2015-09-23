/**
 * Copyright (C) 2015-2015 JEEFRAMEWORK Inc.All Rights Reserved.
 * 																	
 *			
 *  1.0   lanceyan        2008-11-28           Create	
 */

package com.jeeframework.core.context.support;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;

/**
 */

public class SpringContextLoader {
	/**
	 */
	public static final String CONFIG_LOCATION_DELIMITERS = ",; \t\n";

	/**
	 */
	private static ClassPathXmlApplicationContext context = null;

	/**
	 * 
	 */
	public static AbstractApplicationContext loadContext(String confPath) {
		if (context == null) {
			synchronized (SpringContextLoader.class) {
				if (context == null) {
					String[] configs = StringUtils.tokenizeToStringArray(confPath, CONFIG_LOCATION_DELIMITERS);

					context = new ClassPathXmlApplicationContext(configs);
				}
			}
		}
		return context;
	}

	/**
	 * 
	 */
	public static ApplicationContext getContext() {
		return context;
	}

	public static ApplicationContext getContext(String confPath) {
		if (context == null) {
			return loadContext(confPath);
		}
		return context;
	}

}

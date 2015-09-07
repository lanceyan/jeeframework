/**
 * Copyright (C) 2015-2015 JEEFRAMEWORK Inc.All Rights Reserved.
 * 																	
 * FileName��Daemon.java					
 *			
 * Description����Ҫ�������ļ�������							 												
 * History��
 * �汾��    ����           ����          ��Ҫ������ز���
 *  1.0   lanceyan        2008-11-28           Create	
 */

package com.jeeframework.core.context;

import org.springframework.context.ApplicationContext;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;


/**
 * ����spring��deamon����
 * 
 * ʹ�÷��� java -classpath .:./lib com.jeeframework.sdk.kernel.context.Daemon
 * daemon.properties
 * 
 * @author lanceyan�������޸��ߣ�
 * @version 1.0���°汾�ţ�
 * @see �ο���JavaDoc
 */

public class Daemon {

	/**
	 * Daemon ����������
	 * 
	 * @param argv
	 *            main������������������
	 */
	public static void main(String argv[]) {
		if (argv.length != 1) {
			usage("Wrong number of arguments.");
		}

		try {
			Properties prop = PropertiesLoaderUtils.loadAllProperties(argv[0]);
			String configFile = (String) prop.get("contextConfigLocation");
			if (configFile == null || configFile.trim().length() <= 0) {
				usage(" Daemon configfile set error or  null.   ");
			}

			ApplicationContext ac = com.jeeframework.core.context.support.SpringContextLoader.getContext(configFile);

			ClassLoader contextClassLoader = ac.getClass().getClassLoader();

			Class afterSpringContext = contextClassLoader.loadClass("com.jeeframework.core.context.support.AfterSpringContext");

			// Class cc =
			// Class.forName("com.jeeframework.logicframework.common.remote.AfterSpringContext");
			Object afterSpringContextObj = afterSpringContext.newInstance();

			// Method method =
			// cc.getDeclaredMethod("doAfter1" );
			//										
			// Object obj1 = method.invoke(cobj);

			Method method = afterSpringContext.getDeclaredMethod("doAfter", Object.class);
			method.invoke(afterSpringContextObj, ac);

		} catch (IOException e) {
			usage("read configfile error .   " + e);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * ʹ�õķ�������ӡʹ�÷���
	 * 
	 * @param msg
	 *            �������message
	 */
	static void usage(String msg) {
		System.err.println(msg);
		System.err.println("Usage: java " + Daemon.class.getName() + "  configFilePath ");
		System.exit(1);
	}
}

/**
 * Copyright (C) 1998-2007 JEEFRAMEWORK Inc.All Rights Reserved.
 * 																	
 * FileName��ConfigDataSource.java					
 *			
 * Description����Ҫ�������ļ�������							 * 												 * History��
 * �汾��    ����      ����       ��Ҫ������ز���
 *  1.0   lanceyan  2008-5-16  Create	
 */

package com.jeeframework.logicframework.integration.dao.mongodb;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 
 * @author ake
 *
 */

public abstract class MongodbAccessor implements FactoryBean, InitializingBean {


	private DataSource dataSource;
	
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	
	public void afterPropertiesSet() throws Exception {
		if (getDataSource() == null) {
			throw new IllegalArgumentException("Property 'dataSource' is required");
		}
	}

	
	public Class<MongodbAccessor> getObjectType() {
		return MongodbAccessor.class;
	}

	// mongo对象每次返回都是同一个
	
	public boolean isSingleton() {
		return true;
	}

	
	public Object getObject() throws Exception {
		return null;
	}

}

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

import org.springframework.beans.factory.InitializingBean;

import com.jeeframework.util.validate.Validate;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoOptions;
import com.mongodb.ServerAddress;

/**
 * 配置的mongodb数据源
 * @author ake
 *
 */

public class MongodbDataSource implements InitializingBean,DataSource{

	private String host;
	private int port;
	private String userName;//
	private String passWord;//
	private String dbName;// 数据库名
	private boolean autoConnectRetry = true;
	private int connectionsPerHost = 10;
	private int maxWaitTime = 5000;
	private int socketTimeout = 0;
	private int connectTimeout = 0;
	private int threadsAllowedToBlockForConnectionMultiplier = 5;
			
	private Mongo mongo;
	private DB db;
	
	public MongodbDataSource(){
		
	}
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public boolean isAutoConnectRetry() {
		return autoConnectRetry;
	}

	public void setAutoConnectRetry(boolean autoConnectRetry) {
		this.autoConnectRetry = autoConnectRetry;
	}

	public int getConnectionsPerHost() {
		return connectionsPerHost;
	}

	public void setConnectionsPerHost(int connectionsPerHost) {
		this.connectionsPerHost = connectionsPerHost;
	}

	public int getMaxWaitTime() {
		return maxWaitTime;
	}

	public void setMaxWaitTime(int maxWaitTime) {
		this.maxWaitTime = maxWaitTime;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getThreadsAllowedToBlockForConnectionMultiplier() {
		return threadsAllowedToBlockForConnectionMultiplier;
	}

	public void setThreadsAllowedToBlockForConnectionMultiplier(
			int threadsAllowedToBlockForConnectionMultiplier) {
		this.threadsAllowedToBlockForConnectionMultiplier = threadsAllowedToBlockForConnectionMultiplier;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	
	
	public DB getDB() {
		return this.db;
	}
	

	
	public void afterPropertiesSet() throws Exception {
		if (Validate.isEmpty(host)) {
			throw new RuntimeException("数据库主机ip为空");
		}
		if (port <= 0) {
			throw new RuntimeException("数据库端口 输入错误");
		}
		if (Validate.isEmpty(dbName)) {
			throw new RuntimeException("数据库名称为空");
		}
		
		MongoOptions options = new MongoOptions();  
        options.autoConnectRetry = autoConnectRetry;  
        options.connectionsPerHost = connectionsPerHost;  
        options.maxWaitTime = maxWaitTime;  
        options.socketTimeout = socketTimeout;  
        options.connectTimeout = connectTimeout;  
        options.threadsAllowedToBlockForConnectionMultiplier = threadsAllowedToBlockForConnectionMultiplier; 
        mongo = new Mongo(new ServerAddress(host, port), options);  
//		mongo = new Mongo(host, port);

		db = mongo.getDB(dbName);
		if (db == null) {
			throw new RuntimeException("数据库： " + dbName + "  不存在");
		}

		// 验证用户名密码
		if (!Validate.isEmpty(userName)) {

			if (Validate.isNull(passWord)) {
				passWord = "";
			}

			boolean isVerify = db.authenticate(userName, passWord.toCharArray());

			if (!isVerify) {
				throw new RuntimeException("数据库： " + dbName + "  用户名密码输入错误");
			}
		}
	}

	public void close() {
		if (this.mongo != null) {
			this.mongo.close();
		}
	}
}

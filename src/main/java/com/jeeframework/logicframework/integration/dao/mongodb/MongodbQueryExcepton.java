package com.jeeframework.logicframework.integration.dao.mongodb;

import org.springframework.dao.NonTransientDataAccessException;

public class MongodbQueryExcepton extends NonTransientDataAccessException{
	/**
	 * Constructor for NonTransientDataAccessResourceException.
	 * @param msg the detail message
	 */
	public MongodbQueryExcepton(String msg) {
		super(msg);
	}

	/**
	 * Constructor for NonTransientDataAccessResourceException.
	 * @param msg the detail message
	 * @param cause the root cause from the data access API in use
	 */
	public MongodbQueryExcepton(String msg, Throwable cause) {
		super(msg, cause);
	}
}

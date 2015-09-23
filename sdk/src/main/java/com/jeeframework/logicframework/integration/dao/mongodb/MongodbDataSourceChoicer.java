package com.jeeframework.logicframework.integration.dao.mongodb;

public class MongodbDataSourceChoicer {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String> ();

    public static void setCurrentDataSource(String CurrentDataSource) {
	contextHolder.set(CurrentDataSource);
    }

    public static String getCurrentDataSource() {
	return contextHolder.get();
    }

    public static void clearCurrentDataSource() {
	contextHolder.remove();
    }
}

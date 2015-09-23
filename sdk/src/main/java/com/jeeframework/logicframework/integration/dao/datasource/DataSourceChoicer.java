package com.jeeframework.logicframework.integration.dao.datasource;

/** 
  * ���Դѡ���࣬��ݵ�ǰ�߳�key����ѡ��
  * @author lanceyan�������޸��ߣ�
  * @version 1.0���°汾�ţ�
  * 
*/
	
public class DataSourceChoicer {
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

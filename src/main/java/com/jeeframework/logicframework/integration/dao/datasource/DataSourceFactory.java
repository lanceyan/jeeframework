package com.jeeframework.logicframework.integration.dao.datasource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.sql.Connection;
import java.sql.SQLException;
//import LoggerUtil;

/**
 * ���Դ�Ĺ�����
 * 
 * @author lanceyan�������޸��ߣ�
 * @version 1.0���°汾�ţ�
 * 
 */

public class DataSourceFactory extends AbstractRoutingDataSource {

	/**
	 * ������ô˷�������һ�����Դ��key
	 * 
	 * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
	 * @exception �쳣��ע�ͳ�ʲô�����»���ʲô����쳣
	 * @see org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource#determineCurrentLookupKey()
	 */
	protected Object determineCurrentLookupKey() {
		// ���ѡ�����ѡ��̬�����Դ
		// return DataSourceChoicer.getCurrentDataSource();
		if (DataSourceChoicer.getCurrentDataSource() != null) {
			// System.out.println("�õ���ݿ�����Ϊ "+
			// DataSourceChoicer.getCurrentDataSource() + " "+
			// DataSourceRouterManager.getResource(DataSourceChoicer.getCurrentDataSource()));
			// return
			// DataSourceRouterManager.getResource(DataSourceChoicer.getCurrentDataSource());
			return DataSourceChoicer.getCurrentDataSource();
		}
		return null;
	}

	/**
	 * ����һ����ݿ������
	 * 
	 * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
	 * @exception �쳣��ע�ͳ�ʲô�����»���ʲô����쳣
	 * @see org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource#getConnection()
	 */
	public Connection getConnection() throws SQLException {
//		BasicDataSource bds = (BasicDataSource) determineTargetDataSource();
		Connection con = determineTargetDataSource().getConnection();

		// System.out.println("��ǰ���ӵĶ���Ϊ �� " + con);

//		Logger logger1 = LoggerUtil.getLogger();
//		logger1.errorTrace(Thread.currentThread().getName() + "  ��ǰ�Ļ�Ծ������Ϊ��   " + bds.getNumActive());

		return con;

	}

	/**
	 * ����û������뷵����ݿ�����
	 * 
	 * @param username:
	 *            �û���
	 * @param password:
	 *            ����
	 * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
	 * @exception �쳣��ע�ͳ�ʲô�����»���ʲô����쳣
	 * @see org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource#getConnection(java.lang.String,
	 *      java.lang.String)
	 */
	public Connection getConnection(String username, String password) throws SQLException {
		return determineTargetDataSource().getConnection(username, password);
	}

//	public java.util.logging.Logger getParentLogger()
//			throws SQLFeatureNotSupportedException {
//		// TODO Auto-generated method stub
//		return null;
//	}


}

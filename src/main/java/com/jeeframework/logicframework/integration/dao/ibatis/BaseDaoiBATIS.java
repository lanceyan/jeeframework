package com.jeeframework.logicframework.integration.dao.ibatis;

import com.jeeframework.logicframework.beans.ContextManageBean;
import com.jeeframework.logicframework.util.logging.LoggerUtil;
import com.jeeframework.logicframework.integration.dao.datasource.DataSourceRouterManager;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public abstract class BaseDaoiBATIS  implements ContextManageBean {
        @Autowired  
        protected SqlSessionTemplate sqlSessionTemplate; 
    

	protected BeanFactory context;


	protected String beanName = null;



	public final static String TABLE_KEY_NAME = "tableKey";


	// private SqlExecutor SqlExecutor = null;
	//
	// private Map<String, String> routerKeyMap = null;

	private PlatformTransactionManager transactionManager = null;

	// private TransactionStatus status = null;

	// private static final String DONT_PARTITION_TABLE = null;


	private String dataSourceKey = null;

	public void setBeanFactory(BeanFactory beanFactory) {
		this.context = beanFactory;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public void destroy() {
	}


	public Object queryForObject(String statementName) throws DataAccessException {
		return sqlSessionTemplate.selectOne(statementName);
	}

	// public Object queryForObject(int tableKey, String statementName) throws
	// DataAccessException
	// {
	// setTableKey(tableKey);
	// return getSqlMapClientTemplate().queryForObject(statementName);
	// }

	public Object queryForObject(String statementName, Object parameterObject) throws DataAccessException {
		return sqlSessionTemplate.selectOne(statementName, parameterObject);
	}


	public List queryForList(String statementName) throws DataAccessException {
		return sqlSessionTemplate.selectList(statementName);
	}


	public List queryForList(String statementName, Object parameterObject) throws DataAccessException {
		return sqlSessionTemplate.selectList(statementName, parameterObject);
	}

	public Map queryForMap(String statementName, Object parameterObject, String keyProperty) throws DataAccessException {
		return sqlSessionTemplate.selectMap(statementName, parameterObject, keyProperty);
	}

	public Object insert(String statementName) throws DataAccessException {
		return sqlSessionTemplate.insert(statementName);
	}

	public Object insert(String statementName, Object parameterObject) throws DataAccessException {
		return sqlSessionTemplate.insert(statementName, parameterObject);
	}

	public int update(String statementName) throws DataAccessException {
		return sqlSessionTemplate.update(statementName);
	}

	// public int update(int tableKey, String statementName) throws
	// DataAccessException
	// {
	// setTableKey(tableKey);
	// return getSqlMapClientTemplate().update(statementName);
	// }

	public int update(String statementName, Object parameterObject) throws DataAccessException {
		return sqlSessionTemplate.update(statementName, parameterObject);
	}


	public int delete(String statementName) throws DataAccessException {
		return sqlSessionTemplate.delete(statementName);
	}


	public int delete(String statementName, Object parameterObject) throws DataAccessException {
		return sqlSessionTemplate.delete(statementName, parameterObject);
	}



	public void startTransaction() {
		Connection con = null;
		try {
			con = sqlSessionTemplate.getConnection();

			if (con.getAutoCommit()) {
					LoggerUtil.debugTrace("set JDBC Connection [" + con + "] to manual commit");
				con.setAutoCommit(false);
			}


		} catch (SQLException e) {
			try {
				con.close();
			} catch (SQLException ex) {
				LoggerUtil.errorTrace("Could not close JDBC Connection", ex);
			}
			throw new DataAccessResourceFailureException("Could not open JDBC Connection for transaction", e);
		}

	}

	public void commit() {

		Connection con = null;
		try {
			con = sqlSessionTemplate.getConnection();
			con.commit();
		} catch (SQLException e) {
			throw new DataAccessResourceFailureException("Could not commit JDBC transaction", e);
		} finally {
			try {

				con.setAutoCommit(true);

				if (con.isReadOnly()) {

						LoggerUtil.debugTrace("Resetting read-only flag of JDBC Connection [" + con + "]");

					con.setReadOnly(false);
				}

				con.close();

			} catch (Throwable ex) {
				LoggerUtil.errorTrace("Could not reset JDBC Connection after transaction", ex);
			}

		}
	}


	public void rollback() {

		Connection con = null;
		DataSource dataSource = null;
		try {
			con = sqlSessionTemplate.getConnection();
			con.rollback();
		} catch (SQLException e) {
			throw new DataAccessResourceFailureException("Could not commit JDBC transaction", e);
		} finally {
			TransactionSynchronizationManager.unbindResource(dataSource);
			try {

				con.setAutoCommit(true);

				if (con.isReadOnly()) {

					LoggerUtil.debugTrace("Resetting read-only flag of JDBC Connection [" + con + "]");

					con.setReadOnly(false);
				}

				con.close();

			} catch (Throwable ex) {
				LoggerUtil.errorTrace("Could not reset JDBC Connection after transaction", ex);
			}

			// DataSourceUtils.releaseConnection(con, dataSource);
		}
	}


	public PlatformTransactionManager getTransactionManager() {
		return transactionManager;
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	/**
	 * @param dataSourceKey
	 *            the dataSourceKey to set
	 */
	public void setDataSourceKey(String dataSourceKey) {

		this.dataSourceKey = dataSourceKey;

	}

	protected void initDao() throws Exception {
		if (dataSourceKey != null) {
			DataSourceRouterManager.bindResource(beanName, dataSourceKey);
		}
	}


}

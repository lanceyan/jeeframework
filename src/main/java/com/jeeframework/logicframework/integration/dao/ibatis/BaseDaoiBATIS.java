package com.jeeframework.logicframework.integration.dao.ibatis;

import com.jeeframework.logicframework.beans.ContextManageBean;
import com.jeeframework.logicframework.common.logging.LoggerUtil;
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

//import com.ibatis.sqlmap.client.SqlMapExecutor;
//import com.ibatis.sqlmap.client.event.RowHandler;

/**
 * ibatis��DAO���࣬��װ�˻��Ԫ�ط��� ����ibatis��DAO������̳� BaseDaoiBATIS
 * 
 * @author lanceyan�������޸��ߣ�
 * @version 1.0���°汾�ţ�
 */

public abstract class BaseDaoiBATIS  implements ContextManageBean {
        @Autowired  
        protected SqlSessionTemplate sqlSessionTemplate; 
    
    /**
	 * ��ǰbean��������
	 */
	protected BeanFactory context;

	/**
	 * �õ����õ�SAO bean������
	 */
	protected String beanName = null;


	/**
	 * Ĭ�ϵķֱ� key ֵ
	 */
	public final static String TABLE_KEY_NAME = "tableKey";

	// private long invokeBeginTime = 0; // �������ÿ�ʼʱ��

	// private long invokeEndTime = 0; // �������ý���ʱ��

	// private SqlExecutor SqlExecutor = null;
	//
	// private Map<String, String> routerKeyMap = null;

	// �����������
	private PlatformTransactionManager transactionManager = null;

	// private TransactionStatus status = null;

	// private static final String DONT_PARTITION_TABLE = null;

	/**
	 * ���ڷֿ��dataSourceKey
	 */
	private String dataSourceKey = null;

	/**
	 * �û��Զ���context����
	 * 
	 * @param beanFactory
	 *            �����Զ���������
	 * @exception org.springframework.beans.BeansException
	 * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
	 */
	public void setBeanFactory(BeanFactory beanFactory) {
		this.context = beanFactory;
	}

	/**
	 * �õ��̳�SAO�ľ���bean
	 * 
	 * @return bean������
	 */
	public String getBeanName() {
		return beanName;
	}

	/**
	 * ����bean������
	 * 
	 * @param beanName
	 *            bean������
	 * @see org.springframework.beans.factory.BeanNameAware#setBeanName(java.lang.String)
	 */
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	/**
	 * spring����ٷ��� ���������Լ������Զ���
	 * 
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	public void destroy() {
	}




	/**
	 * �����࣬����ʵ�ֻ�ȡ��ǰ����ʵ��ĸ���
	 * 
	 * ֮�����ǳ��󷽷�������ΪҪ����������ʵ�֣���ȡ��������ĸ���
	 * 
	 * @return ���ص�ǰʵ����ľ���ĸ���
	 */
	public abstract int getObjCount();

	/**
	 * ��װ��ѯ�ӿ�
	 * 
	 * @param ����˵��
	 *            ��statementName ����sql���id
	 * @return ��ݲ�ѯ����һ������Ķ�����Ҫǿ��ת�������ﷵ��һ��Object��
	 * @exception org.springframework.dao.DataAccessException
	 */
	public Object queryForObject(String statementName) throws DataAccessException {
		return sqlSessionTemplate.selectOne(statementName);
	}

	/**
	 * ��װ��ѯ�ӿڸ��tableKey·�ɱ�
	 * 
	 * @param ����˵��
	 *            ��tableKey ���tableKey·�ɱ?key������qq�ŵ�
	 * @param ����˵��
	 *            ��statementName ����sql���id
	 * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
	 * @exception �쳣
	 *                ��ע�ͳ�ʲô�����»���ʲô����쳣
	 * 
	 */
	// public Object queryForObject(int tableKey, String statementName) throws
	// DataAccessException
	// {
	// setTableKey(tableKey);
	// return getSqlMapClientTemplate().queryForObject(statementName);
	// }
	/**
	 * ��װ��ѯ�ӿ�
	 * 
	 * @param ����˵��
	 *            ��statementName ����sql���id
	 * @param ����˵��
	 *            ��parameterObject ����sql���������
	 * @return ��ݲ�ѯ����һ������Ķ�����Ҫǿ��ת�������ﷵ��һ��Object��
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryForObject(String,
	 *      Object)
	 * @throws org.springframework.dao.DataAccessException
	 *             in case of errors
	 */
	public Object queryForObject(String statementName, Object parameterObject) throws DataAccessException {
		return sqlSessionTemplate.selectOne(statementName, parameterObject);
	}

	/**
	 * ��װ��ѯ�ӿ�
	 * 
	 * @param ����˵��
	 *            ��statementName ����sql���id
	 * @return ����һ��list��list��ܶ���ݶ���
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryForList(String)
	 * @throws org.springframework.dao.DataAccessException
	 *             in case of errors
	 */
	public List queryForList(String statementName) throws DataAccessException {
		return sqlSessionTemplate.selectList(statementName);
	}

	/**
	 * ��װ��ѯ�ӿ�
	 * 
	 * @param ����˵��
	 *            ��statementName ����sql���id
	 * @param ����˵��
	 *            ��parameterObject ����sql���������
	 * @return ����һ��list��list��ܶ���ݶ���
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryForList(String, Object)
	 * @throws org.springframework.dao.DataAccessException
	 *             in case of errors
	 */
	public List queryForList(String statementName, Object parameterObject) throws DataAccessException {
		return sqlSessionTemplate.selectList(statementName, parameterObject);
	}

	/**
	 * ��װ��ѯ�ӿ�
	 * 
	 * @param ����˵��
	 *            ��statementName ����sql���id
	 * @param ����˵��
	 *            ��parameterObject ����sql���������
	 * @param ����˵��
	 *            ��keyProperty ��ݹؼ��ֲ�ѯ����map
	 * @return ����ֵ�����ز�ѯMap
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryForMap(String, Object,
	 *      String)
	 * @throws org.springframework.dao.DataAccessException
	 *             in case of errors
	 */
	public Map queryForMap(String statementName, Object parameterObject, String keyProperty) throws DataAccessException {
		return sqlSessionTemplate.selectMap(statementName, parameterObject, keyProperty);
	}

	/**
	 * ��װ����ӿ�
	 * 
	 * @param ����˵��
	 *            ��statementName ����sql���id
	 * @return ����ֵ�����뷵�صĶ��󣬹ؼ������Զ���key����ͨ��insertKey����
	 * 
	 *         <pre>
	 *         &lt;!-- Oracle SEQUENCE --&gt;   
	 *         &lt;insert id="insertProduct-ORACLE" parameterClass="com.domain.Product"&gt;   
	 *            &lt;selectKey resultClass="int" keyProperty="id" type="pre"&gt;   
	 *            &lt;![CDATA[SELECT STOCKIDSEQUENCE.NEXTVAL AS ID FROM DUAL]]&gt;   
	 *            &lt;/selectKey&gt;   
	 *            &lt;![CDATA[insert into PRODUCT (PRD_ID,PRD_DESCRIPTION) values #id#,#description#)]]&gt;   
	 *         &lt;/insert&gt;   
	 *         &lt;!-- Mysql Last Insert Id --&gt;  
	 *         &lt;insert id="insertProduct-Mysql" parameterClass="com.domain.Product"&gt;  
	 *           &lt;![CDATA[insert into PRODUCT(PRD_DESCRIPTION) values (#description#)]]&gt;   
	 *           &lt;selectKey resultClass="int" keyProperty="id"&gt;  
	 *           &lt;![CDATA[SELECT LAST_INSERT_ID() AS ID ]]&gt; 
	 *           &lt;/selectKey&gt;  
	 *         &lt;/insert&gt;
	 * 
	 * </pre>
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#insert(String)
	 * @throws org.springframework.dao.DataAccessException
	 *             in case of errors
	 */
	public Object insert(String statementName) throws DataAccessException {
		return sqlSessionTemplate.insert(statementName);
	}

	/**
	 * ��װ����ӿ�
	 * 
	 * @param ����˵��
	 *            ��statementName ����sql���id
	 * @param ����˵��
	 *            ��parameterObject ����sql���������
	 * @return ����ֵ�����뷵�صĶ��󣬹ؼ������Զ���
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#insert(String, Object)
	 * @throws org.springframework.dao.DataAccessException
	 *             in case of errors
	 */
	public Object insert(String statementName, Object parameterObject) throws DataAccessException {
		return sqlSessionTemplate.insert(statementName, parameterObject);
	}

	/**
	 * ��װ�޸Ľӿ�
	 * 
	 * @param ����˵��
	 *            ��statementName ����sql���id
	 * @return ����ֵ���޸�Ӱ�������
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#update(String)
	 * @throws org.springframework.dao.DataAccessException
	 *             in case of errors
	 */
	public int update(String statementName) throws DataAccessException {
		return sqlSessionTemplate.update(statementName);
	}

	/**
	 * ��װ�޸Ľӿڸ��tableKey·�ɱ�
	 * 
	 * @param ����˵��
	 *            ��tableKey ���tableKey·�ɱ?key������qq�ŵ�
	 * @param ����˵��
	 *            ��statementName ����sql���id
	 * @return ����ֵ���޸�Ӱ�������
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#update(String)
	 * @throws org.springframework.dao.DataAccessException
	 *             in case of errors
	 */
	// public int update(int tableKey, String statementName) throws
	// DataAccessException
	// {
	// setTableKey(tableKey);
	// return getSqlMapClientTemplate().update(statementName);
	// }
	/**
	 * ��װ�޸Ľӿ�
	 * 
	 * @param ����˵��
	 *            ��statementName ����sql���id
	 * @param ����˵��
	 *            ��parameterObject ����sql���������
	 * @return ����ֵ���޸�Ӱ�������
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#update(String, Object)
	 * @throws org.springframework.dao.DataAccessException
	 *             in case of errors
	 */
	public int update(String statementName, Object parameterObject) throws DataAccessException {
		return sqlSessionTemplate.update(statementName, parameterObject);
	}

	/**
	 * ��װɾ��ӿ�
	 * 
	 * @param ����˵��
	 *            ��statementName ����sql���id
	 * @return ����ֵ��ɾ��Ӱ�������
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#delete(String)
	 * @throws org.springframework.dao.DataAccessException
	 *             in case of errors
	 */
	public int delete(String statementName) throws DataAccessException {
		return sqlSessionTemplate.delete(statementName);
	}


	/**
	 * ��װɾ��ӿ�
	 * 
	 * @param ����˵��
	 *            ��statementName ����sql���id
	 * @param ����˵��
	 *            ��parameterObject ����sql���������
	 * @return ����ֵ��ɾ��Ӱ�������
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#delete(String, Object)
	 * @throws org.springframework.dao.DataAccessException
	 *             in case of errors
	 */
	public int delete(String statementName, Object parameterObject) throws DataAccessException {
		return sqlSessionTemplate.delete(statementName, parameterObject);
	}


	/**
	 * �ֶ����񣬿�ʼ������ã�����������commit��rollback������ᵼ�����Ӳ����ͷš�
	 */
	public void startTransaction() {
		// if (transactionManager == null)
		// {
		// throw new SdkRuntimeException("transactionManager û�����ã�");
		// }
		// ((DataSourceTransactionManagerWrapper)
		// transactionManager).setDataSource(getSqlMapClientTemplate()
		// .getDataSource());

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

		// status = transactionManager.getTransaction(null); // �������״̬
	}

	/**
	 * �ֶ�����startTransaction���ύ�����ͷ������
	 */
	public void commit() {
		// if (transactionManager == null)
		// {
		// throw new SdkRuntimeException("transactionManager û�����ã�");
		// }
		// if (status == null)
		// {
		// throw new SdkRuntimeException("���ȵ���startTransaction ��");
		// }
		// transactionManager.commit(status); // �ύ����

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

			// DataSourceUtils.releaseConnection(con, dataSource);
		}
	}

	/**
	 * �ֶ�����startTransaction�������ʧ����Ҫrollback�ع���
	 */
	public void rollback() {
		// if (transactionManager == null)
		// {
		// throw new SdkRuntimeException("transactionManager û�����ã�");
		// }
		// if (status == null)
		// {
		// throw new SdkRuntimeException("���ȵ���startTransaction ��");
		// }
		// transactionManager.rollback(status); // �ع�����;
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

	/**
	 * �õ����������
	 * 
	 * @return PlatformTransactionManager �������������
	 */
	public PlatformTransactionManager getTransactionManager() {
		return transactionManager;
	}

	/**
	 * �����Զ������������
	 * 
	 * @param transactionManager
	 *            the transactionManager to set
	 */
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	/**
	 * @param dataSourceKey
	 *            the dataSourceKey to set
	 */
	public void setDataSourceKey(String dataSourceKey) {
		// System.out.println(this.getClass() + " �������ã��������� �� " +
		// dataSourceKey);
		this.dataSourceKey = dataSourceKey;

	}

	protected void initDao() throws Exception {
		if (dataSourceKey != null) {
			DataSourceRouterManager.bindResource(beanName, dataSourceKey);
		}
	}


}

package com.jeeframework.logicframework.integration.dao.mongodb;

import com.jeeframework.logicframework.beans.ContextManageBean;
import com.jeeframework.logicframework.integration.dao.datasource.DataSourceRouterManager;
import com.jeeframework.logicframework.integration.dao.mongodb.support.MongodbDaoSupport;
import com.mongodb.DBObject;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.dao.DataAccessException;

import java.util.Map;

/**
 * mongodb��DAO���࣬��װ�˻��Ԫ�ط��� ����mongodb��DAO������̳� BaseDaoMongodb
 * @author ake
 *
 */

public abstract class BaseDaoMongodb extends MongodbDaoSupport implements ContextManageBean {

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

	
	
	public Object save(String collectionName, Map<String, Object> entity)throws DataAccessException{
		return getMongodbTemplate().save(collectionName, entity);
	}
	
	public Object insert(String collectionName, Map<String, Object> entity)throws DataAccessException{
		return getMongodbTemplate().insert(collectionName, entity);
	}
	
	public Object insert(Class clazz,String collectionName, Map<String, Object> entity)throws DataAccessException{
		return getMongodbTemplate().insert(clazz, collectionName, entity);
	}
	
	public int update(String collectionName,Map<String,Object> q, Map<String, Object> entity, boolean upsert, boolean multi)throws DataAccessException {
		return getMongodbTemplate().update(collectionName, q, entity, upsert, multi);
	}
	
	public <T> T findOne(Class<T> clazz, String collectionName, DBObject query)throws DataAccessException{
		return getMongodbTemplate().findOne(clazz, collectionName, query);
	}
	
	public <T> T findOne(Class<T> clazz, String collectionName, DBObject query, DBObject fields)throws DataAccessException{
		return getMongodbTemplate().findOne(clazz, collectionName, query, fields);
	}
	
	public int remove(String collectionName, DBObject query) throws DataAccessException {
		return getMongodbTemplate().remove(collectionName, query);
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
	 * @return the dataSourceKey
	 */
	public String getDataSourceKey() {
		return dataSourceKey;
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

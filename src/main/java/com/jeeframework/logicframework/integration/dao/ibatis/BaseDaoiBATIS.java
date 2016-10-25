package com.jeeframework.logicframework.integration.dao.ibatis;

import com.jeeframework.logicframework.beans.ContextManageBean;
import com.jeeframework.logicframework.integration.dao.datasource.DataSourceRouterManager;
import com.jeeframework.logicframework.integration.dao.transaction.DoTransactionCallback;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Map;

public abstract class BaseDaoiBATIS implements ContextManageBean {
    @Autowired
    protected SqlSessionTemplate sqlSessionTemplate;


    protected BeanFactory context;


    protected String beanName = null;


    public final static String TABLE_KEY_NAME = "tableKey";


    // private SqlExecutor SqlExecutor = null;
    //
    // private Map<String, String> routerKeyMap = null;
    @Autowired
    protected PlatformTransactionManager transactionManager;

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

    public PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    /**
     * @param dataSourceKey the dataSourceKey to set
     */
    public void setDataSourceKey(String dataSourceKey) {

        this.dataSourceKey = dataSourceKey;

    }

    protected void initDao() throws Exception {
        if (dataSourceKey != null) {
            DataSourceRouterManager.bindResource(beanName, dataSourceKey);
        }
    }


    /**
     * 启动编程式事务管理
     */
    public <T> T  doTransaction(final DoTransactionCallback<T> transactionCallback) {
        return doTransaction(TransactionDefinition.PROPAGATION_REQUIRED, TransactionDefinition.ISOLATION_READ_COMMITTED, transactionCallback);
    }

    /**
     * 启动编程式事务管理带参数
     *
     * @param propagation 事务的传播方式
     * @param isolation   事务的隔离方式
     */
    public <T> T doTransaction(int propagation, int isolation, final DoTransactionCallback<T> transactionCallback) {

        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        return transactionTemplate.execute(new TransactionCallback<T>() {
            @Override
            public T doInTransaction(TransactionStatus status) {
                return transactionCallback.doInTransaction();
            }
        });
    }

}

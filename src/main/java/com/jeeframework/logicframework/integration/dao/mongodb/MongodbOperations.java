package com.jeeframework.logicframework.integration.dao.mongodb;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.mongodb.DBObject;

/**
 * 
 * @author ake
 *
 */
public interface MongodbOperations {
	public Object save(String collectionName, Map<String, Object> entity)throws DataAccessException;
	
	public Object insert(String collectionName, Map<String, Object> entity)throws DataAccessException;
	
	public Object insert(Class clazz,String collectionName, Map<String, Object> entity)throws DataAccessException;
	
	public int update(String collectionName,Map<String,Object> q, Map<String, Object> entity, boolean upsert, boolean multi)throws DataAccessException ;
	
	public <T> T findOne(Class<T> clazz, String collectionName, DBObject query)throws DataAccessException ;
	
	public <T> T findOne(Class<T> clazz, String collectionName, DBObject query ,DBObject fields)throws DataAccessException ;
	
    public <T> List<T> findList(Class<T> clazz, String collectionName, DBObject query)throws DataAccessException;
    
    public <T> List<T> findList(Class<T> clazz, String collectionName, DBObject query ,DBObject fields)throws DataAccessException;
    
    public <T> List<T> findList(Class<T> clazz, String collectionName, DBObject query ,DBObject fields,DBObject orderBy)  throws DataAccessException;
    
    public <T> List<T> findList(Class<T> clazz, String collectionName, DBObject query ,DBObject fields ,DBObject orderBy ,int skip,int limit)throws DataAccessException;
    
    public int remove(String collectionName, DBObject query) throws DataAccessException ;

}

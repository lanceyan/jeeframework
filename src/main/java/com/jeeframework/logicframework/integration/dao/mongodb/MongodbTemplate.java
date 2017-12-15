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

import com.jeeframework.util.validate.Validate;
import com.mongodb.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.processors.PropertyNameProcessor;
import org.apache.commons.beanutils.PropertyUtils;
import org.bson.types.ObjectId;
import org.springframework.dao.DataAccessException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * 
 * @author ake
 * 
 */

public class MongodbTemplate extends MongodbAccessor implements MongodbOperations {

    public DB getDB() {
        return getDataSource().getDB();
    }

    public void afterPropertiesSet() throws Exception {

    }

    // mongo对象每次返回都是同一个

    public boolean isSingleton() {
        return true;
    }

    public Object save(String collectionName, Map<String, Object> entity) throws DataAccessException {

        int result = 0;

        try {
            DBCollection dbCol = getDB().getCollection(collectionName);

            DBObject entityDbObj = new BasicDBObject();

            Set<String> keySet = entity.keySet();
            for (String key : keySet) {
                Object val = entity.get(key);

                if (val != null) {
                    key = key.trim().toLowerCase();
                    entityDbObj.put(key, val); // 对 MongoDB BasicDBObject 对象 进行赋值
                }
            }

            WriteResult ret = dbCol.save(entityDbObj, WriteConcern.SAFE);
//            CommandResult err = ret.getLastError();
//            if (null != err && null != err.get("err")) {
//                throw err.getException();
//            } else {
                result = ret.getN();
//            }
        } catch (MongoException e) {
            throw new MongodbQueryExcepton(e.getMessage(), e);
        }

        return result;
    }

    public Object insert(String collectionName, Map<String, Object> entity) throws DataAccessException {
        return this.insert(null, collectionName, entity);
    }

    public Object insert(Class clazz, String collectionName, Map<String, Object> entity) throws DataAccessException {

        Object result = 0;

        if (null != entity) {
            try {

                //				Mongo mongo = new Mongo("58.246.182.22",27017);
                //				DB db = mongo.getDB("bookoo_sse_test");
                //				DBCollection dbCol = db.getCollection(collectionName);

                DBCollection dbCol = getDB().getCollection(collectionName);
                BasicDBObject entityDbObj = new BasicDBObject();
                if (null != clazz) {
                    Object bean = newInstance(clazz);

                    if (clazz.isAssignableFrom(HashMap.class)) {
                        Set<String> keySet = entity.keySet();
                        for (String key : keySet) {
                            key = key.trim().toLowerCase();
                            Object val = entity.get(key);
                            if (!Validate.isEmpty(key) && val != null) {
                                entityDbObj.put(key, val);
                            }
                        }
                    } else {
                        Set<String> keySet = entity.keySet();
                        for (String key : keySet) {
                            String name = processPropertyName(clazz, key);
                            PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(bean, name);
                            if (pd != null) {
                                Object val = entity.get(key);
                                if (val != null) {
                                    Class targetType = pd.getPropertyType();
                                    if (!targetType.isInstance(val)) {
                                        throw new MongodbQueryExcepton("Property '" + key + "' of " + bean.getClass() + " data type mismatch.");
                                    }
                                    key = key.trim().toLowerCase();
                                    entityDbObj.put(key, val); // 对 MongoDB BasicDBObject 对象 进行赋值
                                }
                            } else {
                                throw new NoSuchMethodException("Unknown property '" + name + "' on class '" + bean.getClass() + "'");
                            }
                        }
                    }
                } else {
                    Set<String> keySet = entity.keySet();
                    for (String key : keySet) {
                        Object val = entity.get(key);
                        if (val != null) {
                            key = key.trim().toLowerCase();
                            entityDbObj.put(key, val); // 对 MongoDB BasicDBObject 对象 进行赋值
                        }
                    }
                }
                Object id = entityDbObj.get("_id");
                if (id == null) {
                    id = ObjectId.get();
                    entityDbObj.put("_id", id);
                }
                WriteResult ret = dbCol.insert(entityDbObj, WriteConcern.SAFE);
                //				WriteResult ret = dbCol.insert(entityDbObj);
//                CommandResult err = ret.getLastError();
//                if (null != err && null != err.get("err")) {
//                    throw err.getException();
//                } else {
                    //					result = ret.getN();
                    result = id;
//                }
            } catch (MongoException me) {
//                if (me instanceof MongoException.DuplicateKey) {
//                    throw new MongodbQueryExcepton("DuplicateKey", me);
//                }
                throw new MongodbQueryExcepton(me.getMessage(), me);
            } catch (Exception e) {
                throw new MongodbQueryExcepton(e.getMessage(), e);
            }
        }

        return result;
    }

    public int update(String collectionName, Map<String, Object> q, Map<String, Object> entity, boolean upsert, boolean multi) throws DataAccessException {
        int result = 0;

        try {
            DBCollection dbCol = getDB().getCollection(collectionName);

            DBObject queryObj = new BasicDBObject();

            if (null != q) {
                Set<String> keySet = q.keySet();
                for (String key : keySet) {
                    Object val = q.get(key);

                    if (val != null) {
                        key = key.trim().toLowerCase();
                        queryObj.put(key, val); // 对 MongoDB BasicDBObject 对象 进行赋值
                    }
                }
            }

            BasicDBObject entityDbObj = new BasicDBObject();
            Set<String> keySet = entity.keySet();
            for (String key : keySet) {
                DBObject val = (DBObject) entity.get(key);

                if (val != null) {
                    if (key.startsWith("$")) {
                        //						entityDbObj.append(key, val);
                        BasicDBObject innerEntityDbObj = new BasicDBObject();
                        Set<String> innerKeySet = val.keySet();
                        for (String innerKey : innerKeySet) {
                            Object obj = val.get(innerKey);
                            if (obj != null) {
                                if (innerKey.startsWith("$")) {
                                    innerEntityDbObj.append(innerKey, obj);
                                } else {
                                    innerKey = innerKey.trim().toLowerCase();
                                    innerEntityDbObj.append(innerKey, obj);
                                }
                            }
                        }
                        entityDbObj.append(key, innerEntityDbObj);
                    } else {
                        key = key.trim().toLowerCase();
                        entityDbObj.put(key, val); // 对 MongoDB BasicDBObject 对象 进行赋值
                    }
                }
            }

            WriteResult ret = dbCol.update(queryObj, entityDbObj, upsert, multi, WriteConcern.SAFE);
//            CommandResult err = ret.getLastError();
//            if (null != err && null != err.get("err")) {
//                throw err.getException();
//            } else {
                result = ret.getN();
//            }
        } catch (MongoException e) {
            throw new MongodbQueryExcepton(e.getMessage(), e);
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    public <T> T findOne(Class<T> clazz, String collectionName, DBObject query) throws DataAccessException {

        T t = null;

        try {
            //			Mongo mongo = new Mongo("58.246.182.22",27017);
            //			DB db = mongo.getDB("bookoo_sse_test");
            //			String userName = "mongosa";
            //			String passWord = "abctrans";
            //			boolean isVerify = db.authenticate(userName, passWord.toCharArray());
            //			System.out.println(isVerify);
            //			DBCollection dbCol = db.getCollection(collectionName);

            DBCollection dbCol = getDB().getCollection(collectionName);

            DBObject retObj = dbCol.findOne(query);

            if (null != retObj) {
                t = (T) toBean(clazz, retObj);
            }

            return t;

        } catch (Exception e) {
            throw new MongodbQueryExcepton(e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T findOne(Class<T> clazz, String collectionName, DBObject query, DBObject fields) throws DataAccessException {

        T t = null;

        try {
            //			Mongo mongo = new Mongo("58.246.182.22",27017);
            //			DB db = mongo.getDB("bookoo_sse_test");
            //			String userName = "mongosa";
            //			String passWord = "abctrans";
            //			boolean isVerify = db.authenticate(userName, passWord.toCharArray());
            //			System.out.println(isVerify);
            //			DBCollection dbCol = db.getCollection(collectionName);

            DBCollection dbCol = getDB().getCollection(collectionName);

            DBObject retObj = dbCol.findOne(query, fields);

            if (null != retObj) {
                t = (T) toBean(clazz, retObj);
            }

            return t;

        } catch (Exception e) {
            throw new MongodbQueryExcepton(e.getMessage(), e);
        }
    }

    public <T> Object toBean(Class<T> beanClass, DBObject dbObject) {
        Object bean = null;
        try {
            bean = newInstance(beanClass);
        } catch (Exception e) {
            throw new JSONException(e);
        }

        if (beanClass.isAssignableFrom(HashMap.class)) {
            Map properties = new HashMap();

            for (String key : dbObject.keySet()) {
                properties.put(key,  dbObject.get(key) );
            }
            bean = properties;
        } else {
            Map props = getProperties(dbObject);
            for (Iterator entries = dbObject.keySet().iterator(); entries.hasNext();) {
                String name = (String) entries.next();
                Class type = (Class) props.get(name);
                Object value = dbObject.get(name);

                String key = processPropertyName(beanClass, name);

                try {
                    PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(bean, key);
                    if (pd != null && pd.getWriteMethod() == null) {
                        // log.info( "Property '" + key + "' of "+ bean.getClass()+" has no write method. SKIPPED." );
                        continue;
                    }
                    if (pd != null) {
                        //	                  Class targetType = pd.getPropertyType();
                        if (!isNull(value)) {
                            if (value instanceof BasicDBList) {
                                setProperty(bean, key, convertPropertyValueToCollection((BasicDBList) value), true);
                            } else if (String.class.isAssignableFrom(type) || isBoolean(type) || isNumber(type) || isString(type)) {
                                if (pd != null) {
                                    //	                        	if ( !targetType.isInstance( value ) ){
                                    //	                        		System.out.println(key + "类型不符合 bean");
                                    //	                            }else{
                                    //	                            }
                                    setProperty(bean, key, value, true);
                                }
                            } else {
                                setProperty(bean, key, value, true);
                            }
                        } else {
                            setProperty(bean, key, null, true);
                        }
                    } else {
                        if (!isNull(value)) {
                            throw new NoSuchMethodException("Unknown property '" + name + "' on class '" + bean.getClass() + "'");
                        }
                    }
                } catch (Exception e) {
                    throw new MongodbQueryExcepton("Error while setting property=" + name + " type " + type, e);
                }
            }
        }
        return bean;
    }

    private List convertPropertyValueToCollection(BasicDBList basicDBList) {

        List lst = new ArrayList();

        int size = basicDBList.size();
        for (int i = 0; i < size; i++) {
            Object value = basicDBList.get(i);

            if (isNull(value)) {
                lst.add(null);
            } else {
                Class type = value.getClass();
                if (BasicDBList.class.isAssignableFrom(value.getClass())) {
                    lst.addAll(convertPropertyValueToCollection((BasicDBList) value));
                } else if (BasicDBObject.class.isAssignableFrom(type)) {
                    BasicDBObject basicDBObject = (BasicDBObject) value;
                    lst.add(basicDBObject.toMap());
                } else if (String.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type) || isNumber(type) || Character.class.isAssignableFrom(type)) {
                    lst.add(value);
                }
            }
        }

        return lst;
    }

    public String processPropertyName(Class beanClass, String name) {
        Field[] fields = beanClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equalsIgnoreCase(name)) {
                return field.getName();
            }
        }
        return name;
    }

    /**
     * Creates a Map with all the properties of the JSONObject.
     */
    public Map getProperties(DBObject dbObject) {
        Map properties = new HashMap();

        for (String key : dbObject.keySet()) {
            properties.put(key, getTypeClass(dbObject.get(key)));
        }
        return properties;
    }

    public void setProperty(Object bean, String key, Object value, boolean ignorePublicFields) throws JSONException {
        if (bean instanceof Map) {
            ((Map) bean).put(key, value);
        } else {
            if (!ignorePublicFields) {
                try {
                    Field field = bean.getClass().getField(key);
                    if (field != null)
                        field.set(bean, value);
                } catch (Exception e) {
                    _setProperty(bean, key, value);
                }
            } else {
                _setProperty(bean, key, value);
            }
        }
    }

    private void _setProperty(Object bean, String key, Object value) {
        try {
            PropertyUtils.setSimpleProperty(bean, key, value);
        } catch (Exception e) {
            throw new JSONException(e);
        }
    }

    public Object newInstance(Class target) throws InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, InvocationTargetException {
        final Object[] EMPTY_ARGS = new Object[0];
        final Class[] EMPTY_PARAM_TYPES = new Class[0];
        if (target != null) {
            Constructor c = target.getDeclaredConstructor(EMPTY_PARAM_TYPES);
            c.setAccessible(true);
            try {
                return c.newInstance(EMPTY_ARGS);
            } catch (InstantiationException e) {
                // getCause() was added on jdk 1.4
                String cause = "";
                try {
                    cause = e.getCause() != null ? "\n" + e.getCause().getMessage() : "";
                } catch (Throwable t) { /* ignore */
                }
                throw new InstantiationException("Instantiation of \"" + target + "\" failed. " + "It's probably because class is an interface, "
                        + "abstract class, array class, primitive type or void." + cause);
            }
        }
        return null;
    }

    public <T> List<T> findList(Class<T> clazz, String collectionName, DBObject query) throws DataAccessException {
        return findList(clazz, collectionName, query, null, null, 0, 0);
    }

    public <T> List<T> findList(Class<T> clazz, String collectionName, DBObject query, DBObject fields) throws DataAccessException {
        return findList(clazz, collectionName, query, fields, null, 0, 0);
    }

    public <T> List<T> findList(Class<T> clazz, String collectionName, DBObject query, DBObject fields, DBObject orderBy) throws DataAccessException {
        return findList(clazz, collectionName, query, fields, orderBy, 0, 0);
    }

    public <T> List<T> findList(Class<T> clazz, String collectionName, DBObject query, DBObject fields, DBObject orderBy, int skip, int limit) throws DataAccessException {
        List<T> retList = new ArrayList<T>();
        DBCursor cursor = null;
        try {

            //			Mongo mongo = new Mongo("61.129.33.35",37000);

            //			Mongo mongo = new Mongo("58.246.182.22",27017);
            //			DB db = mongo.getDB("bookoo_sse_test");
            //			String userName = "mongosa";
            //			String passWord = "abctrans";
            //			boolean isVerify = db.authenticate(userName, passWord.toCharArray());
            //			System.out.println(isVerify);
            //			DBCollection dbCol = db.getCollection(collectionName);

            DBCollection dbCol = getDB().getCollection(collectionName);

            if (null != fields) {
                cursor = dbCol.find(query, fields);
            } else {
                cursor = dbCol.find(query);
            }

            if (null != orderBy) {
                cursor = cursor.sort(orderBy);
            }

            if (skip > 0) {
                cursor = cursor.skip(skip);
            }

            if (limit > 0) {
                cursor = cursor.limit(limit);
            }

            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();

                //				Set<String> keySet = dbObject.keySet();

                T t;
                //				try {
                //					t = clazz.newInstance();
                //
                //					for (String key : keySet) {
                //						try {
                //							ReflectionUtil.setField(t, key, dbObject.get(key));
                //
                //						} catch (NoSuchFieldException e) {
                //						}
                //					}
                //					retList.add(t);
                //				} catch (InstantiationException e) {
                //					throw new RuntimeException(e);
                //				} catch (IllegalAccessException e) {
                //					throw new RuntimeException(e);
                //				}
                //				Map map = dbObject.toMap();
                //				
                //				JsonConfig jsonConfig = new JsonConfig();
                //			    jsonConfig.setRootClass( clazz );
                //			    jsonConfig.registerJavaPropertyNameProcessor(clazz, new MyPropertyNameProcessor());
                //				JSONObject jsonObject = JSONObject.fromObject(map);
                //				
                //				t = ((T)JSONObject.toBean(jsonObject, jsonConfig));

                t = (T) toBean(clazz, dbObject);

                retList.add(t);
            }

        } catch (Exception e) {
            throw new MongodbQueryExcepton(e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return retList;
    }

    public <T> List<T> findListByMap(Class<T> clazz, String collectionName, Map<String, Object> queryMap) throws DataAccessException {

        List<T> retList = new ArrayList<T>();

        if (collectionName == null) {
            throw new RuntimeException(" 请输入集合名称！");
        }

        DBCollection dbCol = getDB().getCollection(collectionName);

        BasicDBObject queryCondition = new BasicDBObject();

        Set<String> paramKeySet = queryMap.keySet();

        for (String paramKey : paramKeySet) {

            if (!paramKey.equals("orderBy") && !paramKey.equals("startRow") && !paramKey.equals("pageSize")) {
                String key = paramKey.trim().toLowerCase();
                queryCondition.append(key, queryMap.get(paramKey));
            }
        }

        DBCursor cursor = dbCol.find(queryCondition);

        try {
            Object orderBy = queryMap.get("orderBy");

            if (orderBy != null) {
                String strOrderBy = (String) orderBy;

                strOrderBy = strOrderBy.trim();

                String[] strOrderBys = strOrderBy.split(",");

                for (String sortKey : strOrderBys) {

                    sortKey = sortKey.trim();

                    if (!Validate.isEmpty(sortKey)) {
                        String[] orderByTmpArr = sortKey.split(" ");
                        String orderWay = "asc";
                        if (orderByTmpArr.length > 1) {
                            sortKey = orderByTmpArr[0].trim();
                            orderWay = orderByTmpArr[1].trim();
                        }
                        int iSortWay = -1;
                        if (orderWay.equalsIgnoreCase("asc")) {
                            iSortWay = 1;
                        }
                        cursor = cursor.sort(new BasicDBObject().append(sortKey, iSortWay));
                    }

                }
            }

            Object startRow = queryMap.get("startRow");

            if (startRow != null) {
                Integer iStartRow = 0;
                try {
                    iStartRow = (Integer) startRow;
                } catch (Exception e) {
                    throw new RuntimeException("startRow 转换出错，检查是否为整型", e);
                }
                if (iStartRow > 0) {
                    cursor = cursor.skip(iStartRow);
                }
            }

            Object pageSize = queryMap.get("pageSize");

            if (pageSize != null) {
                Integer iPageSize = 0;
                try {
                    iPageSize = (Integer) pageSize;
                } catch (Exception e) {
                    throw new RuntimeException("pageSize 转换出错，检查是否为整型", e);
                }
                if (iPageSize > 0) {
                    cursor = cursor.limit(iPageSize);
                }
            }

            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();

                //				Set<String> keySet = dbObject.keySet();

                T t;
                //				try {
                //					t = clazz.newInstance();
                //
                //					for (String key : keySet) {
                //						try {
                //							ReflectionUtil.setField(t, key, dbObject.get(key));
                //
                //						} catch (NoSuchFieldException e) {
                //						}
                //					}
                //					retList.add(t);
                //				} catch (InstantiationException e) {
                //					throw new RuntimeException(e);
                //				} catch (IllegalAccessException e) {
                //					throw new RuntimeException(e);
                //				}
                //				Map map = dbObject.toMap();
                //				
                //				JsonConfig jsonConfig = new JsonConfig();
                //			    jsonConfig.setRootClass( clazz );
                //			    jsonConfig.registerJavaPropertyNameProcessor(clazz, new MyPropertyNameProcessor());
                //				JSONObject jsonObject = JSONObject.fromObject(map);
                //				
                //				t = ((T)JSONObject.toBean(jsonObject, jsonConfig));

                t = (T) toBean(clazz, dbObject);

                retList.add(t);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return retList;

    }

    public long count(String collectionName, DBObject query) throws DataAccessException {
        try {

            //			Mongo mongo = new Mongo("58.246.182.22",27017);
            //			DB db = mongo.getDB("bookoo_sse_test");
            //			String userName = "mongosa";
            //			String passWord = "abctrans";
            //			boolean isVerify = db.authenticate(userName, passWord.toCharArray());
            //			System.out.println(isVerify);
            //			DBCollection dbCol = db.getCollection(collectionName);

            DBCollection dbCol = getDB().getCollection(collectionName);

            //			DBObject queryObj = new BasicDBObject();
            //			
            //			if(null != queryMap ){
            //				Set<String> keySet = queryMap.keySet();
            //				for (String key : keySet) {
            //					Object val = queryMap.get(key);
            //					if (val != null) {
            ////						key = key.trim().toLowerCase();
            //						queryObj.put(key, val); // 对 MongoDB BasicDBObject 对象 进行赋值
            //					}
            //				}
            //			}

            return dbCol.count(query);

        } catch (Exception e) {
            throw new MongodbQueryExcepton(e.getMessage(), e);
        }
    }

    /**
     * calls {@link DBCollection#group(com.mongodb.DBObject, com.mongodb.DBObject, com.mongodb.DBObject, java.lang.String, java.lang.String)} with finalize=null
     * 
     * @param key - { a : true }
     * @param cond - optional condition on query
     * @param reduce javascript reduce function
     * @param initial initial value for first match on a key
     * @return
     * @throws MongoException
     * @see <a href="http://www.mongodb.org/display/DOCS/Aggregation">http://www.mongodb.org/display/DOCS/Aggregation</a>
     */
    public DBObject group(String collectionName, DBObject key, DBObject cond, DBObject initial, String reduce) throws DataAccessException {

        try {

            DBCollection dbCol = getDB().getCollection(collectionName);

            //			BasicDBObject queryObj = new BasicDBObject();

            //			if(null != queryMap ){
            //				Set<String> keySet = queryMap.keySet();
            //				for (String key : keySet) {
            //					Object val = queryMap.get(key);
            //					if (val != null) {
            ////						key = key.trim().toLowerCase();
            //						queryObj.put(key, val); // 对 MongoDB BasicDBObject 对象 进行赋值
            //					}
            //				}
            //			}

            return dbCol.group(key, cond, initial, reduce, null);

        } catch (Exception e) {
            throw new MongodbQueryExcepton(e.getMessage(), e);
        }

    }

    public List<?> distinct(String collectionName, String key, DBObject query) throws DataAccessException {
        try {

            DBCollection dbCol = getDB().getCollection(collectionName);

            return dbCol.distinct(key, query);

        } catch (Exception e) {
            throw new MongodbQueryExcepton(e.getMessage(), e);
        }
    }

    public int remove(String collectionName, DBObject query) throws DataAccessException {
        int result = 0;

        try {
            // Mongo mongo = new Mongo("58.246.182.22",27017);
            // DB db = mongo.getDB("bookoo_sse_test");
            // DBCollection dbCol = db.getCollection(collectionName);

            DBCollection dbCol = getDB().getCollection(collectionName);

            WriteResult ret = dbCol.remove(query, WriteConcern.SAFE);
            // WriteResult ret = dbCol.insert(entityDbObj);
//            CommandResult err = ret.getLastError();
//            if (null != err && null != err.get("err")) {
//                throw err.getException();
//            } else {
                result = ret.getN();
//            }
        } catch (MongoException me) {
//            if (me instanceof MongoException.DuplicateKey) {
//                throw new MongodbQueryExcepton("DuplicateKey", me);
//            }
            throw new MongodbQueryExcepton(me.getMessage(), me);
        } catch (Exception e) {
            throw new MongodbQueryExcepton(e.getMessage(), e);
        }

        return result;
    }

    public class MyPropertyNameProcessor implements PropertyNameProcessor {

        public String processPropertyName(Class beanClass, String name) {
            Field[] fields = beanClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equalsIgnoreCase(name)) {
                    return field.getName();
                }
            }
            return name;
        }

    }

    /**
     * Returns the JSON type.<br>
     * Values are Object, String, Boolean, Number(subclasses) &amp; JSONFunction.
     */
    public Class getTypeClass(Object obj) {
        if (isNull(obj)) {
            return Object.class;
        } else if (isArray(obj)) {
            return List.class;
        } else if (isBoolean(obj)) {
            return Boolean.class;
        } else if (isNumber(obj)) {
            Number n = (Number) obj;
            if (isInteger(n)) {
                return Integer.class;
            } else if (isLong(n)) {
                return Long.class;
            } else if (isFloat(n)) {
                return Float.class;
            } else if (isBigInteger(n)) {
                return BigInteger.class;
            } else if (isBigDecimal(n)) {
                return BigDecimal.class;
            } else if (isDouble(n)) {
                return Double.class;
            } else {
                throw new JSONException("Unsupported type");
            }
        } else if (isString(obj)) {
            return String.class;
        } else if (isObject(obj)) {
            return Object.class;
        } else {
            throw new JSONException("Unsupported type");
        }
    }

    /**
     * Tests if a Class represents an array or Collection.
     */
    public static boolean isArray(Class clazz) {
        return clazz != null && (clazz.isArray() || Collection.class.isAssignableFrom(clazz) || (JSONArray.class.isAssignableFrom(clazz)));
    }

    /**
     * Tests if obj is an array or Collection.
     */
    public static boolean isArray(Object obj) {
        if ((obj != null && obj.getClass().isArray()) || (obj instanceof Collection) || (obj instanceof JSONArray)) {
            return true;
        }
        return false;
    }

    /**
     * Tests if Class represents a Boolean or primitive boolean
     */
    public static boolean isBoolean(Class clazz) {
        return clazz != null && (Boolean.TYPE.isAssignableFrom(clazz) || Boolean.class.isAssignableFrom(clazz));
    }

    /**
     * Tests if obj is a Boolean or primitive boolean
     */
    public static boolean isBoolean(Object obj) {
        if ((obj instanceof Boolean) || (obj != null && obj.getClass() == Boolean.TYPE)) {
            return true;
        }
        return false;
    }

    /**
     * Tests if Class represents a primitive number or wrapper.<br>
     */
    public static boolean isNumber(Class clazz) {
        return clazz != null
                && (Byte.TYPE.isAssignableFrom(clazz) || Short.TYPE.isAssignableFrom(clazz) || Integer.TYPE.isAssignableFrom(clazz) || Long.TYPE.isAssignableFrom(clazz)
                        || Float.TYPE.isAssignableFrom(clazz) || Double.TYPE.isAssignableFrom(clazz) || Number.class.isAssignableFrom(clazz));
    }

    /**
     * Tests if obj is a primitive number or wrapper.<br>
     */
    public static boolean isNumber(Object obj) {
        if ((obj != null && obj.getClass() == Byte.TYPE) || (obj != null && obj.getClass() == Short.TYPE) || (obj != null && obj.getClass() == Integer.TYPE)
                || (obj != null && obj.getClass() == Long.TYPE) || (obj != null && obj.getClass() == Float.TYPE) || (obj != null && obj.getClass() == Double.TYPE)) {
            return true;
        }

        return obj instanceof Number;
    }

    /**
     * Finds out if n represents an Integer.
     * 
     * @return true if n is instanceOf Integer or the literal value can be evaluated as an Integer.
     */
    private static boolean isInteger(Number n) {
        if (n instanceof Integer) {
            return true;
        }
        try {
            Integer.parseInt(String.valueOf(n));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Finds out if n represents a Long.
     * 
     * @return true if n is instanceOf Long or the literal value can be evaluated as a Long.
     */
    private static boolean isLong(Number n) {
        if (n instanceof Long) {
            return true;
        }
        try {
            Long.parseLong(String.valueOf(n));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Finds out if n represents a Float.
     * 
     * @return true if n is instanceOf Float or the literal value can be evaluated as a Float.
     */
    private static boolean isFloat(Number n) {
        if (n instanceof Float) {
            return true;
        }
        try {
            float f = Float.parseFloat(String.valueOf(n));
            return !Float.isInfinite(f);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Tests if Class represents a primitive double or wrapper.<br>
     */
    public static boolean isDouble(Class clazz) {
        return clazz != null && (Double.TYPE.isAssignableFrom(clazz) || Double.class.isAssignableFrom(clazz));
    }

    /**
     * Finds out if n represents a Double.
     * 
     * @return true if n is instanceOf Double or the literal value can be evaluated as a Double.
     */
    private static boolean isDouble(Number n) {
        if (n instanceof Double) {
            return true;
        }
        try {
            double d = Double.parseDouble(String.valueOf(n));
            return !Double.isInfinite(d);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Finds out if n represents a BigInteger
     * 
     * @return true if n is instanceOf BigInteger or the literal value can be evaluated as a BigInteger
     */
    private static boolean isBigInteger(Number n) {
        if (n instanceof BigInteger) {
            return true;
        }
        try {
            new BigInteger(String.valueOf(n));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Finds out if n represents a BigInteger
     * 
     * @return true if n is instanceOf BigInteger or the literal value can be evaluated as a BigInteger
     */
    private static boolean isBigDecimal(Number n) {
        if (n instanceof BigDecimal) {
            return true;
        }
        try {
            new BigDecimal(String.valueOf(n));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Tests if Class represents a String or a char
     */
    public static boolean isString(Class clazz) {
        return clazz != null && (String.class.isAssignableFrom(clazz) || (Character.TYPE.isAssignableFrom(clazz) || Character.class.isAssignableFrom(clazz)));
    }

    /**
     * Tests if obj is a String or a char
     */
    public static boolean isString(Object obj) {
        if ((obj instanceof String) || (obj instanceof Character) || (obj != null && (obj.getClass() == Character.TYPE || String.class.isAssignableFrom(obj.getClass())))) {
            return true;
        }
        return false;
    }

    /**
     * Tests if obj is not a boolean, number, string or array.
     */
    public boolean isObject(Object obj) {
        return !isNumber(obj) && !isString(obj) && !isBoolean(obj) && !isArray(obj) && !isNull(obj);
    }

    /**
     * Tests if the obj is a javaScript null.
     */
    public boolean isNull(Object obj) {
        return null == obj || this == obj || "null".equals(obj);
    }

    public static void main(String[] args) {

        try {
            //			testInsert();

            //			testFindOne();

            //			testCount();

            //			long time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2012-08-15 00:00:00").getTime();
            //			System.out.println(time);

            //			testDistinct();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testDistinct() {
        try {

            String colName = "news_temp";

            Mongo mongo = new Mongo("58.246.182.22", 27017);
            DB db = mongo.getDB("bookoo_sse_test");
            DBCollection dbCol = db.getCollection(colName);

            String key = "source";

            BasicDBObject query = new BasicDBObject();
            query.put("rptappid", 831);

            List retList = dbCol.distinct(key, query);
            for (Object obj : retList) {
                System.out.println(obj.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    	private static void testCount() {
    		try{
    			MongodbTemplate mongodbTemplate = new MongodbTemplate();
    			
    			Date weiboTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2012-08-01 00:00:00");
    			
    			BasicDBObject query = new BasicDBObject();
    //			query.append("keywordflag", 0);
    			
    			//{ "content" : { "$regex" : "(帮宝适)?" , "$options" : "i"}}
    			Pattern pattern = Pattern.compile("((pampers|帮宝适))+",Pattern.CASE_INSENSITIVE);
    			query.put("content", pattern);
    			long totalCount = mongodbTemplate.count("weibo", query);
    			System.out.println(totalCount);
    			
    			query.clear();
    			//(帮宝适 OR Pampers) NOT (优惠OR 促销OR 包邮OR 全场 OR特价)
    			
    			//new BasicDBObject().append("$regex", "("+columnValue+")?").append("$options", "i")
    			//{ "content" : { "$regex" : "(帮宝适)?" , "$options" : "i"}}
    			query.put("content", new BasicDBObject().append("$regex", "^(?:(?!促销|优惠|包邮|全场|特价).)+?(pampers|帮宝适)(?:(?!促销|优惠|包邮|全场|特价).)+?$").append("$options", "i"));
    //			query.put("content", new BasicDBObject().append("$regex", "^(?:(促销).)+?$").append("$options", "i"));
    			//{ "content" : { "$regex" : "(帮宝适)?" , "$options" : "i"}}
    			totalCount = mongodbTemplate.count("weibo", query);
    			
    			BasicDBObject fields = new BasicDBObject();
    			fields.put("content", 1);
    			fields.put("_id", 0);
    			List<WeiboBO> weiboBOList = mongodbTemplate.findList(WeiboBO.class, "weibo", query,fields);
    			if(null != weiboBOList && weiboBOList.size() > 0){
    				for(WeiboBO weibo : weiboBOList){
    					System.out.println(weibo.getContent());
    				}
    			}
    			
    //			query.append("weibotime", new BasicDBObject().append("$gt", weiboTime));
    			
    			System.out.println(totalCount);
    			
    			
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    	}*/

    private static void testInsert() {
        //		MongodbTemplate mongodbTemplate = new MongodbTemplate();
        //		Map<String,Object> weiboMap = new HashMap<String,Object>();
        //		weiboMap.put("createTime",new Date()); 
        //		weiboMap.put("weiboApiId",3474351414237703L); 
        //		weiboMap.put("forwardTimes",0L); 
        //		weiboMap.put("replyTimes",0L); 
        //		weiboMap.put("SrcForwardTimes",0L); 
        //		weiboMap.put("srcReplyTimes",0L); 
        //		weiboMap.put("weiboUrl","http://weibo.com/2507356197/yvcdDhMq3"); 
        //		
        //		List<String> crawlKeywords = new ArrayList<String>();
        //		crawlKeywords.add("pampers");
        //		
        //		weiboMap.put("crawlkeywords",crawlKeywords); 
        //		weiboMap.put("srcAccount",""); 
        //		weiboMap.put("totalScore",0); 
        //		weiboMap.put("negativeScore",0); 
        //		weiboMap.put("site",1); 
        //		
        //		List<Map> keywordList = new ArrayList<Map>();
        //		weiboMap.put("keywordList",keywordList); 
        //		
        //		weiboMap.put("website",1); 
        //		weiboMap.put("keywordFlag",0); 
        //		weiboMap.put("srcId",""); 
        //		weiboMap.put("imgShow","http://ww2.sinaimg.cn/thumbnail/95733825tw1dvhkuspqg0j.jpg"); 
        //		weiboMap.put("weiboFrom","新浪微博"); 
        //		weiboMap.put("content","Pampers Baby 帮宝适 婴儿纸尿裤 3号 222片装 $35.66!http://t.cn/zWNa5vE"); 
        //		weiboMap.put("source",""); 
        //		weiboMap.put("srcApiId",0L); 
        //		weiboMap.put("hasForward",0); 
        //		weiboMap.put("weiboTime",new Date()); 
        //		weiboMap.put("account","2507356197"); 
        //		weiboMap.put("weiboId","yvcdDhMq3"); 
        //		weiboMap.put("lastModifyTime",new Date());
        //		mongodbTemplate.insert(WeiboBO.class, "weibo", weiboMap);

        //		Map<String,Object> userMap = new HashMap<String,Object>();
        //		userMap.put("tags","");
        //		userMap.put("icon30","http://tp2.sinaimg.cn/1541603965/50/1286543156/1"); 
        //		userMap.put("trueName","vinW"); 
        //		userMap.put("hint","英国利兹大学计算机及语言学研究员，搜索项目博士后研究员。"); 
        //		userMap.put("weiboUid",1541603965L); 
        //		userMap.put("userFlag",1); 
        //		userMap.put("intro","做过不少微博APP,网站版的手机版的都有,欢迎咨询探讨任何微博APP问题"); 
        //		userMap.put("blogUrl","http://t.468a.com"); 
        //		userMap.put("city","深圳"); 
        //		userMap.put("attentions",2000L); 
        //		userMap.put("followers",8489L); 
        //		userMap.put("mblogs",12355L); 
        //		userMap.put("nickName","vinW"); 
        //		userMap.put("indexFlag",2); 
        //		userMap.put("province","广东"); 
        //		userMap.put("md5","68a63b316cae82560aa1886a29973878"); 
        //		userMap.put("gender","男"); 
        //		userMap.put("tableSuffix",125L); 
        ////		userMap.put("statisticsFlag",0); 
        //		userMap.put("vipType",1);
        //		mongodbTemplate.insert(TUserBO.class, "user", userMap);

    }

    /*private static void testFindOne() {
    	
    	MongodbTemplate mongodbTemplate = new MongodbTemplate();
    	
    	Map<String,Object> q = new HashMap<String,Object>();
    	q.put("weiboapiid", 3474351414237703L);
    	WeiboBO weiboBO = (WeiboBO)mongodbTemplate.findOne(WeiboBO.class, "weibo", q);
    	
    	if(null != weiboBO){
    		System.out.println(weiboBO);
    	}
    }*/

}

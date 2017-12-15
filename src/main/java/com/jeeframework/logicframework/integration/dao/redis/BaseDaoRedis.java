/*
 * @project: com.jeeframework 
 * @package: com.jeeframework.logicframework.integration.dao.redis
 * @title:   BaseDaoRedis.java 
 *
 * Copyright (c) 2017 jeeframework Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.logicframework.integration.dao.redis;

import com.jeeframework.core.exception.BaseException;
import com.jeeframework.logicframework.beans.ContextManageBean;
import com.jeeframework.util.validate.Validate;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.DisposableBean;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * redis 访问对象
 *
 * @author lance
 * @version 1.0 2017-12-11 16:20
 */
public class BaseDaoRedis implements ContextManageBean, DisposableBean {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected BeanFactory context;
    protected String beanName = null;

    private static final String DEFAULT_REDIS_SEPARATOR = ";";

    private static final String HOST_PORT_SEPARATOR = ":";

    private static final String NAMESPACE_SEPARATOR = "_";

    private String timeout;//操作超时时间,默认2秒
    private String maxTotal;//jedis池最大连接数总数，默认8
    private String maxIdle;//jedis池最大空闲连接数，默认8
    private String minIdle;//jedis池最少空闲连接数
    private String maxWaitMillis;//jedis池没有对象返回时，最大等待时间单位为毫秒
    private String testOnBorrow;//在borrow一个jedis实例时，是否提前进行validate操作
    private String poolType = "shared";//连接池的类型， 默认为 shared， 可以设置为 hash
    private String auth; //redis授权密码
    private String namespace;//用于多个业务系统在一个redis里使用的情况，区分不同的业务

    private String redisUrls = "127.0.0.1:6379";

    private static final String POOL_TYPE_SHARED = "shared";
    private static final String POOL_TYPE_HASH = "hash";

    private JedisPool[] jedisPools = new JedisPool[0];
    private ShardedJedisPool shardedJedisPool = null;


    private void initialShardedPool() {
        // 操作超时时间,默认2秒
        int timeout = NumberUtils.toInt(this.timeout, 2000);
        // jedis池最大连接数总数，默认8
        int maxTotal = NumberUtils.toInt(this.maxTotal, 8);
        // jedis池最大空闲连接数，默认8
        int maxIdle = NumberUtils.toInt(this.maxIdle, 8);
        // jedis池最少空闲连接数
        int minIdle = NumberUtils.toInt(this.minIdle, 0);
        // jedis池没有对象返回时，最大等待时间单位为毫秒
        long maxWaitMillis = NumberUtils.toLong(this.maxWaitMillis, -1);
        // 在borrow一个jedis实例时，是否提前进行validate操作
        boolean testOnBorrow = Boolean.parseBoolean(this.testOnBorrow);

        // 设置jedis连接池配置
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxWaitMillis(maxWaitMillis);
        poolConfig.setTestOnBorrow(testOnBorrow);

        // 取得redis的url
        String redisUrls = this.redisUrls;
        if (redisUrls == null || redisUrls.trim().isEmpty()) {
            throw new IllegalStateException("the urls of redis is not configured");
        }
        logger.info("the urls of redis is {}", redisUrls);

        if (POOL_TYPE_HASH.equals(poolType)) {
            // 生成连接池
            List<JedisPool> jedisPoolList = new ArrayList<JedisPool>();
            for (String redisUrl : redisUrls.split(DEFAULT_REDIS_SEPARATOR)) {
                String[] redisUrlInfo = redisUrl.split(HOST_PORT_SEPARATOR);

                JedisPool jedisPool = null;
                if (!Validate.isEmpty(auth)) {
                    jedisPool = new JedisPool(poolConfig, redisUrlInfo[0], Integer.parseInt(redisUrlInfo[1]),
                            timeout, auth);
                } else {
                    jedisPool = new JedisPool(poolConfig, redisUrlInfo[0], Integer.parseInt(redisUrlInfo[1]),
                            timeout);
                }
                jedisPoolList.add(jedisPool);

            }
            jedisPools = jedisPoolList.toArray(jedisPools);
        } else {

            List<JedisShardInfo> shardedPoolList = new ArrayList<JedisShardInfo>();
            for (String redisUrl : redisUrls.split(DEFAULT_REDIS_SEPARATOR)) {
                String[] redisUrlInfo = redisUrl.split(HOST_PORT_SEPARATOR);
                JedisShardInfo jedisinfo = new JedisShardInfo(redisUrlInfo[0], Integer.parseInt(redisUrlInfo[1]),
                        timeout);
                if (!Validate.isEmpty(auth)) {
                    jedisinfo.setPassword(auth);
                }
                shardedPoolList.add(jedisinfo);
            }
            // 构造池
            this.shardedJedisPool = new ShardedJedisPool(poolConfig, shardedPoolList, Hashing.MURMUR_HASH);
        }

    }

    public JedisCommands getJedisClient(String poolType, String key) {
        JedisCommands jedis = null;

        if (POOL_TYPE_HASH.equals(poolType)) {
            jedis = jedisPools[(0x7FFFFFFF & key.hashCode()) % jedisPools.length].getResource();
        } else {
            jedis = this.shardedJedisPool.getResource();
        }

        return jedis;
    }

    /**
     * 实现jedis连接的获取和释放，具体的redis业务逻辑由executor实现
     *
     * @param executor RedisExecutor接口的实现类
     * @return
     */
    private <T> T execute(String key, RedisExecutor<T> executor) {
        JedisCommands jedis = this.getJedisClient(poolType, key);
        T result = null;
        try {
            result = executor.execute(jedis);
        } finally {
            if (jedis != null) {
                try {
                    ((Closeable) jedis).close();
                } catch (IOException e) {
                    throw new BaseException("关闭redis 链接出错 e =  " + e, e);
                }
            }
        }
        return result;
    }

    private String wrapNamespace(String key) {
        String retKey = key;
        if (!Validate.isEmpty(this.namespace)) {
            retKey = this.namespace + NAMESPACE_SEPARATOR + key;
        }

        return retKey;
    }

    public String set(final String key, final String value) {
        return execute(key, new RedisExecutor<String>() {
            @Override
            public String execute(JedisCommands jedis) {
                return jedis.set(wrapNamespace(key), value);
            }
        });
    }

    public String set(final String key, final String value, final String nxxx, final String expx, final long time) {
        return execute(key, new RedisExecutor<String>() {
            @Override
            public String execute(JedisCommands jedis) {
                return jedis.set(wrapNamespace(key), value, nxxx, expx, time);
            }
        });
    }

    public String get(final String key) {
        return execute(key, new RedisExecutor<String>() {
            @Override
            public String execute(JedisCommands jedis) {
                return jedis.get(wrapNamespace(key));
            }
        });
    }

    public Boolean exists(final String key) {
        return execute(key, new RedisExecutor<Boolean>() {
            @Override
            public Boolean execute(JedisCommands jedis) {
                return jedis.exists(wrapNamespace(key));
            }
        });
    }

    public Long setnx(final String key, final String value) {
        return execute(key, new RedisExecutor<Long>() {
            @Override
            public Long execute(JedisCommands jedis) {
                return jedis.setnx(wrapNamespace(key), value);
            }
        });
    }

    public String setex(final String key, final int seconds, final String value) {
        return execute(key, new RedisExecutor<String>() {
            @Override
            public String execute(JedisCommands jedis) {
                return jedis.setex(wrapNamespace(key), seconds, value);
            }
        });
    }

    public Long expire(final String key, final int seconds) {
        return execute(key, new RedisExecutor<Long>() {
            @Override
            public Long execute(JedisCommands jedis) {
                return jedis.expire(wrapNamespace(key), seconds);
            }
        });
    }

    public Long incr(final String key) {
        return execute(key, new RedisExecutor<Long>() {
            @Override
            public Long execute(JedisCommands jedis) {
                return jedis.incr(wrapNamespace(key));
            }
        });
    }

    public Long decr(final String key) {
        return execute(key, new RedisExecutor<Long>() {
            @Override
            public Long execute(JedisCommands jedis) {
                return jedis.decr(wrapNamespace(key));
            }
        });
    }

    public Long hset(final String key, final String field, final String value) {
        return execute(key, new RedisExecutor<Long>() {
            @Override
            public Long execute(JedisCommands jedis) {
                return jedis.hset(wrapNamespace(key), field, value);
            }
        });
    }

    public String hget(final String key, final String field) {
        return execute(key, new RedisExecutor<String>() {
            @Override
            public String execute(JedisCommands jedis) {
                return jedis.hget(wrapNamespace(key), field);
            }
        });
    }

    public String hmset(final String key, final Map<String, String> hash) {
        return execute(key, new RedisExecutor<String>() {
            @Override
            public String execute(JedisCommands jedis) {
                return jedis.hmset(wrapNamespace(key), hash);
            }
        });
    }

    public List<String> hmget(final String key, final String... fields) {
        return execute(key, new RedisExecutor<List<String>>() {
            @Override
            public List<String> execute(JedisCommands jedis) {
                return jedis.hmget(wrapNamespace(key), fields);
            }
        });
    }

    public Long del(final String key) {
        return execute(key, new RedisExecutor<Long>() {
            @Override
            public Long execute(JedisCommands jedis) {
                return jedis.del(wrapNamespace(key));
            }
        });
    }

    public Map<String, String> hgetAll(final String key) {
        return execute(key, new RedisExecutor<Map<String, String>>() {
            @Override
            public Map<String, String> execute(JedisCommands jedis) {
                return jedis.hgetAll(wrapNamespace(key));
            }
        });
    }

    @Override
    public void destroy() {
        if (POOL_TYPE_HASH.equals(poolType)) {
            for (int i = 0; i < jedisPools.length; i++) {
                jedisPools[i].close();
            }
        } else {
            this.shardedJedisPool.close();
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.context = beanFactory;
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = beanName;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initialShardedPool();
    }

    public void setContext(BeanFactory context) {
        this.context = context;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public void setMaxTotal(String maxTotal) {
        this.maxTotal = maxTotal;
    }

    public void setMaxIdle(String maxIdle) {
        this.maxIdle = maxIdle;
    }

    public void setMinIdle(String minIdle) {
        this.minIdle = minIdle;
    }

    public void setMaxWaitMillis(String maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    public void setTestOnBorrow(String testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public void setPoolType(String poolType) {

        if (!(POOL_TYPE_HASH.equals(poolType) || POOL_TYPE_SHARED.equals(poolType))) {
            throw new BaseException("poolType只能是 " + POOL_TYPE_HASH + "  或者  " + POOL_TYPE_SHARED);
        }
        this.poolType = poolType;
    }

    public void setRedisUrls(String redisUrls) {
        this.redisUrls = redisUrls;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    // redis具体逻辑接口
    interface RedisExecutor<T> {
        T execute(JedisCommands jedis);
    }
}


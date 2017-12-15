package com.jeeframework.logicframework.integration.dao.redis;

import com.jeeframework.testframework.AbstractSpringBaseTestNoTransaction;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * 描述
 *
 * @author lance
 * @version 1.0 2017-12-11 18:33
 */
public class BaseDaoRedisTest extends AbstractSpringBaseTestNoTransaction {

    @Resource
    private BaseDaoRedis redisClient;

    @Test
    public void set() throws Exception {
        redisClient.set("111", "1111");
    }

}
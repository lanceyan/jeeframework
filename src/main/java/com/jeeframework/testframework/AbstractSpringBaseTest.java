/*
 * @project: spore_appserver 
 * @package: com.spore.util.test
 * @title:   AbstractSpringBaseTest.java 
 *
 * Copyright (c) 2016 jeeframework Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.testframework;

import com.jeeframework.logicframework.util.server.JeeFrameWorkServer;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Spring 组件测试类
 *
 * @author lance
 * @version 1.0 2016-03-28 22:44
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextHierarchy({
        @ContextConfiguration(locations = {"classpath:config/biz-context-core.xml"}),
        @ContextConfiguration(locations = {"classpath:config/conf-spring/biz-context-*.xml"})
})
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public abstract class AbstractSpringBaseTest extends AbstractTransactionalJUnit4SpringContextTests {


    @BeforeClass
    public static void beforeSetUp() {
        System.out.println("Test beforeSetUp...");

        setTestEnv();

    }

    public static void setTestEnv() {
        JeeFrameWorkServer.initEnvVariables();
    }

    @Before
    public void setUp() {
        System.out.println("Test start...");
    }


    @AfterClass
    public static void tearUp() {
        System.out.println("Test end!");
    }

    @Resource(name = "dsFactory")
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
}

/*
 * @project: jeeframework 
 * @package: com.jeeframework.log4j2
 * @title:   App.java 
 *
 * Copyright (c) 2015 Hyfay Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.log4j2.slf4jtest;

/**
 * 描述
 *
 * @author lance
 * @version 1.0 2015-06-11 10:19
 */

import com.jeeframework.util.resource.ResourceUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
/**
 * Hello world!
 */
public class Sl4jLog4j2 {
    //    static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);


    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Sl4jLog4j2.class);

    public static void main(String[] args) throws FileNotFoundException {

        File log4j2xml = ResourceUtils.getFile("classpath:com/jeeframework/log4j2/slf4jtest/log4j2.xml");

        System.setProperty("log4j.configurationFile", log4j2xml.getAbsolutePath());
        //重新初始化Log4j2的配置上下文
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        context.reconfigure();

        logger.trace("trace message");
        logger.debug("debug message");
        logger.info("info message");
        logger.warn("warn message");
        logger.error("error message");
        System.out.println("Hello World!");
    }
}
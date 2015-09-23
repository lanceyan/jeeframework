/*
 * @project: jeeframework 
 * @package: com.jeeframework.log4j2
 * @title:   App.java 
 *
 * Copyright (c) 2015 Hyfay Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.log4j2.basetest;

/**
 * 描述
 *
 * @author lance
 * @version 1.0 2015-06-11 10:19
 */

import com.jeeframework.util.resource.ResourceUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Hello world!
 */
public class BaseLog4j2 {
    //    static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
    static Logger logger = LogManager.getLogger(BaseLog4j2.class);

    public static void main(String[] args) throws FileNotFoundException {

        File log4j2xml = ResourceUtils.getFile("classpath:com/jeeframework/log4j2/basetest/log4j2.xml");

        System.setProperty("log4j.configurationFile", log4j2xml.getAbsolutePath());
        //重新初始化Log4j2的配置上下文
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        context.reconfigure();

        logger.trace("trace message");
        logger.debug("debug message");
        logger.info("info message");
        logger.warn("warn message");
        logger.error("error message");
        logger.fatal("fatal message");
        System.out.println("Hello World!");
    }
}
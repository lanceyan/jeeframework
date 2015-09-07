/*
 * @project: jeeframework 
 * @package: com.jeeframework.log4j2
 * @title:   App.java 
 *
 * Copyright (c) 2015 Hyfay Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.log4j2.routetest;

/**
 * 描述
 *
 * @author lance
 * @version 1.0 2015-06-11 10:19
 */

import com.jeeframework.util.resource.ResourceUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LoggerContext;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Hello world!
 */
public class RouteLog4j2 {
    //    static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    private static String countryCode = "3333";
    private static String interfaceFile = "2222";

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(RouteLog4j2.class);

    public static void main(String[] args) throws FileNotFoundException {

        File log4j2xml = ResourceUtils.getFile("classpath:com/jeeframework/log4j2/routetest/log4j2.xml");

        System.setProperty("log4j.configurationFile", log4j2xml.getAbsolutePath());
        //重新初始化Log4j2的配置上下文
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        context.reconfigure();

        ThreadContext.put("countryCode", countryCode);
//        ThreadContext.put("interfaceFile", interfaceFile);
        log.debug("Parsing file " + interfaceFile + " from " + countryCode);
        log.debug("Uploading data from " + interfaceFile + " from " + countryCode);
        log.debug("Generating invoices from " + interfaceFile + " from " + countryCode);
        log.debug("Processing completed **** " + interfaceFile + " from " + countryCode);
        ThreadContext.remove("countryCode");
//        ThreadContext.remove("interfaceFile");
    }
}
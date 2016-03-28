/*
 * @project: jeeframework 
 * @package: com.jeeframework.log4j2
 * @title:   App.java 
 *
 * Copyright (c) 2015 Hyfay Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.log4j2.routelogutil;

/**
 * 描述
 *
 * @author lance
 * @version 1.0 2015-06-11 10:19
 */

import com.jeeframework.logicframework.util.logging.LoggerUtil;
import com.jeeframework.util.resource.ResourceUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ResourceBundle;

/**
 * Hello world!
 */
public class RouteLogUtilLog4j2 {
    //    static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    private static String countryCode = "3333";
    private static String interfaceFile = "2222";

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(RouteLogUtilLog4j2.class);

    public static void main(String[] args) throws FileNotFoundException {

        File log4j2xml = ResourceUtils.getFile("classpath:com/jeeframework/log4j2/routelogutil/log4j2.xml");

        System.setProperty("log4j.configurationFile", log4j2xml.getAbsolutePath());
        //重新初始化Log4j2的配置上下文
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        context.reconfigure();

        String sss = ResourceBundle.getBundle("with-local").getString("root.log.level");

        LoggerUtil.debugTrace("aaa", "hhhhhhhh");
        LoggerUtil.infoTrace("aaa", "hhhhhhhh");
        LoggerUtil.errorTrace("aaa", "hhhhhhhh");
        LoggerUtil.errorTrace("aaa", "hhhhhhhh", new RuntimeException());



        LoggerUtil.debugTrace( "----------------------");
        LoggerUtil.infoTrace( "----------------------");
        LoggerUtil.errorTrace( "----------------------");
        LoggerUtil.errorTrace( "----------------------", new RuntimeException());

    }
}
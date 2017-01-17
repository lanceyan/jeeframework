/**
 * Copyright (C) 1998-2007 JEEFRAMEWORK Inc.All Rights Reserved.
 * <p/>
 * FileName��LoggerUtil.java
 * <p/>
 * Description����Ҫ�������ļ�������
 * History��
 * �汾��    ����      ����       ��Ҫ������ز���
 * 1.0   lanceyan  2008-5-23  Create
 */

package com.jeeframework.logicframework.util.logging;

import com.jeeframework.util.resource.ResourceUtils;
import com.jeeframework.util.validate.Validate;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.log4j.util.ReflectionUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.management.ManagementFactory;

//import org.apache.log4j.spi.ThrowableInformation;

/**
 * 调用日志静态方法
 */
public class LoggerUtil {

    final static String FQCN = LoggerUtil.class.getName();

    private static boolean allErrorInFileSwitch = true;//所有错误信息记录到一个日志文件

    static {
        String allErrorInFileTmp = System.getProperty("log.allErrorInFile");
        allErrorInFileSwitch = (allErrorInFileTmp != null) && allErrorInFileTmp.equalsIgnoreCase("true");

        String confEnv = System.getProperty("conf.env");

        String log4j2File = "";
        if (!Validate.isEmpty(confEnv)) {
            log4j2File = "log4j2-" + confEnv + ".xml";
        }

        if (!Validate.isEmpty(log4j2File)) {
            File log4j2xml = null;
            try {
                log4j2xml = ResourceUtils.getFile("classpath:" + log4j2File);
            } catch (FileNotFoundException e) {
                System.out.println("根据 -Dconf.env  没有找到log4j2的配置文件，" + log4j2File + "，使用log4j2默认加载方式");
            }

            if (log4j2xml != null) {
                System.out.println("根据 -Dconf.env  找到log4j2的配置文件，" + log4j2xml.getAbsolutePath());
                System.setProperty("log4j.configurationFile", "file:///" +log4j2xml.getAbsolutePath());
                //重新初始化Log4j2的配置上下文
                LoggerContext context = (LoggerContext) LogManager.getContext(false);
                context.reconfigure();
            }

        }
    }

    private static String serverPid = null; // 服务器的进程ID

    public static com.jeeframework.core.logging.Logger getLogger() {
        com.jeeframework.core.logging.Logger sdkLogger = null;
        return sdkLogger;
    }

    /**
     * 返回系统进程号
     *
     * @return
     */
    public static String getPid() {

        //为空才会根据系统获取进程PID
        if (Validate.isEmpty(serverPid)) {
            serverPid = ManagementFactory.getRuntimeMXBean().getName();
            serverPid = serverPid.substring(0, serverPid.indexOf('@'));
        }
        return serverPid;
    }

    /**
     * 描述： 将调试信息记录到调试级别日志
     *
     * @param message 调试级别日志要记录的信息
     */
    public static void debugTrace(String message) {
        ExtendedLogger logger = (ExtendedLogger) LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
        logger.logIfEnabled(FQCN, Level.DEBUG, null, message, (Throwable) null);
    }

    /**
     * 描述： 将运营信息记录到调试级别日志
     *
     * @param message 调试级别日志要记录的信息
     */
    public static void infoTrace(String message) {
        ExtendedLogger logger = (ExtendedLogger) LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
        logger.logIfEnabled(FQCN, Level.INFO, null, message, (Throwable) null);
    }

    /**
     * 描述： 将异常错误信息记录到异常日志
     *
     * @param message 错误信息
     */
    public static void errorTrace(String message) {
        ExtendedLogger logger = (ExtendedLogger) LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
        logger.logIfEnabled(FQCN, Level.ERROR, null, message, (Throwable) null);
    }

    /**
     * 异常日志
     *
     * @param message 消息
     * @param t       异常
     */
    public static void errorTrace(String message, Throwable t) {
        ExtendedLogger logger = (ExtendedLogger) LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
        logger.logIfEnabled(FQCN, Level.ERROR, null, message, (Throwable) t);
    }


    /**
     * 将调试信息记录到调试级别日志(主要由平台内部组件调用)
     *
     * @param busiModule 模块名
     * @param message    日志消息
     */
    public static void debugTrace(String busiModule, String message) {
        try {

            ThreadContext.put("busiModule", busiModule);
            Class clazz = ReflectionUtil.getCallerClass(2);
            ExtendedLogger logger = (ExtendedLogger) LogManager.getLogger(clazz);
            logger.logIfEnabled(FQCN, Level.DEBUG, null, message, (Throwable) null);
        } finally {
            ThreadContext.remove("busiModule");
        }
    }


    /**
     * 将调试信息记录到INFO级别日志(主要由平台内部组件调用)
     *
     * @param busiModule 模块名
     * @param message    日志消息
     */
    public static void infoTrace(String busiModule, String message) {
        try {
            ThreadContext.put("busiModule", busiModule);
            Class clazz = ReflectionUtil.getCallerClass(2);
            ExtendedLogger logger = (ExtendedLogger) LogManager.getLogger(clazz);
            logger.logIfEnabled(FQCN, Level.INFO, null, message, (Throwable) null);
        } finally {
            ThreadContext.remove("busiModule");
        }
    }


    /**
     * 描述： 将错误信息记录到错误级别日志(主要由平台内部组件调用)
     *
     * @param busiModule 模块名
     * @param message    日志消息
     */
    public static void errorTrace(String busiModule, String message) {
        try {
            ThreadContext.put("busiModule", busiModule);
            Class clazz = ReflectionUtil.getCallerClass(2);
            ExtendedLogger logger = (ExtendedLogger) LogManager.getLogger(clazz);
            logger.logIfEnabled(FQCN, Level.ERROR, null, message, (Throwable) null);
        } finally {
            ThreadContext.remove("busiModule");
        }
    }


    /**
     * 描述： 将错误信息记录到错误级别日志(主要由平台内部组件调用)
     *
     * @param busiModule 模块名
     * @param message    日志消息
     * @param t          异常
     */
    public static void errorTrace(String busiModule, String message, Throwable t) {
        try {
            ThreadContext.put("busiModule", busiModule);
            Class clazz = ReflectionUtil.getCallerClass(2);
            ExtendedLogger logger = (ExtendedLogger) LogManager.getLogger(clazz);
            logger.logIfEnabled(FQCN, Level.ERROR, null, message, (Throwable) t);
        } finally {
            ThreadContext.remove("busiModule");
        }
    }


    /**
     * 描述： 将错误信息记录到错误级别日志，所有错误信息记录到一个错误日志(主要由平台内部组件调用)
     *
     * @param busiModule 模块名
     * @param message    日志消息
     * @param t          异常
     */
    public static void errorInFileTrace(String busiModule, String message, Throwable t) {

        if (allErrorInFileSwitch) {
            try {
                ThreadContext.put("busiModule", busiModule);
                Class clazz = ReflectionUtil.getCallerClass(2);
                ExtendedLogger logger = (ExtendedLogger) LogManager.getLogger(clazz);
                logger.logIfEnabled(FQCN, Level.ERROR, null, message, (Throwable) t);
            } finally {
                ThreadContext.remove("busiModule");
            }
        }
    }

}

/*
 * @project: webdemo 
 * @package: com.jeeframework.logicframework.util.server
 * @title:   JeeFrameWorkServer.java 
 *
 * Copyright (c) 2016 JeeFrameWork Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.logicframework.util.server;

import com.jeeframework.core.context.support.SpringContextHolder;
import com.jeeframework.core.exception.BaseException;
import com.jeeframework.logicframework.util.logging.LoggerUtil;
import com.jeeframework.logicframework.util.server.http.HttpServer;
import com.jeeframework.logicframework.util.server.tcp.MinaTcpServer;
import com.jeeframework.util.format.DateFormat;
import com.jeeframework.util.properties.JeeProperties;
import com.jeeframework.util.string.StringUtils;
import com.jeeframework.util.validate.Validate;
import org.apache.commons.lang.SystemUtils;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.util.Date;

/**
 * 基础服务类启动类
 *
 * @author lance
 * @version 1.0 2016-03-08 18:49
 */
public class JeeFrameWorkServer {

//    private static final Logger Log = LoggerFactory.getLogger(HttpServer.class);

    public static final String SERVER_CONFIG_FILE = "server.ini";
    private static JeeProperties serverProperties = null;

    public static final String NET_CONTROLLER_PACKAGES = "net.controller.packages";

    private HttpServer httpServer;
    private MinaTcpServer minaTcpServer;

    private ApplicationContext applicationContext = null;//全局applicationContext

    private static JeeFrameWorkServer instance = new JeeFrameWorkServer();

    public static JeeFrameWorkServer getInstance() {
        return instance;
    }

    private JeeFrameWorkServer() {

    }

    public HttpServer getHttpServer() {
        return httpServer;
    }

    public JeeProperties getServerProperties() {
        return serverProperties;
    }

    public void start() {
        initWebEnvVariables();


        serverProperties = new JeeProperties(SERVER_CONFIG_FILE, false);

        httpServer = new HttpServer();
        httpServer.start();

        String webroot = httpServer.getWebroot();
        //根据webroot 获取 class、配置文件等

        applicationContext = SpringContextHolder.getApplicationContext();

        MinaTcpServer minaTcpServer = new MinaTcpServer(applicationContext, serverProperties);
        minaTcpServer.start();


    }

    public static void initWebEnvVariables() {
        initEnvVariables();


        //配置api文档的是否开启
        String confApiDoc = null;
        try {
            confApiDoc = System.getProperty("conf.apidoc");
        } catch (Exception e) {
        }
        if (Validate.isEmpty(confApiDoc)) {
            confApiDoc = "close";
            System.setProperty("conf.apidoc", confApiDoc);
            System.out.println("===============没有配置  -Dconf.apidoc   环境变量，使用默认配置  " + confApiDoc + "  " +
                    "==================");
        } else {
            System.out.println("===============找到 -Dconf.apidoc    系统环境变量，值为：  " + confApiDoc + "  ==================");
        }


        //配置webview的模板路径
        String confTemplPath = null;
        try {
            confTemplPath = System.getProperty("conf.templ.path");
        } catch (Exception e) {
        }
        if (Validate.isEmpty(confTemplPath)) {
            String path = WebServerUtil.findWebDescriptor();
            if (Validate.isEmpty(path)) {
                throw new BaseException("没有找到web.xml");
            }
            String jspDir = StringUtils.substringBefore(path, "web.xml") + "jsp";

            confTemplPath = jspDir;
            System.setProperty("conf.templ.path", confTemplPath);
            System.out.println("===============没有配置  -Dconf.templ.path  环境变量，使用默认配置  " + confTemplPath + "  " +
                    "==================");
        } else {
            System.out.println("===============找到 -Dconf.templ.path   系统环境变量，值为：  " + confTemplPath + "  " +
                    "==================");
        }
    }

    public static void initEnvVariables() {
        //        -Dlog.dir=d:\log\guanv
//                -Dconf.env=local
//                -Drun.log.additivity=true
//                -Droot.log.level=debug
//                -Drun.log.level=debug

        //添加环境变量默认值
        //日志目录
        String logDir = null;
        try {
            logDir = System.getProperty("log.dir");
        } catch (Exception e) {
        }
        if (Validate.isEmpty(logDir)) {

            String[] dirArrays = null;
            if (SystemUtils.IS_OS_WINDOWS) {
                dirArrays = new String[]{"d:", "e:", "f:", "g:", "c:"};
            } else if (SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_MAC_OSX) {
                dirArrays = new String[]{"/tmp"};
            } else if (SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_UNIX) {
                dirArrays = new String[]{"/tmp"};
            }

            for (String dirDirectoryTmp : dirArrays) {

                logDir = dirDirectoryTmp + File.separator + "log";

                File logDirFile = new File(logDir);
                //文件不存在就mkdirs
                if (!logDirFile.exists()) {

                    if (logDirFile.mkdirs()) {
                        break;
                        //如果创建目录成功也退出循环，赋值到  log.dir 系统变量里去
                    }
                } else {
                    break;
                }
            }


            //由于没有指定log目录，需要生成一个临时目录用于区分不同项目，这里按照天yyyymmdd生成目录
            logDir = logDir + File.separator + DateFormat.formatDate(new Date(), DateFormat.DT_YYYYMMDD);
            File logDirFile = new File(logDir);
            //文件不存在就mkdirs
            if (!logDirFile.exists()) {
                if (!logDirFile.mkdirs()) {
                    System.err.println("===============创建日志目录  " + logDir + "   失败，请检查 ==================");
                    System.exit(0);

                }
            }
            System.setProperty("log.dir", logDir);
            System.out.println("===============没有配置  -Dlog.dir   环境变量，使用默认配置  " + logDir + "  ==================");
        } else {
            System.out.println("===============找到 -Dlog.dir   系统环境变量，值为：  " + logDir + "  ==================");
        }

        //配置文件环境变量
        String confEnv = null;
        try {
            confEnv = System.getProperty("conf.env");
        } catch (Exception e) {
        }
        if (Validate.isEmpty(confEnv)) {
            confEnv = "local";
            System.setProperty("conf.env", confEnv);
            System.out.println("===============没有配置  -Dconf.env   环境变量，使用默认配置  " + confEnv + "  ==================");
        } else {
            System.out.println("===============找到 -Dconf.env   系统环境变量，值为：  " + confEnv + "  ==================");
        }

        //系统全局日志
        String rootLogLevel = null;
        try {
            rootLogLevel = System.getProperty("root.log.level");
        } catch (Exception e) {
        }
        if (Validate.isEmpty(rootLogLevel)) {
            rootLogLevel = "debug";
            System.setProperty("root.log.level", rootLogLevel);
            System.out.println("===============没有配置  -Droot.log.level   环境变量，使用默认配置  " + rootLogLevel + "  " +
                    "==================");
        } else {
            System.out.println("===============找到 -Droot.log.level   系统环境变量，值为：  " + rootLogLevel + "  " +
                    "==================");
        }

        //模块运行时日志
        String runLogLevel = null;
        try {
            runLogLevel = System.getProperty("run.log.level");
        } catch (Exception e) {
        }
        if (Validate.isEmpty(runLogLevel)) {
            runLogLevel = "debug";
            System.setProperty("run.log.level", runLogLevel);
            System.out.println("===============没有配置  -Drun.log.level   环境变量，使用默认配置  " + runLogLevel + "  " +
                    "==================");
        } else {
            System.out.println("===============找到 -Drun.log.level   系统环境变量，值为：  " + runLogLevel + "  " +
                    "==================");
        }

        //是否追加到rootlog中
        boolean runLogAdditivity = true;
        try {
            String runLogAdditivityTmp = System.getProperty("run.log.additivity");
            if (!Validate.isEmpty(runLogAdditivityTmp)) {
                runLogAdditivity = Boolean.valueOf(runLogAdditivityTmp);
            }
        } catch (Exception e) {
            runLogAdditivity = true;
        }
        if (runLogAdditivity) {
            System.setProperty("run.log.additivity", "true");
        } else {
            System.setProperty("run.log.additivity", "false");
        }
        System.out.println("===============配置 -Drun.log.additivity，值为：  " + runLogAdditivity + "  ==================");

        LoggerUtil.debugTrace("JeeFrameWorkServer", "初始化logger配置");
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }


    public static void main(String[] args) {
        JeeFrameWorkServer jeeFrameWorkServer = JeeFrameWorkServer.getInstance();
        jeeFrameWorkServer.start();

    }


}

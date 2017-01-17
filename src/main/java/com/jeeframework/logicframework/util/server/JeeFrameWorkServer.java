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
import com.jeeframework.logicframework.util.server.http.HttpServer;
import com.jeeframework.logicframework.util.server.tcp.BaseNetController;
import com.jeeframework.logicframework.util.server.tcp.MinaTcpServer;
import com.jeeframework.logicframework.util.server.tcp.protocol.ProtocolParser;
import com.jeeframework.util.format.DateFormat;
import com.jeeframework.util.properties.JeeProperties;
import com.jeeframework.util.resource.ResolverUtil;
import com.jeeframework.util.string.StringUtils;
import com.jeeframework.util.validate.Validate;
import org.apache.commons.lang.SystemUtils;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.*;

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
            System.out.println("===============没有配置  -Droot.log.level   环境变量，使用默认配置  " + rootLogLevel + "  ==================");
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
            System.out.println("===============没有配置  -Drun.log.level   环境变量，使用默认配置  " + runLogLevel + "  ==================");
        }

        //是否追加到rootlog中
        boolean runLogAdditivity = true;
        try {
            runLogAdditivity = Boolean.valueOf(System.getProperty("run.log.additivity"));
        } catch (Exception e) {
            runLogAdditivity = true;
        }
        if (runLogAdditivity) {
            System.setProperty("run.log.additivity", "true");
        } else {
            System.setProperty("run.log.additivity", "false");
        }


        serverProperties = new JeeProperties(SERVER_CONFIG_FILE, false);

        httpServer = new HttpServer();
        httpServer.start();

        String webroot = httpServer.getWebroot();
        //根据webroot 获取 class、配置文件等

        MinaTcpServer minaTcpServer = new MinaTcpServer();
        minaTcpServer.start();

        applicationContext = SpringContextHolder.getApplicationContext();
        if (applicationContext != null) {
            try {
                scanAndRegistActionClass(applicationContext);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 从配置文件获得netserver的配置。
     * 扫描配置的路径，并把继承NetBusiness类的Action注入容器。
     *
     * @param context 容器池
     * @throws Exception
     */
    protected static void scanAndRegistActionClass(ApplicationContext context) throws Exception {

        String packages = serverProperties.getProperty(NET_CONTROLLER_PACKAGES);
        ;
        Set<Class> actionClassesSet = new HashSet<Class>(); // 装载
        // action的class
        // 集合
        // 扫描需要找的的action类
        if (packages != null) {
            // String[] names = packages.split("\\s*[,]\\s*");
            // lanceyan 增加解析各个包路径，包括通配符
            String[] names = StringUtils.tokenizeToStringArray(packages, ",; \t\n", true, true);
            // Initialize the classloader scanner with the configured
            // packages
            List<String> allPackagePath = new ArrayList<String>();
            if (names.length > 0) {
                for (String name : names) {
                    name = name.replace('.', '/');
                    String[] resourceNames = null;
                    PathMatchingResourcePatternResolverWrapper resourceLoader = new PathMatchingResourcePatternResolverWrapper();
                    try {
                        // 获取根路径  modify bylanceyan  ，增加获取jar包和classpath路径的方法
                        Resource[] actionResources = ((ResourcePatternResolver) resourceLoader)
                                .getResources("classpath*:" + name);

                        if (actionResources != null) {
                            resourceNames = new String[actionResources.length];
                            for (int i = 0; i < actionResources.length; i++) {
                                // 得到相对路径，然后替换为包的命名方式
                                resourceNames[i] = resourceLoader.getRelativeResourcesPath(actionResources[i]);

                            }
                        }
                    } catch (IOException e) {
                        System.out.println("Loaded action configuration from:getAllResource()");
                    }

//                    String[] resourceNames = PathMatchingResourcePatternResolverWrapper.getAllResource(name);

                    if (resourceNames != null && resourceNames.length > 0) {
                        for (String resourceName : resourceNames) {
                            allPackagePath.add(resourceName);
                        }
                    }
                }
                loadActionClass(actionClassesSet, allPackagePath.toArray(new String[0]));
            }
        }

        // 注册扫描到的action
        for (Object obj : actionClassesSet) {
            Class cls = (Class) obj;
            DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getAutowireCapableBeanFactory();
            RootBeanDefinition def = new RootBeanDefinition();
            def.setBeanClass(cls);
            def.setAutowireMode(AutowireCapableBeanFactory.AUTOWIRE_BY_NAME);

            String beanName = StringUtils.uncapitalize(cls.getSimpleName());

            System.out.println("Net action class  beanName   " + beanName + " has  loaded in context! ");
            beanFactory.registerBeanDefinition(beanName, def);


        }

    }


    /**
     * 从扫描得到的action集合把继承NetBusiness的类加载到容器池。
     *
     * @param actionClassesSet action class集合
     * @param pkgs             容器池
     */
    protected static void loadActionClass(Set<Class> actionClassesSet, String[] pkgs) {
        ResolverUtil<Class> resolver = new ResolverUtil<Class>();
        resolver.find(new ResolverUtil.Test() {
            // 回调函数，用于校验类是否是继承NetBusiness
            public boolean matches(Class type) {
                // TODO: should also find annotated classes
                return (BaseNetController.class.isAssignableFrom(type));
            }

        }, pkgs);

        Set<? extends Class<? extends Class>> actionClasses = resolver.getClasses();
        for (Object obj : actionClasses) {
            Class cls = (Class) obj;
            if (!Modifier.isAbstract(cls.getModifiers())) {
                // ClassPathXmlApplicationContext context1;
                // context1.

                actionClassesSet.add(cls);
                ProtocolParser.registerNetControllerClazz(cls); // 在服务器里注册加了annotation的方法
                // 比如： @Protocol(cmdId = 0x26211803, desc = "根据角色获取留言", export =
                // true)
            }
        }
    }

    public static void main(String[] args) {
        JeeFrameWorkServer jeeFrameWorkServer = JeeFrameWorkServer.getInstance();
        jeeFrameWorkServer.start();

    }


}

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
import com.jeeframework.util.properties.JeeProperties;
import com.jeeframework.util.resource.ResolverUtil;
import com.jeeframework.util.string.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 基础服务类启动类
 *
 * @author lance
 * @version 1.0 2016-03-08 18:49
 */
public class JeeFrameWorkServer {

    private static final Logger Log = LoggerFactory.getLogger(HttpServer.class);

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
                        Log.debug("Loaded action configuration from:getAllResource()");
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

            System.out.println("beanName   " + beanName + "   loaded in context! ");
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

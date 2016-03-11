/*
 * @project: webdemo 
 * @package: com.jeeframework.logicframework.util.server.tcp.protocol
 * @title:   ProtocolParser.java 
 *
 * Copyright (c) 2016 Hyfay Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.logicframework.util.server.tcp.protocol;

import com.jeeframework.logicframework.util.server.tcp.annotation.NetProtocol;
import com.jeeframework.util.string.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * 协议的解析类
 *
 * @author lance
 * @version 1.0 2016-03-09 16:51
 */
public class ProtocolParser {
    private static HashMap<Object, Class<?>> cmdReqs = new HashMap<Object, Class<?>>();
    private static HashMap<Object, Class<?>> cmdResponses = new HashMap<Object, Class<?>>();
    private static HashMap<Object, String> cmdFunctions = new HashMap<Object, String>();

    /*
 * 存储命令字和业务处理类的hashmap
 */
    private static HashMap<Long, String> netControllerCache = new HashMap<Long, String>();

    /**
     * 根据命令字生成 请求对象
     *
     * @param cmdid
     * @return
     */
    public static NetData getRequestClazzByCmdId(long cmdid) {
        Class<?> clazz = cmdReqs.get(cmdid);
        if (clazz == null) {
            System.out.println("ProtocolParser  getRequest Data :"
                    + Long.toHexString(cmdid) + " no Class Deal it ! ");
            return null;
        }

        try {
            return (NetData) clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static NetData getResponseClazzByCmdId(long cmdId) {
        Class<?> clazz = cmdResponses.get(cmdId);
        if (clazz == null) {
            System.out.println("ProtocolParser  getResponse Data :"
                    + Long.toHexString(cmdId) + " no Class Deal it ! ");
            return null;
        }

        try {
            return (NetData) clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据命令字生成 请求对象
     *
     * @param cmdid
     * @return
     */
    public static String getBaseNetControllerByCmdId(long cmdid) {
        return netControllerCache.get(cmdid);
    }

    /**
     * 根据命令字生成 请求对象
     *
     * @param cmdid
     * @return
     */
    public static void cacheBaseNetController(long cmdid, String baseNetController) {
        netControllerCache.put(cmdid, baseNetController);
    }

    /**
     * 根据命令号获取对应的方法名
     *
     * @param cmdId
     * @return
     */
    public static String getRequestMethodByCmdId(long cmdId) {
        return cmdFunctions.get(cmdId);
    }

    /**
     * 注册网络的controller接口
     *
     * @param netControllerClazz
     */
    public static synchronized void registerNetControllerClazz(Class<?> netControllerClazz) {

        // 分析方法，所有标注为 protocol 的方法全部进行注册
        Method[] methods = netControllerClazz.getDeclaredMethods();
        registMethod(netControllerClazz, methods);

        // 获取取基类的接口，兼容接口方式注入
        Class<?>[] interfaces = netControllerClazz.getInterfaces();
        if (interfaces != null) {
            for (int i = 0; i < interfaces.length; i++) {
                Class<?> interfaceOne = interfaces[i];
                methods = interfaceOne.getDeclaredMethods();
                registMethod(netControllerClazz, methods);

            }

        }
    }

    private static void registMethod(Class<?> netControllerClazz, Method[] methods) {
        if (methods != null) {
            for (int i = 0; i < methods.length; i++) {
                Method one = methods[i];
                if (!one.isAnnotationPresent(NetProtocol.class))
                    continue;

                // 分析方法的protocol信息
                NetProtocol protocol = one.getAnnotation(NetProtocol.class);
                Long cmdid = protocol.cmdId();
                //
                cmdFunctions.put(cmdid, one.getName());
                // 提取请求参数类
                cmdReqs.put(cmdid, one.getParameterTypes()[0]);

                cmdResponses.put(cmdid, one.getReturnType());
                String beanName = StringUtils.uncapitalize(netControllerClazz.getSimpleName());
                ProtocolParser.cacheBaseNetController(cmdid, beanName);

                System.out.println(one.getName());
            }
        }
    }
}

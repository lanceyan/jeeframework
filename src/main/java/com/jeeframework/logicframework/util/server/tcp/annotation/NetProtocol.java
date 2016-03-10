/*
 * @project: webdemo 
 * @package: com.jeeframework.logicframework.util.server.tcp.annotation
 * @title:   NetProtocol.java 
 *
 * Copyright (c) 2016 Hyfay Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.logicframework.util.server.tcp.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 网络协议注解
 *
 * @author lance
 * @version 1.0 2016-03-09 18:00
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface  NetProtocol {
    /**
     * 命令字
     * @return
     */
    long cmdId() default 0;

    /**
     * 协议描述
     * @return
     */
    String desc() default "";

    /**
     * 协议版本
     * @return
     */
    int version() default 0;

    /**
     * 是否导出成接口
     * @return
     */
    boolean export() default false;
}

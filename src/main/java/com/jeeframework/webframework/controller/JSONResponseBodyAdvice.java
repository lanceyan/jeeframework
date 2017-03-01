/*
 * @project: mytest2 
 * @package: com.mytest2.web.apidoc
 * @title:   MyResponseBodyAdvice.java 
 *
 * Copyright (c) 2017 jeeframework Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.webframework.controller;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 用于转换json对象，并封装数据
 *
 * @author lance
 * @version 1.0 2017-02-13 18:48
 */
@ControllerAdvice
public class JSONResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {


        DataResponse dataResponse = new DataResponse();

        dataResponse.setData(body);
        //返回修改后的值
        return dataResponse;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class converterType) {
        //获取当前处理请求的controller的方法
        String methodName = methodParameter.getMethod().getName();
        String className = methodParameter.getMethod().getDeclaringClass().getName();

        if (className.startsWith("com.mangofactory.swagger")) {
            return false;
        }

        return MappingJackson2HttpMessageConverter.class
                .isAssignableFrom(converterType);
    }
}
/*
 * @project: webdemo 
 * @package: com.jeeframework.logicframework.util.server.tcp
 * @title:   BaseNetController.java 
 *
 * Copyright (c) 2016 Hyfay Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.logicframework.util.server.tcp;

import com.jeeframework.core.logging.Logger;
import com.jeeframework.logicframework.util.logging.LoggerUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * 网络层访问接口层
 *
 * @author lance
 * @version 1.0 2016-03-09 16:26
 */
public class BaseNetController implements BeanFactoryAware {

    private BeanFactory context = null;
    protected Logger logger = LoggerUtil.getLogger();

    private HashMap<String, Method> methods = new HashMap<String, Method>();

    public final Object callMethod(String methodName, Class<?> reqClazz, Object req) throws Exception {
        Method method = methods.get(methodName);

        try {
            if (method == null) {
                method = this.getClass().getDeclaredMethod(methodName, new Class[]{reqClazz});
                methods.put(methodName, method);
            }
        } catch (Exception e) {
            throw new Exception("No method [" + methodName + "] is defined in " + this.getClass().getName());
        }

        try {
            return method.invoke(this, new Object[]{req});
        } catch (Exception e) {
            System.out.println("Exception!");
            e.printStackTrace();
            throw new Exception("invoke method [" + methodName + "] in " + this.getClass().getName() + " failed!");
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.context = beanFactory;
    }
}

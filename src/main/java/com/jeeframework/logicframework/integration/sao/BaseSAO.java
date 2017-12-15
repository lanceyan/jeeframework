package com.jeeframework.logicframework.integration.sao;

import com.jeeframework.logicframework.beans.ContextManageBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;


/**
 * 服务访问对象的基础类
 */
public abstract class BaseSAO implements ContextManageBean {

    protected BeanFactory context;
    protected String beanName = null;

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.context = beanFactory;
    }

    public String getBeanName() {
        return beanName;
    }


    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }






}

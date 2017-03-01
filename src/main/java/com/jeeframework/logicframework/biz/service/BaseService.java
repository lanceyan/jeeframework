package com.jeeframework.logicframework.biz.service;

import com.jeeframework.logicframework.beans.ContextManageBean;
import org.springframework.beans.factory.BeanFactory;

/**
 * 抽象BizService的基类，业务服务对象。
 * 开发BizService必须继承BaseService。
 *
 * @author lanceyan（最新修改者）
 * @version 1.0{新版本号）
 */
public abstract class BaseService implements ContextManageBean {
    /**
     * 当前bean的容器池
     */
    protected BeanFactory context;
    /**
     * 得到配置的bean的名字
     */
    protected String name;

    /**
     * 默认构造函数
     */
    public BaseService() {

        super();
    }

    /**
     * 设置bean的名字
     *
     * @param beanName bean的名字
     * @see org.springframework.beans.factory.BeanNameAware#setBeanName(java.lang.String)
     */
    public void setBeanName(String name) {
        this.name = name;
    }

    /**
     * spring的销毁方法
     * 开发可以自己覆盖自定义
     *
     * @see org.springframework.beans.factory.DisposableBean#destroy()
     */
    public void destroy() {
    }

    /**
     * spring的初始化函数，用户可以重写，初始化参数
     *
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * 用户自定义context对象
     *
     * @param beanFactory 可以自定义容器池
     * @throws org.springframework.beans.BeansException
     * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
     */
    public void setBeanFactory(BeanFactory beanFactory) {
        this.context = beanFactory;
    }




}

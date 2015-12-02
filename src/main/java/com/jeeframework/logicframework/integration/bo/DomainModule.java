package com.jeeframework.logicframework.integration.bo;

import java.io.Serializable;

import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * ����ģ�͵Ľӿ�
 * 
 * @author lanceyan�������޸��ߣ�
 * @version 1.0���°汾�ţ�
 */

public interface DomainModule extends BeanFactoryAware, InitializingBean, DisposableBean, Serializable {

}

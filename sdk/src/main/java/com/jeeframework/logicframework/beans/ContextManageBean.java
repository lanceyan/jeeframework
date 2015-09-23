/**
 * Copyright (C) 1998-2007 JEEFRAMEWORK Inc.All Rights Reserved.
 * 																	
 * FileName��ContextManageBean.java					
 *			
 * Description����Ҫ�������ļ�������							 * 												 * History��
* �汾��    ����      ����       ��Ҫ������ز���
*  1.0   lanceyan  2008-5-15  Create	
*/

package com.jeeframework.logicframework.beans;

import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/** 
 * ����bean��Service��SAO��DAO�Ľӿ�
 * @author lanceyan�������޸��ߣ�
 * @version 1.0���°汾�ţ�
 * 
 */

public interface ContextManageBean extends BeanFactoryAware, BeanNameAware, InitializingBean, DisposableBean
{

}



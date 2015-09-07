/**
 * Copyright (C) 1998-2008 TENCENT Inc.All Rights Reserved.		
 * 																	
 * FileName��PaiPaiDispatcher.java					
 *			
 */

package com.jeeframework.webframework.filter.dispatcher;

import com.opensymphony.xwork2.config.providers.XmlConfigurationProvider;
import org.apache.struts2.dispatcher.Dispatcher;

import javax.servlet.ServletContext;
import java.util.Map;

/**
 * ���ĸ�д�ĵ�����
 * 
 * @author lanceyan�������޸��ߣ�
 * @version 1.0���°汾�ţ�
 * @see �ο���JavaDoc
 */

public class DispatcherWrapper extends Dispatcher {

	

	/**
     * 简单描述：继承dispatcher的构造方法
     * <p>
     * 详细描述：继承dispatcher的构造方法
     * 
     * @param servletContext
     * @param initParams 
     */
    public DispatcherWrapper(ServletContext servletContext, Map<String, String> initParams) {
        super(servletContext, initParams);
        
    }

    protected XmlConfigurationProvider createXmlConfigurationProvider(String filename, boolean errorIfMissing) {
		return new XmlConfigurationProviderWrapper(filename, errorIfMissing);
	}

	protected XmlConfigurationProvider createStrutsXmlConfigurationProvider(String filename, boolean errorIfMissing, ServletContext ctx) {
		return new StrutsXmlConfigurationProviderWrapper(filename, errorIfMissing, ctx);
	}

}

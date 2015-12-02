package com.jeeframework.logicframework.common.remote;

import java.util.HashSet;
import java.util.Properties;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.jeeframework.util.validate.Validate;

public class PropertyReaderUtilObject extends PropertyPlaceholderConfigurer implements InitializingBean {
	private String nullValue;

	private Properties pros;

	public void afterPropertiesSet() throws Exception {
		pros = mergeProperties();

	}

	public String getParseValue(String key) {

		String val = pros.getProperty(key);

		if (Validate.isEmpty(val)) {
			return val;
		}
		String ret = parseStringValue(val, this.pros, new HashSet());

		return ret;
	}

	public String getValue(String key) {
		return pros.getProperty(key);
	}
}

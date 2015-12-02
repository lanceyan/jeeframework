package com.jeeframework.logicframework.common.remote;

import com.jeeframework.util.validate.Validate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.HashSet;
import java.util.Properties;

public class PropertyReaderUtil extends PropertyPlaceholderConfigurer implements InitializingBean {
	private String nullValue;

	private static  Properties pros;

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

	public static String getValue(String key) {
		return pros.getProperty(key);
	}
}

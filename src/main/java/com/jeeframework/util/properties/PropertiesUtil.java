package com.jeeframework.util.properties;

import com.jeeframework.util.classes.ClassUtils;
import com.jeeframework.util.validate.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Properties;


public class PropertiesUtil {


	/**
	 * Load all properties from the given class path resource,
	 * using the default class loader.
	 * <p>Merges properties if more than one resource of the same name
	 * found in the class path.
	 * @param resourceName the name of the class path resource
	 * @return the populated Properties instance
	 * @throws IOException if loading failed
	 */
	public static Properties loadAllProperties(String resourceName) throws IOException {
		return loadAllProperties(resourceName, null);
	}

	/**
	 * Load all properties from the given class path resource,
	 * using the given class loader.
	 * <p>Merges properties if more than one resource of the same name
	 * found in the class path.
	 * @param resourceName the name of the class path resource
	 * @param classLoader the ClassLoader to use for loading
	 * (or <code>null</code> to use the default class loader)
	 * @return the populated Properties instance
	 * @throws IOException if loading failed
	 */
	public static Properties loadAllProperties(String resourceName, ClassLoader classLoader) throws IOException {
		Assert.notNull(resourceName, "Resource name must not be null");
		ClassLoader clToUse = classLoader;
		if (clToUse == null) {
			clToUse = ClassUtils.getDefaultClassLoader();
		}
		Properties properties = new Properties();
		Enumeration urls = clToUse.getResources(resourceName);
		while (urls.hasMoreElements()) {
			URL url = (URL) urls.nextElement();
			InputStream is = null;
			try {
				URLConnection con = url.openConnection();
				con.setUseCaches(false);
				is = con.getInputStream();
				properties.load(is);
			}
			finally {
				if (is != null) {
					is.close();
				}
			}
		}
		return properties;
	}
}

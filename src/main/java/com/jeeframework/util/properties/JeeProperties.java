package com.jeeframework.util.properties;

import com.jeeframework.util.classes.ClassUtils;
import com.jeeframework.util.validate.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class JeeProperties extends Properties {

    private static final Logger Log = LoggerFactory.getLogger(JeeProperties.class);
    private java.util.Properties pros;
    private int reloadInterval = 60;//60S重新加载一次properties
    private long currentFileTime = 0; //文件修改时间

    private String resourceName = "";

    public int getReloadInterval() {
        return reloadInterval;
    }

    public void setReloadInterval(int reloadInterval) {
        this.reloadInterval = reloadInterval;
    }

    /**
     * Load all properties from the given class path resource, using the default
     * class loader.
     * <p/>
     * Merges properties if more than one resource of the same name found in the
     * class path.
     *
     * @param resourceName the name of the class path resource
     * @return the populated Properties instance
     * @throws IOException if loading failed
     */
    public JeeProperties(String resourceName) {
        this(resourceName, false);
    }


    /**
     * @param resourceName 资源名
     * @param reloaded     是否可以重新加载
     */
    public JeeProperties(String resourceName, boolean reloaded) {
        try {
            this.resourceName = resourceName;
            pros = loadAllPropertiesClassLoader(resourceName, null);
            if (reloaded) {
                new Thread(new ReloadThread()).start();
            }
        } catch (IOException e) {
            Log.error("Error load properties from " + resourceName, e);
        }
    }

    /**
     * 加载系统环境变量
     */
    public void loadSystemEnv() {
        super.putAll(pros);
        System.setProperties(this);
    }

    /**
     * Load all properties from the given class path resource, using the given
     * class loader.
     * <p/>
     * Merges properties if more than one resource of the same name found in the
     * class path.
     *
     * @param resourceName the name of the class path resource
     * @param classLoader  the ClassLoader to use for loading (or <code>null</code> to
     *                     use the default class loader)
     * @return the populated Properties instance
     * @throws IOException if loading failed
     */
    public Properties loadAllPropertiesClassLoader(String resourceName, ClassLoader classLoader) throws IOException {
        Assert.notNull(resourceName, "Resource name must not be null");
        ClassLoader clToUse = classLoader;
        if (clToUse == null) {
            clToUse = ClassUtils.getDefaultClassLoader();
        }
        Properties properties = new Properties();
        URL url = clToUse.getResource(resourceName);
        loadPropsFromUrl(properties, url);
//        Enumeration urls = clToUse.getResources(resourceName);
//        while (urls.hasMoreElements()) {
//            URL url = (URL) urls.nextElement();
//            InputStream is = null;
//            try {
//                URLConnection con = url.openConnection();
//                con.setUseCaches(false);
//                is = con.getInputStream();
//                properties.load(is);
//            } finally {
//                if (is != null) {
//                    is.close();
//                }
//            }
//        }
        return properties;
    }

    private void loadPropsFromUrl(Properties properties, URL url) throws IOException {
        InputStream is = null;
        try {
            URLConnection con = url.openConnection();
            con.setUseCaches(false);
            is = con.getInputStream();
            properties.load(is);
            this.putAll(properties);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String getProperty(String key) {
        return pros.getProperty(key);
    }

    public class ReloadThread implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    ClassLoader clToUse = ClassUtils.getDefaultClassLoader();
                    URL url = clToUse.getResource(resourceName);
                    File file;
                    try {
                        file = new File(url.toURI());
                    } catch (URISyntaxException e) {
                        file = new File(url.getPath());
                    }
                    long lastModifiedTime = file.exists() ? file.lastModified() : -1;
                    if (currentFileTime == 0) {
                        currentFileTime = lastModifiedTime; //第一次把当前的文件时间戳付给currentFileTime
                    }
                    if (currentFileTime != lastModifiedTime) {
                        Log.info(" File [ " + file.getName() + " ] changed At: "
                                + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(lastModifiedTime));
                        currentFileTime = lastModifiedTime;
                        loadPropsFromUrl(pros, url);
                    }
                } catch (IOException e) {
                    Log.error(resourceName + "  JeeProperties 重新加载出错 ", e);
                } finally {
                    try {
                        Thread.sleep(reloadInterval * 1000);
                    } catch (InterruptedException e) {
                        Log.error(resourceName + "  JeeProperties 休眠出错 ", e);
                    }
                    Log.debug(" 休眠 " + reloadInterval + " 秒再监测资源文件是否有更新");
                }

            }
        }
    }

    /**
     * Returns a Jive property. If the specified property doesn't exist, the
     * <tt>defaultValue</tt> will be returned.
     *
     * @param name         the name of the property to return.
     * @param defaultValue value returned if the property doesn't exist.
     * @return the property value specified by name.
     */
    public String getProperty(String name, String defaultValue) {
        String value = pros.getProperty(name);
        if (value != null) {
            return value;
        } else {
            return defaultValue;
        }
    }

    /**
     * Returns an integer value Jive property. If the specified property doesn't exist, the
     * <tt>defaultValue</tt> will be returned.
     *
     * @param name         the name of the property to return.
     * @param defaultValue value returned if the property doesn't exist or was not
     *                     a number.
     * @return the property value specified by name or <tt>defaultValue</tt>.
     */
    public int getIntProperty(String name, int defaultValue) {
        String value = getProperty(name);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException nfe) {
                // Ignore.
            }
        }
        return defaultValue;
    }

    /**
     * Returns a long value Jive property. If the specified property doesn't exist, the
     * <tt>defaultValue</tt> will be returned.
     *
     * @param name         the name of the property to return.
     * @param defaultValue value returned if the property doesn't exist or was not
     *                     a number.
     * @return the property value specified by name or <tt>defaultValue</tt>.
     */
    public long getLongProperty(String name, long defaultValue) {
        String value = getProperty(name);
        if (value != null) {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException nfe) {
                // Ignore.
            }
        }
        return defaultValue;
    }

    /**
     * Returns a boolean value Jive property.
     *
     * @param name the name of the property to return.
     * @return true if the property value exists and is set to <tt>"true"</tt> (ignoring case).
     * Otherwise <tt>false</tt> is returned.
     */
    public boolean getBooleanProperty(String name) {
        return Boolean.valueOf(getProperty(name));
    }

    /**
     * Returns a boolean value Jive property. If the property doesn't exist, the <tt>defaultValue</tt>
     * will be returned.
     * <p/>
     * If the specified property can't be found, or if the value is not a number, the
     * <tt>defaultValue</tt> will be returned.
     *
     * @param name         the name of the property to return.
     * @param defaultValue value returned if the property doesn't exist.
     * @return true if the property value exists and is set to <tt>"true"</tt> (ignoring case).
     * Otherwise <tt>false</tt> is returned.
     */
    public boolean getBooleanProperty(String name, boolean defaultValue) {
        String value = getProperty(name);
        if (value != null) {
            return Boolean.valueOf(value);
        } else {
            return defaultValue;
        }
    }

    public static void main(String[] args) {
        JeeProperties props = new JeeProperties(
                "http.ini", true);
    }

}

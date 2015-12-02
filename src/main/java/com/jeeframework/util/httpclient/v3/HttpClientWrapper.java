package com.jeeframework.util.httpclient.v3;

import java.util.List;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jeeframework.util.validate.Validate;

/**
 * 作为httpclient的代理类
 * <p>
 * 
 * @Description: 代理httpclient访问接口，通过interval设定的频率间隔访问数据接口
 * @author lanceyan
 * @version 1.0 2014-5-28 上午10:08:33
 */
public class HttpClientWrapper {

    /**
     * logger记录属性
     */
    protected static Log logger = LogFactory.getLog(HttpClientWrapper.class);

    private int interval = 0; // 访问频率限制

    private long lastAccessTime = 0L; // 上次访问的时间

    private HttpClientHelper crawlHttper = new HttpClientHelper(false);

    public HttpClientWrapper(int interval) {
        this.interval = interval;
    }

    public String doGet(String actionUrl, String encoding, String referer, boolean needUrlEncode, List<Cookie> cookieList) {

        long curTime = System.currentTimeMillis();

        long diffTime = curTime - lastAccessTime;

        while (diffTime < lastAccessTime) {

            try {
                Thread.sleep(interval);

                String sRunLog = " 访问  " + actionUrl + "  设置访问频率interval，休息（" + interval + "）秒--完成。";
                logger.info(sRunLog);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 网络错误重试3次
        String content = "";
        int icount = 0;
        while (icount < 3 && Validate.isEmpty(content)) {
            try {
                content = crawlHttper.doGet(actionUrl, encoding, referer, needUrlEncode, cookieList);
                break;
            } catch (Exception pie) {
                icount++;

                String sRunLog = " 访问  " + actionUrl + "   访问数据出现问题，重试（" + icount + "）次。";
                logger.error(sRunLog + pie, pie);
                // pie.printStackTrace();
            }
        }
        return content;

    }

    public String doGet(String actionUrl, String encoding, String referer, List<Cookie> cookieList) {

        long curTime = System.currentTimeMillis();

        long diffTime = curTime - lastAccessTime;

        while (diffTime < lastAccessTime) {

            try {
                Thread.sleep(interval);

                String sRunLog = " 访问  " + actionUrl + "  设置访问频率interval，休息（" + interval + "）秒--完成。";
                logger.info(sRunLog);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 网络错误重试3次
        String content = "";
        int icount = 0;
        while (icount < 3 && Validate.isEmpty(content)) {
            try {
                content = crawlHttper.doGet(actionUrl, encoding, referer, false, cookieList);
                break;
            } catch (Exception pie) {
                icount++;

                String sRunLog = " 访问  " + actionUrl + "   访问数据出现问题，重试（" + icount + "）次。";
                logger.error(sRunLog + pie, pie);
                // pie.printStackTrace();
            }
        }
        return content;

    }

    public byte[] doGetBytes(String actionUrl, String encoding, String referer, boolean needUrlEncode) {

        long curTime = System.currentTimeMillis();

        long diffTime = curTime - lastAccessTime;

        while (diffTime < lastAccessTime) {

            try {
                Thread.sleep(interval);

                String sRunLog = " 访问  " + actionUrl + "  设置访问频率interval，休息（" + interval + "）秒--完成。";
                logger.info(sRunLog);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 网络错误重试3次

        byte[] byteArray = null;

        int icount = 0;
        while (icount < 3 && Validate.isNull(byteArray)) {
            try {
                byteArray = crawlHttper.doGetBytes(actionUrl, encoding, referer, needUrlEncode);
                break;
            } catch (Exception pie) {
                icount++;

                String sRunLog = " 访问  " + actionUrl + "  访问数据出现问题，重试（" + icount + "）次。";
                logger.error(sRunLog + pie, pie);
                // pie.printStackTrace();
            }
        }
        return byteArray;

    }

    public String doPost(String actionUrl, NameValuePair[] params, String encoding, String referer, boolean needHeader, boolean needRedirect, List<Cookie> cookieList) {

        long curTime = System.currentTimeMillis();

        long diffTime = curTime - lastAccessTime;

        while (diffTime < lastAccessTime) {

            try {
                Thread.sleep(interval);

                String sRunLog = " 访问  " + actionUrl + "  设置访问频率interval，休息（" + interval + "）秒--完成。";
                logger.info(sRunLog);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 网络错误重试3次
        String content = "";
        int icount = 0;
        while (icount < 3 && Validate.isEmpty(content)) {
            try {
                content = crawlHttper.doPost(actionUrl, params, null, encoding, referer, needHeader, needRedirect, cookieList);
                break;
            } catch (RuntimeException pie) {
                icount++;

                String sRunLog = " 访问  " + actionUrl + " 数据出现问题，重试（" + icount + "）次。";
                logger.error(sRunLog);
            }
        }
        return content;

    }

    public String doPost(String actionUrl, RequestEntity requestEntity, String encoding, String referer, boolean needHeader, boolean needRedirect, List<Cookie> cookieList) {

        long curTime = System.currentTimeMillis();

        long diffTime = curTime - lastAccessTime;

        while (diffTime < lastAccessTime) {

            try {
                Thread.sleep(interval);

                String sRunLog = " 访问  " + actionUrl + "  设置访问频率interval，休息（" + interval + "）秒--完成。";
                logger.info(sRunLog);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 网络错误重试3次
        String content = "";
        int icount = 0;
        while (icount < 3 && Validate.isEmpty(content)) {
            try {
                content = crawlHttper.doPost(actionUrl, null, requestEntity, encoding, referer, needHeader, needRedirect, cookieList);
                break;
            } catch (RuntimeException pie) {
                icount++;

                String sRunLog = " 访问  " + actionUrl + " 数据出现问题，重试（" + icount + "）次。";
                logger.error(sRunLog);
            }
        }
        return content;

    }

    public void setProxy(final String proxyHost, int proxyPort) {
        crawlHttper.setProxy(proxyHost, proxyPort);
    }

}

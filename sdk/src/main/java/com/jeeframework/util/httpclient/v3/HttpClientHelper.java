package com.jeeframework.util.httpclient.v3;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.io.IOUtils;

import com.jeeframework.util.string.StringUtils;
import com.jeeframework.util.validate.Validate;

public class HttpClientHelper {

    // 连接超时时间（默认10秒 10000ms） 单位毫秒（ms）
    private int connectionTimeout = 15000;

    // 读取数据超时时间（默认30秒 30000ms） 单位毫秒（ms）
    private int soTimeout = 30000;

    // 每个主机的最大并行链接数，默认为2
    private int maxConnectionsPerHost = 160;

    // 客户端总并行链接最大数，默认为20
    private int maxTotalConnections = 256;

    // 一次性从数据流读取的最大byte数
    private int max_bytes = 4096;

    private InputStream is;

    private HttpClient httpClient = null;

    private String redirectUrl = "";// 重定向url

    // 构造HttpClient的实例
    private static HttpClientHelper instance;

    public HttpClientHelper(boolean isMulti) {
        HttpConnectionManagerParams params = null;
        if (isMulti) {
            HttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
            // params = httpConnectionManager.getParams();
            httpClient = new HttpClient(httpConnectionManager);
            params = httpClient.getHttpConnectionManager().getParams();
            params.setStaleCheckingEnabled(false);
            params.setDefaultMaxConnectionsPerHost(maxConnectionsPerHost);
            params.setMaxTotalConnections(maxTotalConnections);

        } else {
            HttpConnectionManager httpConnectionManager = new SimpleHttpConnectionManager(true);
            httpClient = new HttpClient(httpConnectionManager);
            params = httpClient.getHttpConnectionManager().getParams();
        }

        params.setTcpNoDelay(true);
        params.setLinger(1000);

        params.setConnectionTimeout(connectionTimeout);
        params.setSoTimeout(soTimeout);
        // params.setParameter("http.protocol.cookie-policy",
        // CookiePolicy.BROWSER_COMPATIBILITY);
        // params.setParameter("http.protocol.single-cookie-header", true);
        // params.setParameter("http.protocol.content-charset",encoding);
    }

    public static synchronized HttpClientHelper getInstance(boolean isMulti) {
        if (instance == null) {
            instance = new HttpClientHelper(isMulti);
        }
        return instance;
    }

    public static synchronized HttpClientHelper getInstance() {
        if (instance == null) {
            instance = new HttpClientHelper(false);
        }
        return instance;
    }

    public void setProxy(final String proxyHost, int proxyPort) {
        if (null != httpClient) {
            httpClient.getHostConfiguration().setProxy(proxyHost, proxyPort);
        }
    }

    public void addCookie(Cookie cookie) {
        httpClient.getState().addCookie(cookie);
    }

    public void addCookie(Cookie[] cookies) {
        httpClient.getState().addCookies(cookies);
    }

    private void supportSSL(String strUrl) {
        Protocol myhttps = new Protocol("https", new MySecureProtocolSocketFactory(), 443);
        Protocol.registerProtocol("https", myhttps);
    }

    public void setParameter(final String name, final Object value) {
        if (null != httpClient) {
            HttpConnectionManagerParams params = httpClient.getHttpConnectionManager().getParams();
            params.setParameter(name, value);
        }
    }

    public void setParameters(final String[] names, final Object value) {
        for (int i = 0; i < names.length; i++) {
            setParameter(names[i], value);
        }
    }

    /**
     * 进行HTTP Post请求
     * 
     * @param actionUrl post请求的url
     * @param params post请求的参数
     * 
     * @return Post应答体
     * @throws HttpException
     * @throws IOException
     */
    public String doPost(String actionUrl, NameValuePair[] params) {
        return doPost(actionUrl, params, "");
    }

    /**
     * 进行HTTP Post请求
     * 
     * @param actionUrl post请求的url
     * @param params post请求的参数
     * @param referer HTPP REFERER
     * 
     * @return Post应答体
     * @throws HttpException
     * @throws IOException
     */

    public String doPost(String actionUrl, NameValuePair[] params, String encoding) {
        return doPost(actionUrl, params, encoding, "");
    }

    public String doPost(String actionUrl, NameValuePair[] params, String encoding, String referer) {
        return doPost(actionUrl, params, encoding, referer, true);
    }

    public String doPost(String actionUrl, NameValuePair[] params, String encoding, String referer, boolean needRedirect) {
        return doPost(actionUrl, params, encoding, referer, false, needRedirect);
    }

    public String doPost(String actionUrl, NameValuePair[] params, String encoding, String referer, boolean needHeader, boolean needRedirect) {
        return doPost(actionUrl, params, null, encoding, referer, needHeader, needRedirect, null);
    }

    /*
     * public String doPost(String actionUrl, RequestEntity requestEntity,
     * String encoding, String referer,boolean needHeader, boolean needRedirect)
     * { return doPost(actionUrl, null, requestEntity, encoding, referer,
     * needHeader, needRedirect); }
     */

    public String doPost(String actionUrl, NameValuePair[] params, RequestEntity requestEntity, String encoding, String referer, boolean needHeader, boolean needRedirect, List<Cookie> cookieList) {
        if (null == encoding || encoding.trim().length() == 0) {
            encoding = "utf-8";
        }

        if (encoding.trim().equalsIgnoreCase("gb2312")) {
            encoding = "gbk";
        }

        if (actionUrl.startsWith("https:")) {
            this.supportSSL(actionUrl);
        }
        PostMethod method = null;
        try {
            method = new PostMethod(actionUrl);
            if (needHeader) {
                setHeaders(method, referer);
            }
            method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset="+ encoding);
            method.setRequestHeader("Accept-Charset", encoding);
            method.setRequestHeader("Referer", referer);
//            method.setRequestHeader("Connection", "close");

            // method
            // .setRequestHeader(
            // "User-Agent",
            // "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.19)
            // Gecko/2010031422 Firefox/3.0.19 ( .NET CLR 3.5.30729)");
            // method
            // .setRequestHeader("Accept",
            // "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            //
            // method.setRequestHeader("Accept-Language", "zh-cn,zh;q=0.5");
            // method.setRequestHeader("Accept-Encoding", "gzip,deflate");
            // method.setRequestHeader("Accept-Charset",
            // "gb2312,utf-8;q=0.7,*;q=0.7");
            //
            // method.setRequestHeader("Keep-Alive", "300");
            // method.setRequestHeader("Connection", "keep-alive");
            // method.setRequestHeader("Referer", "http://weibo.com/");
            //
            // method.setRequestHeader("Content-Type",
            // "application/x-www-form-urlencoded");
            //
            // org.apache.commons.httpclient.Cookie newCookie = new
            // org.apache.commons.httpclient.Cookie();
            // newCookie.setDomain("sina.com.cn");
            // newCookie.setName("Apache");
            // newCookie.setValue("116.231.88.137.257731307035639536");
            // newCookie.setPath("/");
            // httpClient.getState().addCookie(newCookie);

            if (!Validate.isEmpty(cookieList)) {
                for (Cookie cookie : cookieList) {
                    httpClient.getState().addCookie(cookie);
                }
            }

            // 116.231.88.137.257731307035639536
            // Cookie Apache=115.172.83.28.796971307024489563
            // Host login.sina.com.cn

            // // 使用系统提供的默认的恢复策略
            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
            method.getParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
            method.getParams().setParameter("http.protocol.single-cookie-header", true);
            if (!Validate.isEmpty(params)) {
                method.setRequestBody(params);
            }
            if (null != requestEntity) {
                method.setRequestEntity(requestEntity);
            }
            // logPostRequest(method);

            int statusCode = httpClient.executeMethod(method);
            // System.out.println("statusCode:" + statusCode);
            String responseStr = "";

            String acceptEncoding = "";
            if (method.getResponseHeader("Content-Encoding") != null) {
                acceptEncoding = method.getResponseHeader("Content-Encoding").getValue();
            }
            if (acceptEncoding.toLowerCase().indexOf("gzip") > -1) {
                InputStream is = method.getResponseBodyAsStream();
                GZIPInputStream gzin = new GZIPInputStream(is);
                InputStreamReader isr = new InputStreamReader(gzin, encoding); // 设置读取流的编码格式，自定义编码
                java.io.BufferedReader br = new java.io.BufferedReader(isr);
                StringBuffer sb = new StringBuffer();
                String tempbf;
                while ((tempbf = br.readLine()) != null) {
                    sb.append(tempbf);
                }
                isr.close();
                gzin.close();
                responseStr = sb.toString();
            } else {
                // 2013-01-07 ake 注释
                // responseStr =
                // readInputStream(method.getResponseBodyAsStream(), encoding);
                // 2013-01-07 ake 添加
                responseStr = readInputStream(method, encoding);

            }
            // logPostResponse(method, responseStr);
            if (!needRedirect) {
                return responseStr;
            }
            if (method.getResponseHeader("Location") != null) {
                if (method.getResponseHeader("Location").getValue().startsWith("http")) {
                    redirectUrl = method.getResponseHeader("Location").getValue();
                    return doGet(redirectUrl, encoding, actionUrl, false, cookieList);
                } else {
                    redirectUrl = "http://" + getResponseHost(method) + "/" + method.getResponseHeader("Location").getValue();
                    return doGet(redirectUrl, encoding, actionUrl, false, cookieList);
                }
            } else {
                if (statusCode != 200) {
                    throw new RuntimeException("访问网络资源出现问题，错误码为： " + statusCode);
                }
                return responseStr;
            }
        } catch (HttpException e) {
            // 发生致命的异常，可能是协议不对或者返回的内容有问题
            throw new RuntimeException("访问资源出现问题，请检查请求的url是否正确", e);
        } catch (IOException e) {
            // 发生网络异常
            throw new RuntimeException("网络出现问题，请检查网络是否正常", e);
        } catch (Exception e) {
            // 发生网络异常
            throw new RuntimeException("网络出现问题，请检查网络是否正常", e);
        } finally {
            if (null != method) {
                method.releaseConnection();
            }
        }
    }

    public String doGet(String url, String encoding, String referer, boolean needUrlEncode, List<Cookie> cookieList) {
        if (null == encoding || encoding.trim().length() == 0) {
            encoding = "utf-8";
        }

        if (encoding.trim().equalsIgnoreCase("gb2312")) {
            encoding = "gbk";
        }

        if (url.startsWith("https:")) {
            this.supportSSL(url);
        }
        String responseStr = null;
        GetMethod method = null;
        try {
            // httpClient.getParams().setContentCharset(encoding);
            // httpClient.getParams().setHttpElementCharset(encoding);
            // httpClient.getParams().setUriCharset("GBK");

            // method.getParams().setUriCharset("gbk");

            // if (useUri) {
            // method = new GetMethod();
            // method.setURI(new URI(url, false, encoding));
            // } else {
            // method = new GetMethod(url);
            // }
            if (needUrlEncode) {
                String encodeUrl = StringUtils.getEncodeUrl(url, encoding);
                method = new GetMethod(encodeUrl);
            } else {
                method = new GetMethod(url);
            }

            setHeaders(method, referer);
            method.setRequestHeader("Accept-Charset", encoding);
//            method.setRequestHeader("Connection", "close");

            // 使用系统提供的默认的恢复策略
            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
            method.getParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
            method.getParams().setParameter("http.protocol.single-cookie-header", true);

            // method
            // .setRequestHeader(
            // "User-Agent",
            // "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.19)
            // Gecko/2010031422 Firefox/3.0.19 ( .NET CLR 3.5.30729)");
            // method
            // .setRequestHeader("Accept",
            // "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            //
            // method.setRequestHeader("Accept-Language", "zh-cn,zh;q=0.5");
            // method.setRequestHeader("Accept-Encoding", "gzip,deflate");
            // method.setRequestHeader("Accept-Charset",
            // "gb2312,utf-8;q=0.7,*;q=0.7");
            //
            // method.setRequestHeader("Keep-Alive", "300");
            // method.setRequestHeader("Connection", "keep-alive");
            // method.setRequestHeader("Referer", "http://weibo.com/");

            // logGetRequest(method);
            // 设置httpclient可以发送请求到相同的url上

            // 例如：
            // http://t.sina.com.cn/attention/att_list.php?action=0&uid=1229143114&page=8
            httpClient.getParams().setBooleanParameter(HttpClientParams.ALLOW_CIRCULAR_REDIRECTS, true);
            httpClient.getParams().setIntParameter(HttpClientParams.MAX_REDIRECTS, 5);

            // httpClient.getParams().setBooleanParameter(HttpClientParams.ALLOW_CIRCULAR_REDIRECTS,
            // false);

            // org.apache.commons.httpclient.Cookie newCookie = new
            // org.apache.commons.httpclient.Cookie();
            // newCookie.setDomain("sina.com.cn");
            // newCookie.setName("Apache");
            // newCookie.setValue("116.231.88.137.257731307035639536");
            // newCookie.setPath("/");
            // httpClient.getState().addCookie(newCookie);
            if (!Validate.isEmpty(cookieList)) {
                for (Cookie cookie : cookieList) {
                    httpClient.getState().addCookie(cookie);
                }
            }

            int statusCode = httpClient.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                if (statusCode == HttpStatus.SC_NOT_FOUND) {
                    throw new RuntimeException("404 Not Found");
                }
                throw new HttpException("出错了，错误码为：" + statusCode);
            }
            String acceptEncoding = "";
            if (method.getResponseHeader("Content-Encoding") != null) {
                acceptEncoding = method.getResponseHeader("Content-Encoding").getValue();
            }
            if (acceptEncoding.toLowerCase().indexOf("gzip") > -1) {
                method.setRequestHeader("Accept-Encoding", "gzip, deflate");
            }

            Header htmlHeader = method.getResponseHeader("Content-Type");
            if (htmlHeader != null) {
                String contentType = method.getResponseHeader("Content-Type").getValue();
                // 读取内容 docType，没有text javascript标示就不获取文件流
                if (!(contentType.toLowerCase().indexOf("text") != -1 || contentType.toLowerCase().indexOf("javascript") != -1 || contentType.toLowerCase().indexOf("json") != -1 || contentType
                        .toLowerCase().indexOf("image/jpeg") != -1)) {
                    method.abort();
                    return responseStr;
                }
            }
            if (acceptEncoding.toLowerCase().indexOf("gzip") > -1) {
                InputStream is = method.getResponseBodyAsStream();
                GZIPInputStream gzin = new GZIPInputStream(is);
                InputStreamReader isr = new InputStreamReader(gzin, encoding); // 设置读取流的编码格式，自定义编码
                java.io.BufferedReader br = new java.io.BufferedReader(isr);
                StringBuffer sb = new StringBuffer();
                String tempbf;
                while ((tempbf = br.readLine()) != null) {
                    sb.append(tempbf);
                }
                isr.close();
                gzin.close();
                responseStr = sb.toString();
            } else {
                // byteData = method.getResponseBody();

                // is = method.getResponseBodyAsStream();
                // responseStr = readInputStream(is, encoding);
                // 2013-01-07 ake 注释
                // responseStr =
                // readInputStream(method.getResponseBodyAsStream(), encoding);
                // 2013-01-07 ake 添加
                responseStr = readInputStream(method, encoding);
            }
            // logGetResponse(method, responseStr);

            if (method.getResponseHeader("Location") != null) {
                if (method.getResponseHeader("Location").getValue().startsWith("http")) {
                    redirectUrl = method.getResponseHeader("Location").getValue();
                    return doGet(redirectUrl, encoding, referer, false, cookieList);
                } else {
                    redirectUrl = "http://" + getResponseHost(method) + method.getResponseHeader("Location").getValue();
                    return doGet(redirectUrl, encoding, referer, false, cookieList);
                }
            }
            // else if(responseStr.indexOf("http-equiv=\"refresh\"")!=-1)
            // {
            // redirectUrl = StringUtilss.substringBetween(responseStr, "url=",
            // "\"");
            // return doGet(redirectUrl, encoding);
            // }
            else {
                if (statusCode != 200) {
                    throw new RuntimeException("访问网络资源出现问题，错误码为： " + statusCode);
                }
                return responseStr;
            }

        } catch (HttpException e) {
            // 发生致命的异常，可能是协议不对或者返回的内容有问题
            throw new RuntimeException("访问 url：" + url + " 出现异常：" + e.getMessage(), e);
        } catch (IOException e) {
            // 发生网络异常
            throw new RuntimeException("网络出现问题，请检查网络是否正常", e);
        } catch (Exception e) {
            // 发生网络异常
            throw new RuntimeException("网络出现问题，请检查网络是否正常", e);
        } finally {
            if (null != method) {
                method.releaseConnection();
            }
        }
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public byte[] doGetBytes(String url, String encoding, String referer, boolean needUrlEncode) {
        byte[] byteData = null;
        if (null == encoding || encoding.trim().length() == 0) {
            encoding = "utf-8";
        }

        if (encoding.trim().equalsIgnoreCase("gb2312")) {
            encoding = "gbk";
        }

        if (url.startsWith("https:")) {
            this.supportSSL(url);
        }
        GetMethod method = null;
        try {
            // httpClient.getParams().setContentCharset(encoding);
            // httpClient.getParams().setHttpElementCharset(encoding);
            // httpClient.getParams().setUriCharset("GBK");

            // method.getParams().setUriCharset("gbk");

            // if (useUri) {
            // method = new GetMethod();
            // method.setURI(new URI(url, false, encoding));
            // } else {
            // method = new GetMethod(url);
            // }
            if (needUrlEncode) {
                String encodeUrl = StringUtils.getEncodeUrl(url, encoding);
                method = new GetMethod(encodeUrl);
            } else {
                method = new GetMethod(url);
            }

            setHeaders(method, referer);
            method.setRequestHeader("Accept-Charset", encoding);
//            method.setRequestHeader("Connection", "close");

            // 使用系统提供的默认的恢复策略
            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
            method.getParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
            method.getParams().setParameter("http.protocol.single-cookie-header", true);

            // method
            // .setRequestHeader(
            // "User-Agent",
            // "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.19)
            // Gecko/2010031422 Firefox/3.0.19 ( .NET CLR 3.5.30729)");
            // method
            // .setRequestHeader("Accept",
            // "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            //
            // method.setRequestHeader("Accept-Language", "zh-cn,zh;q=0.5");
            // method.setRequestHeader("Accept-Encoding", "gzip,deflate");
            // method.setRequestHeader("Accept-Charset",
            // "gb2312,utf-8;q=0.7,*;q=0.7");
            //
            // method.setRequestHeader("Keep-Alive", "300");
            // method.setRequestHeader("Connection", "keep-alive");
            // method.setRequestHeader("Referer", "http://weibo.com/");

            // logGetRequest(method);
            // 设置httpclient可以发送请求到相同的url上

            // 例如：
            // http://t.sina.com.cn/attention/att_list.php?action=0&uid=1229143114&page=8
            httpClient.getParams().setBooleanParameter(HttpClientParams.ALLOW_CIRCULAR_REDIRECTS, true);
            httpClient.getParams().setIntParameter(HttpClientParams.MAX_REDIRECTS, 5);

            // httpClient.getParams().setBooleanParameter(HttpClientParams.ALLOW_CIRCULAR_REDIRECTS,
            // false);

            // org.apache.commons.httpclient.Cookie newCookie = new
            // org.apache.commons.httpclient.Cookie();
            // newCookie.setDomain("sina.com.cn");
            // newCookie.setName("Apache");
            // newCookie.setValue("116.231.88.137.257731307035639536");
            // newCookie.setPath("/");
            // httpClient.getState().addCookie(newCookie);

            int statusCode = httpClient.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                if (statusCode == HttpStatus.SC_NOT_FOUND) {
                    throw new RuntimeException("404 Not Found");
                }
                throw new HttpException("出错了，错误码为：" + statusCode);
            }
            String acceptEncoding = "";
            if (method.getResponseHeader("Content-Encoding") != null) {
                acceptEncoding = method.getResponseHeader("Content-Encoding").getValue();
            }
            if (acceptEncoding.toLowerCase().indexOf("gzip") > -1) {
                method.setRequestHeader("Accept-Encoding", "gzip, deflate");
            }

            if (acceptEncoding.toLowerCase().indexOf("gzip") > -1) {
                InputStream is = method.getResponseBodyAsStream();
                GZIPInputStream gzin = new GZIPInputStream(is);
                InputStreamReader isr = new InputStreamReader(gzin, encoding); // 设置读取流的编码格式，自定义编码
                java.io.BufferedReader br = new java.io.BufferedReader(isr);

                StringBuffer sb = new StringBuffer();
                String tempbf;
                while ((tempbf = br.readLine()) != null) {
                    sb.append(tempbf);
                }
                isr.close();
                gzin.close();
                byteData = sb.toString().getBytes();
            } else {
                byteData = method.getResponseBody();

            }
            // logGetResponse(method, responseStr);

            if (statusCode != 200) {
                throw new RuntimeException("访问网络资源出现问题，错误码为： " + statusCode);
            }
            return byteData;

        } catch (HttpException e) {
            // 发生致命的异常，可能是协议不对或者返回的内容有问题
            throw new RuntimeException("访问 url：" + url + " 出现异常：" + e.getMessage(), e);
        } catch (IOException e) {
            // 发生网络异常
            throw new RuntimeException("网络出现问题，请检查网络是否正常", e);
        } catch (Exception e) {
            // 发生网络异常
            throw new RuntimeException("网络出现问题，请检查网络是否正常", e);
        } finally {
            if (null != method) {
                method.releaseConnection();
            }
        }
    }

    /**
     * 设置Http请求的Header
     * 
     * @param method Http Method
     */
    public void setHeaders(HttpMethod method, String refer) {
        method.setRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml,application/json,text/javascript,*/*;");
        method.setRequestHeader("Accept-Language", "zh-cn");
        // method.setRequestHeader("User-Agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; SV1; QQDownload 1.7; Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1) ; .NET CLR 1.1.4322; CIBA)");

        // method.setRequestHeader(
        // "User-Agent",
        // "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; SV1; QQDownload 1.7; Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1) ; .NET CLR 1.1.4322; CIBA)");

        // method.setRequestHeader("User-Agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 718; Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1) ; CIBA; .NET CLR 2.0.50727; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; .NET4.0C; .NET4.0E)");

        // method.setRequestHeader("User-Agent",
        // "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.97 Safari/537.11");

        // method.setRequestHeader("Accept-Encoding", "gzip, deflate");

        method.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.89 Safari/537.36");
        // 和讯专用
        // method.setRequestHeader("User-Agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; SV1;");

        // method.setRequestHeader("Keep-Alive", "300");
        // method.setRequestHeader("Connection", "Keep-Alive");
        method.setRequestHeader("Cache-Control", "no-cache");

        method.setRequestHeader("Referer", refer);
        // method.setRequestHeader("x-requested-with", "XMLHttpRequest");
    }

    /**
     * 将输入流按照特定的编码转换成字符串
     * 
     * @param is 输入流
     * @param encoding
     * @return 字符串
     * @throws IOException
     */
    private String readInputStream(InputStream is, String encoding) throws IOException {

        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] b = new byte[max_bytes];
        StringBuilder builder = new StringBuilder();
        int bytesRead = 0;
        while (true) {
            bytesRead = is.read(b, 0, max_bytes);
            if (bytesRead == -1) {
                builder.append(new String(swapStream.toByteArray(), encoding));
                return builder.toString();
            }
            swapStream.write(b, 0, bytesRead);
        }
    }

    /**
     * 获取页面html内容
     * 
     * @param method
     * @param methodType
     * @return String
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    private static String readInputStream(HttpMethod method, String encoding) throws Exception {
        String methodcharset = "iso-8859-1";
        String sticketContent = "";
        if (method instanceof PostMethod) {
            methodcharset = ((PostMethod) method).getResponseCharSet();
        } else {
            methodcharset = ((GetMethod) method).getResponseCharSet();
        }
        // byte[] bytes = method.getResponseBody();
        // String body = new String(bytes,"UTF-8");
        InputStream inputStream = method.getResponseBodyAsStream();

        // byte[] bytes = IOUtils.toByteArray(inputStream);
        // String sticketContent = IOUtils.toString(inputStream, oldcharset);
        // String newCharset = WebUtil.getCharSetByBody(sticketContent,
        // oldcharset);
        // if (!newCharset.equalsIgnoreCase(oldcharset)) {
        // sticketContent = new String(sticketContent.getBytes(oldcharset),
        // newCharset);
        // }
        // // return new String(bytes, charset);

        if (methodcharset.equalsIgnoreCase("iso-8859-1")) {
            sticketContent = IOUtils.toString(inputStream, methodcharset);
            String newCharset = StringUtils.getCharSetByBody(sticketContent, "");

            if (Validate.isEmpty(newCharset)) // 页面内容编码为空，则采用规则设置的编码
            {
                sticketContent = new String(sticketContent.getBytes(methodcharset), encoding);
            } else {
                // 不为空，且内容中的编码和头部的不一样就采用内容中的
                if (!newCharset.equalsIgnoreCase(methodcharset)) {
                    sticketContent = new String(sticketContent.getBytes(methodcharset), newCharset);
                }
            }
        } else {
            sticketContent = IOUtils.toString(inputStream, methodcharset);
        }

        return sticketContent;

    }

    /**
     * 获取页面html内容
     * 
     * @param method
     * @param methodType
     * @return String
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    private static String readInputStream(HttpMethod method) throws Exception {
        String sticketContent = "";
        String oldcharset = "";
        if (method instanceof PostMethod) {
            oldcharset = ((PostMethod) method).getResponseCharSet();
        } else {
            oldcharset = ((GetMethod) method).getResponseCharSet();
        }
        InputStream inputStream = method.getResponseBodyAsStream();
        if (oldcharset.equalsIgnoreCase("iso-8859-1")) {
            sticketContent = IOUtils.toString(inputStream, oldcharset);
            String newCharset = StringUtils.getCharSetByBody(sticketContent, oldcharset);

            if (Validate.isEmpty(newCharset)) {
                sticketContent = new String(sticketContent.getBytes(oldcharset), "UTF-8");
            } else {
                if (!newCharset.equalsIgnoreCase(oldcharset)) {
                    sticketContent = new String(sticketContent.getBytes(oldcharset), newCharset);
                }
            }
        } else {
            sticketContent = IOUtils.toString(inputStream, oldcharset);
        }

        return sticketContent;

    }

    /**
     * 将Header[]转换为String
     * 
     * @param headers Http头信息
     * @return String形式的Http头信息
     */
    private String getHeadersStr(Header[] headers) {
        StringBuilder builder = new StringBuilder();
        for (Header header : headers) {
            builder.append(header.getName()).append(": ").append(header.getValue()).append("\n");
        }
        return builder.toString();
    }

    /**
     * 获取Post应答消息的Host
     * 
     * @param method Post方法
     * @return 应答消息的Host
     * @throws URIException
     */
    private String getResponseHost(HttpMethod method) throws URIException {
        String url = method.getURI().toString();
        return url.split("/")[2];
    }

    /**
     * 得到String形式的cookie
     * 
     * @return String形式的cookie
     */
    private String getCookieStr() {
        Cookie[] cookies = httpClient.getState().getCookies();
        StringBuilder builder = new StringBuilder();
        for (Cookie cookie : cookies) {
            builder.append(cookie.getDomain()).append(":").append(cookie.getName()).append("=").append(cookie.getValue()).append(";").append(cookie.getPath()).append(";")
                    .append(cookie.getExpiryDate()).append(";").append(cookie.getSecure()).append(";\n");
        }
        return builder.toString();
    }

    /**
     * 得到String形式的post请求体
     * 
     * @param postValues Post请求的键值对
     * @return String形式的post请求体
     */
    private String getPostBody(NameValuePair[] postValues) {
        StringBuilder builder = new StringBuilder();
        for (NameValuePair pair : postValues) {
            builder.append(pair.getName()).append(": ").append(pair.getValue()).append("\n");
        }
        return builder.toString();
    }

    public static void main(String[] args) {

        HttpClientHelper curClient = HttpClientHelper.getInstance(true);

        NameValuePair[] params = new NameValuePair[] { new NameValuePair("keyword", "银行") };
        curClient.setProxy("61.129.51.28", 9001);
        for (int i = 0; i < 100; i++) {
            try {
                long t1 = System.currentTimeMillis();
                String content = curClient.doGet("http://news.soso.com/n.q?pid=n.ftx.page&ch=n.search.active&ty=c&sd=6&st=t&qc=0&usort=on&interval=1369238400-1369324800&pg=11&w=收款", "utf-8", "",
                        false, null);
                long t2 = System.currentTimeMillis();
                // System.out.println(content);
                long t3 = (t2 - t1);
                System.out.println("耗时：" + (t3 / 1000) + " s, " + t3 + " ms");

                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // String content = curClient.doPost("http://bbs.zgjrw.com/search.aspx",
        // params );
        // String content =
        // curClient.doGet("http://bbs.zgjrw.com/search.aspx?keyword=银行","utf-8");

        // // curClient.setProxy("61.129.251.155", 9001);
        // // curClient.setProxy("61.129.251.161", 9001);
        // // curClient.setProxy("61.129.251.33", 9001);//no
        // // curClient.setProxy("61.152.116.34", 9001);//yes
        // // curClient.setProxy("192.168.1.89", 9001);//yes
        // // curClient.setProxy("118.126.18.81", 9001);//yes

        String str = "<meta charset=\"utf-8\" />";
        // String str =
        // "http-equiv=\"Content-Type\" content=\"text/html;chArset=gb2312\"ssss";
        String charset = StringUtils.getCharSetByBody(str, "utf-8");
        // String charset = getCharSet(str);
        System.out.println(charset);

        // String str =
        // "http-equiv=\"Content-Type\" content=\"text/html;chArset=gb2312\"ssss";
        // String charset = getCharSet(str);
        // System.out.println(charset);

        // String keyword = "银行";
        // String encodeKeyword;
        // try {
        // encodeKeyword = URLEncoder.encode(keyword,"utf-8");
        // String crawlSiteUrl =
        // "http://tags.21cn.com/files/tags_list/1,"+encodeKeyword+",1610,3,20.shtml";
        // String respStr;
        // //// // respStr = curClient.doGet("http://www.baidu.com");
        // //// NameValuePair[] params = new NameValuePair[] { new
        // NameValuePair() };
        // //// // respStr =
        // curClient.doPost("http://www.baidu.com",params,"gbk");
        // respStr = curClient.doGet(crawlSiteUrl, "gbk","");
        // System.out.println(respStr);
        // } catch (UnsupportedEncodingException e) {
        // e.printStackTrace();
        // }

        // System.out.println(java.net.URLDecoder.decode("%E9%93%B6%E8%A1%8C",
        // "utf-8"));

    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getSoTimeout() {
        return soTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public int getMaxConnectionsPerHost() {
        return maxConnectionsPerHost;
    }

    public void setMaxConnectionsPerHost(int maxConnectionsPerHost) {
        this.maxConnectionsPerHost = maxConnectionsPerHost;
    }

    public int getMaxTotalConnections() {
        return maxTotalConnections;
    }

    public void setMaxTotalConnections(int maxTotalConnections) {
        this.maxTotalConnections = maxTotalConnections;
    }

    // public byte[] getByteData() {
    // return byteData;
    // }
    //
    // public void setByteData(byte[] byteData) {
    // this.byteData = byteData;
    // }

    public InputStream getIs() {
        return is;
    }

    public void setIs(InputStream is) {
        this.is = is;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

}

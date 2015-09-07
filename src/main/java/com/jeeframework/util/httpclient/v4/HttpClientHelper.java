package com.jeeframework.util.httpclient.v4;


import com.jeeframework.core.exception.BaseException;
import com.jeeframework.logicframework.biz.exception.BizException;
import com.jeeframework.util.httpclient.proxy.SiteProxyIp;
import com.jeeframework.util.regx.RegxUtils;
import com.jeeframework.util.validate.Validate;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

public class HttpClientHelper {
    private static Logger logger = Logger.getLogger(HttpClientHelper.class.getName());

    /**
     * http error, IO error retry times
     */
    private static final int retry_times = 3;

    // 连接超时时间（默认10秒 10000ms） 单位毫秒（ms）
    private int connectionTimeout = 15000;

    // 读取数据超时时间（默认30秒 30000ms） 单位毫秒（ms）
    private int soTimeout = 30000;

    // 每个主机的最大并行链接数，默认为2
    private int maxConnectionsPerHost = 20;

    // 客户端总并行链接最大数，默认为20
    private int maxTotalConnections = 256;

    // 一次性从数据流读取的最大byte数
    private final static int max_bytes = 4096;

    private byte[] byteData;

    private CloseableHttpClient httpClient = null;

    private String redirectUrl = "";// 重定向url

    private HttpClientContext context = HttpClientContext.create();

    public HttpClientHelper() {
        // SSL context for secure connections can be created either based on
        // system or application specific properties.
        SSLContext sslContext = null;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

                @Override
                public boolean isTrusted(final X509Certificate[] arg0, final String arg1)
                        throws CertificateException {

                    return true;
                }
            }).build();
        } catch (Exception e) {
            throw new BaseException("can not create http client.", e);
        }
        // Use custom hostname verifier to customize SSL hostname verification.
        X509HostnameVerifier hostnameVerifier = new AllowAllHostnameVerifier();//new BrowserCompatHostnameVerifier();

        // Create a registry of custom connection socket factories for supported
        // protocol schemes.
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslContext, hostnameVerifier))
                .build();
        HttpClientConnectionManager connManager = null;

        SocketConfig socketConfig = SocketConfig.custom()
                .setTcpNoDelay(true)
                .build();

//        if (isMulti) {
//
            connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            ((PoolingHttpClientConnectionManager)connManager).setMaxTotal(maxTotalConnections);
            ((PoolingHttpClientConnectionManager)connManager).setDefaultMaxPerRoute(maxConnectionsPerHost);
            ((PoolingHttpClientConnectionManager)connManager).setDefaultSocketConfig(socketConfig);
//        } else {
//        httpClient.close();
//        connManager = new BasicHttpClientConnectionManager(socketFactoryRegistry);
//        ((BasicHttpClientConnectionManager) connManager).setSocketConfig(socketConfig);


//        }

        //设置重试3次
        httpClient = HttpClients.custom()
                .setConnectionManager(connManager).setRetryHandler(new DefaultHttpRequestRetryHandler(3, false)).setRedirectStrategy(new LaxRedirectStrategy())
                .build();

        CookieStore cookieStore = new BasicCookieStore();


        context.setCookieStore(cookieStore);

    }

//    public static synchronized HttpClientHelper getInstance() {
//        if (instance == null) {
//            instance = new HttpClientHelper();
//        }
//        return instance;
//    }

    public HttpResponse doGet(String url, String requestEncoding, String responseEncoding,
                              Map<String, String> headerMap, SiteProxyIp proxyIp)
            throws HttpException, IOException {
        int retryTimes = 0;
        while (true) {
            try {
                return doGetWrapper(url, requestEncoding, responseEncoding, headerMap, proxyIp);
            } catch (IOException e) {
                retryTimes++;
                if (retryTimes == retry_times) {
                    throw e;
                }
            }
        }
    }

    private HttpResponse doGetWrapper(String url, String requestEncoding, String responseEncoding, Map<String, String> headerMap, SiteProxyIp proxyIp) throws HttpException, IOException {

        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setRequestEncode(requestEncoding);
        httpResponse.setResponseEncode(responseEncoding);
        httpResponse.setRequestUrl(url);

        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY)//.BEST_MATCH
                .setExpectContinueEnabled(true)
                .setStaleConnectionCheckEnabled(true)
                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                .build();

        RequestConfig.Builder requestConfigBuilder = RequestConfig.copy(defaultRequestConfig);
        requestConfigBuilder = requestConfigBuilder
                .setSocketTimeout(soTimeout)
                .setConnectTimeout(connectionTimeout)
                .setConnectionRequestTimeout(connectionTimeout);

        if (proxyIp != null) {
            requestConfigBuilder.setProxy(new HttpHost(proxyIp.getHost(), proxyIp.getPort()));
        }

        RequestConfig requestConfig = requestConfigBuilder.build();


        if (null == requestEncoding || requestEncoding.trim().length() == 0) {
            requestEncoding = "utf-8";
        }

        if (requestEncoding.trim().equalsIgnoreCase("gb2312")) {
            requestEncoding = "gbk";
        }

//        if (url.startsWith("https:")) {
//            this.supportSSL(url);
//        }
        String responseContent = null;
        HttpGet httpget = null;
        try {

            if (!Validate.isEmpty(requestEncoding)) {
                String encodeUrl = URLUtil.getEncodeUrl(url, requestEncoding);
                httpget = new HttpGet(encodeUrl);
            } else {
                httpget = new HttpGet(url);
            }

            //List<NameValuePair> params
//            String str = EntityUtils.toString(new UrlEncodedFormEntity(params));
//            httpget.setURI(new URI(httpget.getURI().toString() + "?" + str));

            httpget.setConfig(requestConfig);

//            setHeaders(httpget, referer, requestEncoding);

            List<Header> headers = getHeaders(headerMap);

            headers.add(new BasicHeader("Accept-Charset", requestEncoding));

            Header[] headersArray = new Header[headers.size()];
            headersArray = headers.toArray(headersArray);

            httpget.setHeaders(headersArray);

            // 设置httpclient可以发送请求到相同的url上
            CloseableHttpResponse response = httpClient.execute(httpget, context);
            HttpEntity responseEntity = null;

            try {
                responseEntity = response.getEntity();

                HttpRequest currentReq = (HttpRequest) context.getAttribute(
                        HttpCoreContext.HTTP_REQUEST);

                HttpHost currentHost = (HttpHost) context.getAttribute(
                        HttpCoreContext.HTTP_TARGET_HOST);
//                String currentUrl = (currentReq.getURI().isAbsolute()) ? currentReq.getURI().toString() : (currentHost.toURI() + currentReq.getURI());

                String currentUrl = currentHost.toURI() + currentReq.getRequestLine().getUri();

                httpResponse.setTargetUrl(currentUrl);

                StatusLine statusLine = response.getStatusLine();

                int statusCode = statusLine.getStatusCode();

                httpResponse.setStatusCode(statusCode);

                if (statusCode != HttpStatus.SC_OK) {
                    if (statusCode == HttpStatus.SC_NOT_FOUND) {
                        throw new BaseException("404 Not Found");
                    }
                    throw new HttpException("出错了，错误码为：" + statusCode);
                }


                if (responseEntity != null) {
                    InputStream inStream = responseEntity.getContent();
                    ContentType contentType = ContentType.getOrDefault(responseEntity);
                    Charset charset = contentType.getCharset();


                    try {
                        Header contentEncodingHeader = responseEntity.getContentEncoding();

                        String acceptEncoding = contentEncodingHeader != null ? contentEncodingHeader.getValue() : null;

                        if (acceptEncoding != null && acceptEncoding.toLowerCase().indexOf("gzip") > -1) {
                            GZIPInputStream gzin = new GZIPInputStream(inStream);
                            try {
                                responseContent = readStreamToString(charset, gzin);
                            } finally {
                                if (gzin != null) {
                                    gzin.close();
                                }
                            }

                        } else {
                            responseContent = readStreamToString(charset, inStream);
                        }
                        // do something useful with the response
                    } catch (IOException ex) {
                        // In case of an IOException the connection will be released
                        // back to the connection manager automatically
                        throw ex;
                    } finally {
                        // Closing the input stream will trigger connection release
                        if (inStream != null) {
                            inStream.close();
                        }
                    }
                } else {
                    if (statusCode != 200) {
                        throw new BizException("访问网络资源出现问题，错误码为： " + statusCode);
                    }

                    return httpResponse;
                }
            } finally {
                if (responseEntity != null) {
                    EntityUtils.consume(responseEntity);
                }
                if (response != null) {
                    response.close();
                }
            }

        } catch (HttpException e) {
            throw e;
        } catch (ClientProtocolException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            httpResponse.setContent(responseContent);
//            httpClient.close();
            //如果 httpclient 有登录，关闭了登录状态就没有了
        }

        return httpResponse;

    }

    public HttpResponse doGetBytes(String url, String requestEncoding, String responseEncoding,
                                   Map<String, String> headerMap, SiteProxyIp proxyIp)
            throws HttpException, IOException {
        int retryTimes = 0;
        while (true) {
            try {
                return doGetBytesWrapper(url, requestEncoding, responseEncoding, headerMap, proxyIp);
            } catch (IOException e) {
                retryTimes++;
                if (retryTimes == retry_times) {
                    throw e;
                }
            }
        }
    }

    private HttpResponse doGetBytesWrapper(String url, String requestEncoding, String responseEncoding, Map<String, String> headerMap, SiteProxyIp proxyIp) throws HttpException, IOException {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setRequestEncode(requestEncoding);
        httpResponse.setResponseEncode(responseEncoding);
        httpResponse.setRequestUrl(url);

        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.BEST_MATCH)
                .setExpectContinueEnabled(true)
                .setStaleConnectionCheckEnabled(true)
                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                .build();

        RequestConfig.Builder requestConfigBuilder = RequestConfig.copy(defaultRequestConfig);
        requestConfigBuilder = requestConfigBuilder
                .setSocketTimeout(soTimeout)
                .setConnectTimeout(connectionTimeout)
                .setConnectionRequestTimeout(connectionTimeout);

        if (proxyIp != null) {
            requestConfigBuilder.setProxy(new HttpHost(proxyIp.getHost(), proxyIp.getPort()));
        }

        RequestConfig requestConfig = requestConfigBuilder.build();


        if (null == requestEncoding || requestEncoding.trim().length() == 0) {
            requestEncoding = "utf-8";
        }

        if (requestEncoding.trim().equalsIgnoreCase("gb2312")) {
            requestEncoding = "gbk";
        }

//        if (url.startsWith("https:")) {
//            this.supportSSL(url);
//        }
        byte[] responseBytes = null;
        HttpGet httpget = null;
        try {

            if (!Validate.isEmpty(requestEncoding)) {
                String encodeUrl = URLUtil.getEncodeUrl(url, requestEncoding);
                httpget = new HttpGet(encodeUrl);
            } else {
                httpget = new HttpGet(url);
            }

            //List<NameValuePair> params
//            String str = EntityUtils.toString(new UrlEncodedFormEntity(params));
//            httpget.setURI(new URI(httpget.getURI().toString() + "?" + str));

            httpget.setConfig(requestConfig);

//            setHeaders(httpget, referer, requestEncoding);

            List<Header> headers = getHeaders(headerMap);

            headers.add(new BasicHeader("Accept-Charset", requestEncoding));

            Header[] headersArray = new Header[headers.size()];
            headersArray = headers.toArray(headersArray);

            httpget.setHeaders(headersArray);

            // 设置httpclient可以发送请求到相同的url上
            CloseableHttpResponse response = httpClient.execute(httpget, context);
            HttpEntity responseEntity = null;

            try {
                responseEntity = response.getEntity();

                HttpRequest currentReq = (HttpRequest) context.getAttribute(
                        HttpCoreContext.HTTP_REQUEST);

                HttpHost currentHost = (HttpHost) context.getAttribute(
                        HttpCoreContext.HTTP_TARGET_HOST);
//                String currentUrl = (currentReq.getURI().isAbsolute()) ? currentReq.getURI().toString() : (currentHost.toURI() + currentReq.getURI());

                String currentUrl = currentHost.toURI() + currentReq.getRequestLine().getUri();
                httpResponse.setTargetUrl(currentUrl);

                StatusLine statusLine = response.getStatusLine();

                int statusCode = statusLine.getStatusCode();

                httpResponse.setStatusCode(statusCode);

                if (statusCode != HttpStatus.SC_OK) {
                    if (statusCode == HttpStatus.SC_NOT_FOUND) {
                        throw new BaseException("404 Not Found");
                    }
                    throw new HttpException("出错了，错误码为：" + statusCode);
                }


                if (responseEntity != null) {
                    InputStream inStream = responseEntity.getContent();
                    ContentType contentType = ContentType.getOrDefault(responseEntity);
                    Charset charset = contentType.getCharset();


                    try {
                        Header contentEncodingHeader = responseEntity.getContentEncoding();

                        String acceptEncoding = contentEncodingHeader != null ? contentEncodingHeader.getValue() : null;

                        if (acceptEncoding != null && acceptEncoding.toLowerCase().indexOf("gzip") > -1) {
                            GZIPInputStream gzin = new GZIPInputStream(inStream);
                            try {
                                responseBytes = readStreamToBytes(charset, gzin);
                            } finally {
                                if (gzin != null) {
                                    gzin.close();
                                }
                            }

                        } else {
                            responseBytes = readStreamToBytes(charset, inStream);
                        }
                        // do something useful with the response
                    } catch (IOException ex) {
                        // In case of an IOException the connection will be released
                        // back to the connection manager automatically
                        throw ex;
                    } finally {
                        // Closing the input stream will trigger connection release
                        if (inStream != null) {
                            inStream.close();
                        }
                    }
                } else {
                    if (statusCode != 200) {
                        throw new BizException("访问网络资源出现问题，错误码为： " + statusCode);
                    }

                    return httpResponse;
                }
            } finally {
                if (responseEntity != null) {
                    EntityUtils.consume(responseEntity);
                }
                if (response != null) {
                    response.close();
                }
            }

        } catch (HttpException e) {
            throw e;
        } catch (ClientProtocolException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            httpResponse.setContentBytes(responseBytes);
            httpResponse.setContentType(HttpResponse.CONTENT_TYPE_BYTE);
//            httpClient.close();
            //如果 httpclient 有登录，关闭了登录状态就没有了
        }

        return httpResponse;
    }


    public HttpResponse doPost(String url, Map<String, String> postData, String requestEncoding,
                               String responseEncoding, Map<String, String> headerMap, SiteProxyIp proxyIp)
            throws HttpException, IOException {
        int retryTimes = 0;
        while (true) {
            try {
                return doPostWrapper(url, postData, requestEncoding, responseEncoding, headerMap, proxyIp);
            } catch (IOException e) {
                retryTimes++;
                if (retryTimes == retry_times) {
                    throw e;
                }
            }
        }
    }

    private HttpResponse doPostWrapper(String url, Map<String, String> postData, String requestEncoding, String responseEncoding, Map<String, String> headerMap, SiteProxyIp proxyIp) throws HttpException, IOException {

        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setRequestEncode(requestEncoding);
        httpResponse.setResponseEncode(responseEncoding);
        httpResponse.setRequestUrl(url);

        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.BEST_MATCH)
                .setExpectContinueEnabled(true)
                .setStaleConnectionCheckEnabled(true)
                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                .build();

        RequestConfig.Builder requestConfigBuilder = RequestConfig.copy(defaultRequestConfig);
        requestConfigBuilder = requestConfigBuilder
                .setSocketTimeout(soTimeout)
                .setConnectTimeout(connectionTimeout)
                .setConnectionRequestTimeout(connectionTimeout);

        if (proxyIp != null) {
            requestConfigBuilder.setProxy(new HttpHost(proxyIp.getHost(), proxyIp.getPort()));
        }

        RequestConfig requestConfig = requestConfigBuilder.build();


//        if (null == requestEncoding || requestEncoding.trim().length() == 0) {
//            requestEncoding = "utf-8";
//        }

        if (requestEncoding.trim().equalsIgnoreCase("gb2312")) {
            requestEncoding = "gbk";
        }

//        if (url.startsWith("https:")) {
//            this.supportSSL(url);
//        }
        String responseStr = null;
        HttpPost httpPost = null;
        try {

            httpPost = new HttpPost(url);

            httpPost.setConfig(requestConfig);

            List<Header> headers = getHeaders(headerMap);

            if (!Validate.isEmpty(requestEncoding)) {
                headers.add(new BasicHeader("Accept-Charset", requestEncoding));
            }

            Header[] headersArray = new Header[headers.size()];
            headersArray = headers.toArray(headersArray);

            httpPost.setHeaders(headersArray);

            List<NameValuePair> params = getPairs(postData);
            if (!Validate.isEmpty(params)) {
                if (!Validate.isEmpty(requestEncoding)) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, requestEncoding));
                } else {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }
            }


            // 设置httpclient可以发送请求到相同的url上
            CloseableHttpResponse response = httpClient.execute(httpPost, context);
            HttpEntity responseEntity = null;

            try {
                responseEntity = response.getEntity();

                HttpRequest currentReq = (HttpRequest) context.getAttribute(
                        HttpCoreContext.HTTP_REQUEST);

                HttpHost currentHost = (HttpHost) context.getAttribute(
                        HttpCoreContext.HTTP_TARGET_HOST);
//                String currentUrl = (currentReq.getURI().isAbsolute()) ? currentReq.getURI().toString() : (currentHost.toURI() + currentReq.getURI());

                String currentUrl = currentHost.toURI() + currentReq.getRequestLine().getUri();
                httpResponse.setTargetUrl(currentUrl);

                StatusLine statusLine = response.getStatusLine();

                int statusCode = statusLine.getStatusCode();

                httpResponse.setStatusCode(statusCode);
                if (statusCode != HttpStatus.SC_OK) {
                    if (statusCode == HttpStatus.SC_NOT_FOUND) {
                        throw new BaseException("404 Not Found");
                    }
                    throw new HttpException("出错了，错误码为：" + statusCode);
                }


                if (responseEntity != null) {
                    InputStream inStream = responseEntity.getContent();
                    ContentType contentType = ContentType.getOrDefault(responseEntity);
                    Charset charset = contentType.getCharset();


                    try {
                        Header contentEncodingHeader = responseEntity.getContentEncoding();

                        String acceptEncoding = contentEncodingHeader != null ? contentEncodingHeader.getValue() : null;

                        if (acceptEncoding != null && acceptEncoding.toLowerCase().indexOf("gzip") > -1) {
                            GZIPInputStream gzin = new GZIPInputStream(inStream);
                            try {
                                responseStr = readStreamToString(charset, gzin);
                            } finally {
                                if (gzin != null) {
                                    gzin.close();
                                }
                            }

                        } else {
                            responseStr = readStreamToString(charset, inStream);
                        }
                        // do something useful with the response
                    } catch (IOException ex) {
                        // In case of an IOException the connection will be released
                        // back to the connection manager automatically
                        throw ex;
                    } finally {
                        // Closing the input stream will trigger connection release
                        if (inStream != null) {
                            inStream.close();
                        }
                    }
                } else {
                    if (statusCode != 200) {
                        throw new BizException("访问网络资源出现问题，错误码为： " + statusCode);
                    }
                    return httpResponse;
                }
            } finally {
                if (responseEntity != null) {
                    EntityUtils.consume(responseEntity);
                }
                if (response != null) {
                    response.close();
                }
            }

        } catch (HttpException e) {
            throw e;
        } catch (ClientProtocolException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            httpResponse.setContent(responseStr);
//            httpClient.close();
            //如果 httpclient 有登录，关闭了登录状态就没有了
        }
        return httpResponse;
    }

    public HttpResponse doPostMultiPart(String url, Map<String, ContentBody> postData, String requestEncoding,
                                        String responseEncoding, Map<String, String> headerMap, SiteProxyIp proxyIp)
            throws HttpException, IOException {
        int retryTimes = 0;
        while (true) {
            try {
                return doPostMultiPartWrapper(url, postData, requestEncoding, responseEncoding, headerMap, proxyIp);
            } catch (IOException e) {
                retryTimes++;
                if (retryTimes == retry_times) {
                    throw e;
                }
            }
        }
    }

    private HttpResponse doPostMultiPartWrapper(String url, Map<String, ContentBody> postData, String requestEncoding, String responseEncoding, Map<String, String> headerMap, SiteProxyIp proxyIp) throws HttpException, IOException {

        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setRequestEncode(requestEncoding);
        httpResponse.setResponseEncode(responseEncoding);
        httpResponse.setRequestUrl(url);

        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.BEST_MATCH)
                .setExpectContinueEnabled(true)
                .setStaleConnectionCheckEnabled(true)
                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                .build();

        RequestConfig.Builder requestConfigBuilder = RequestConfig.copy(defaultRequestConfig);
        requestConfigBuilder = requestConfigBuilder
                .setSocketTimeout(soTimeout)
                .setConnectTimeout(connectionTimeout)
                .setConnectionRequestTimeout(connectionTimeout);

        if (proxyIp != null) {
            requestConfigBuilder.setProxy(new HttpHost(proxyIp.getHost(), proxyIp.getPort()));
        }

        RequestConfig requestConfig = requestConfigBuilder.build();


//        if (null == requestEncoding || requestEncoding.trim().length() == 0) {
//            requestEncoding = "utf-8";
//        }

        if (requestEncoding.trim().equalsIgnoreCase("gb2312")) {
            requestEncoding = "gbk";
        }

//        if (url.startsWith("https:")) {
//            this.supportSSL(url);
//        }
        String responseStr = null;
        HttpPost httpPost = null;
        try {

            httpPost = new HttpPost(url);

            httpPost.setConfig(requestConfig);

            List<Header> headers = getHeaders(headerMap);

            if (!Validate.isEmpty(requestEncoding)) {
                headers.add(new BasicHeader("Accept-Charset", requestEncoding));
            }

            Header[] headersArray = new Header[headers.size()];
            headersArray = headers.toArray(headersArray);

            httpPost.setHeaders(headersArray);

            // 以浏览器兼容模式运行，防止文件名乱码。
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE);


            Set<Map.Entry<String, ContentBody>> entrySet = postData.entrySet();
            int i = 0;
            for (Map.Entry<String, ContentBody> entry : entrySet) {
                multipartEntityBuilder.addPart(entry.getKey(), entry.getValue());
                i++;
            }


            if (i > 0) {
                if (!Validate.isEmpty(requestEncoding)) {
                    multipartEntityBuilder.setCharset(CharsetUtils.get(requestEncoding));
                }
                HttpEntity requestEntity = multipartEntityBuilder.build();
                httpPost.setEntity(requestEntity);
            }

            // 设置httpclient可以发送请求到相同的url上
            CloseableHttpResponse response = httpClient.execute(httpPost, context);
            HttpEntity responseEntity = null;

            try {
                responseEntity = response.getEntity();

                HttpRequest currentReq = (HttpRequest) context.getAttribute(
                        HttpCoreContext.HTTP_REQUEST);

                HttpHost currentHost = (HttpHost) context.getAttribute(
                        HttpCoreContext.HTTP_TARGET_HOST);
//                String currentUrl = (currentReq.getURI().isAbsolute()) ? currentReq.getURI().toString() : (currentHost.toURI() + currentReq.getURI());

                String currentUrl = currentHost.toURI() + currentReq.getRequestLine().getUri();
                httpResponse.setTargetUrl(currentUrl);

                StatusLine statusLine = response.getStatusLine();

                int statusCode = statusLine.getStatusCode();

                httpResponse.setStatusCode(statusCode);
                if (statusCode != HttpStatus.SC_OK) {
                    if (statusCode == HttpStatus.SC_NOT_FOUND) {
                        throw new BaseException("404 Not Found");
                    }
                    throw new HttpException("出错了，错误码为：" + statusCode);
                }


                if (responseEntity != null) {
                    InputStream inStream = responseEntity.getContent();
                    ContentType contentType = ContentType.getOrDefault(responseEntity);
                    Charset charset = contentType.getCharset();


                    try {
                        Header contentEncodingHeader = responseEntity.getContentEncoding();

                        String acceptEncoding = contentEncodingHeader != null ? contentEncodingHeader.getValue() : null;

                        if (acceptEncoding != null && acceptEncoding.toLowerCase().indexOf("gzip") > -1) {
                            GZIPInputStream gzin = new GZIPInputStream(inStream);
                            try {
                                responseStr = readStreamToString(charset, gzin);
                            } finally {
                                if (gzin != null) {
                                    gzin.close();
                                }
                            }

                        } else {
                            responseStr = readStreamToString(charset, inStream);
                        }
                        // do something useful with the response
                    } catch (IOException ex) {
                        // In case of an IOException the connection will be released
                        // back to the connection manager automatically
                        throw ex;
                    } finally {
                        // Closing the input stream will trigger connection release
                        if (inStream != null) {
                            inStream.close();
                        }
                    }
                } else {
                    if (statusCode != 200) {
                        throw new BizException("访问网络资源出现问题，错误码为： " + statusCode);
                    }
                    return httpResponse;
                }
            } finally {
                if (responseEntity != null) {
                    EntityUtils.consume(responseEntity);
                }
                if (response != null) {
                    response.close();
                }
            }

        } catch (HttpException e) {
            throw e;
        } catch (ClientProtocolException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            httpResponse.setContent(responseStr);
//            httpClient.close();
            //如果 httpclient 有登录，关闭了登录状态就没有了
        }
        return httpResponse;
    }

    public HttpResponse doPostString(String url, String stringEntity, String requestEncoding,
                               String responseEncoding, Map<String, String> headerMap, SiteProxyIp proxyIp)
            throws HttpException, IOException {
        int retryTimes = 0;
        while (true) {
            try {
                return doPostStringWrapper(url, stringEntity, requestEncoding, responseEncoding, headerMap, proxyIp);
            } catch (IOException e) {
                retryTimes++;
                if (retryTimes == retry_times) {
                    throw e;
                }
            }
        }
    }

    private HttpResponse doPostStringWrapper(String url, String stringEntity, String requestEncoding, String responseEncoding, Map<String, String> headerMap, SiteProxyIp proxyIp) throws HttpException, IOException {

        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setRequestEncode(requestEncoding);
        httpResponse.setResponseEncode(responseEncoding);
        httpResponse.setRequestUrl(url);

        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.BEST_MATCH)
                .setExpectContinueEnabled(true)
                .setStaleConnectionCheckEnabled(true)
                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                .build();

        RequestConfig.Builder requestConfigBuilder = RequestConfig.copy(defaultRequestConfig);
        requestConfigBuilder = requestConfigBuilder
                .setSocketTimeout(soTimeout)
                .setConnectTimeout(connectionTimeout)
                .setConnectionRequestTimeout(connectionTimeout);

        if (proxyIp != null) {
            requestConfigBuilder.setProxy(new HttpHost(proxyIp.getHost(), proxyIp.getPort()));
        }

        RequestConfig requestConfig = requestConfigBuilder.build();


//        if (null == requestEncoding || requestEncoding.trim().length() == 0) {
//            requestEncoding = "utf-8";
//        }

        if (requestEncoding.trim().equalsIgnoreCase("gb2312")) {
            requestEncoding = "gbk";
        }

//        if (url.startsWith("https:")) {
//            this.supportSSL(url);
//        }
        String responseStr = null;
        HttpPost httpPost = null;
        try {

            httpPost = new HttpPost(url);

            httpPost.setConfig(requestConfig);

            List<Header> headers = getHeaders(headerMap);

            if (!Validate.isEmpty(requestEncoding)) {
                headers.add(new BasicHeader("Accept-Charset", requestEncoding));
            }

            Header[] headersArray = new Header[headers.size()];
            headersArray = headers.toArray(headersArray);

            httpPost.setHeaders(headersArray);
            StringEntity s = new StringEntity(stringEntity);
            httpPost.setEntity(s);


            // 设置httpclient可以发送请求到相同的url上
            CloseableHttpResponse response = httpClient.execute(httpPost, context);
            HttpEntity responseEntity = null;

            try {
                responseEntity = response.getEntity();

                HttpRequest currentReq = (HttpRequest) context.getAttribute(
                        HttpCoreContext.HTTP_REQUEST);

                HttpHost currentHost = (HttpHost) context.getAttribute(
                        HttpCoreContext.HTTP_TARGET_HOST);
//                String currentUrl = (currentReq.getURI().isAbsolute()) ? currentReq.getURI().toString() : (currentHost.toURI() + currentReq.getURI());

                String currentUrl = currentHost.toURI() + currentReq.getRequestLine().getUri();
                httpResponse.setTargetUrl(currentUrl);

                StatusLine statusLine = response.getStatusLine();

                int statusCode = statusLine.getStatusCode();

                httpResponse.setStatusCode(statusCode);
                if (statusCode != HttpStatus.SC_OK) {
                    if (statusCode == HttpStatus.SC_NOT_FOUND) {
                        throw new BaseException("404 Not Found");
                    }
                    throw new HttpException("出错了，错误码为：" + statusCode);
                }


                if (responseEntity != null) {
                    InputStream inStream = responseEntity.getContent();
                    ContentType contentType = ContentType.getOrDefault(responseEntity);
                    Charset charset = contentType.getCharset();


                    try {
                        Header contentEncodingHeader = responseEntity.getContentEncoding();

                        String acceptEncoding = contentEncodingHeader != null ? contentEncodingHeader.getValue() : null;

                        if (acceptEncoding != null && acceptEncoding.toLowerCase().indexOf("gzip") > -1) {
                            GZIPInputStream gzin = new GZIPInputStream(inStream);
                            try {
                                responseStr = readStreamToString(charset, gzin);
                            } finally {
                                if (gzin != null) {
                                    gzin.close();
                                }
                            }

                        } else {
                            responseStr = readStreamToString(charset, inStream);
                        }
                        // do something useful with the response
                    } catch (IOException ex) {
                        // In case of an IOException the connection will be released
                        // back to the connection manager automatically
                        throw ex;
                    } finally {
                        // Closing the input stream will trigger connection release
                        if (inStream != null) {
                            inStream.close();
                        }
                    }
                } else {
                    if (statusCode != 200) {
                        throw new BizException("访问网络资源出现问题，错误码为： " + statusCode);
                    }
                    return httpResponse;
                }
            } finally {
                if (responseEntity != null) {
                    EntityUtils.consume(responseEntity);
                }
                if (response != null) {
                    response.close();
                }
            }

        } catch (HttpException e) {
            throw e;
        } catch (ClientProtocolException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            httpResponse.setContent(responseStr);
//            httpClient.close();
            //如果 httpclient 有登录，关闭了登录状态就没有了
        }
        return httpResponse;
    }

    private static List<Header> getHeaders(Map<String, String> headersMap) {
        List<Header> headers = new ArrayList<Header>();

        Map<String, String> headersMapTmp = new HashMap<String, String>();
        headersMapTmp.put("Accept", "text/html,application/xhtml+xml,application/xml,application/json,text/javascript,*/*;");
        headersMapTmp.put("Accept-Language", "zh-cn");

        headersMapTmp.put("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.97 Safari/537.11");

//        headersMapTmp.put("Cache-Control", "no-cache");
//        headers.add(new BasicHeader("Referer", refer));
//        headers.add(new BasicHeader("Accept-Charset", requestEncoding));
//        headersMapTmp.put("Connection", "close");


        if (null != headersMap && false == headersMap.isEmpty()) {
            headersMapTmp.putAll(headersMap);
        }

        Set<Map.Entry<String, String>> entrySet = headersMapTmp.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            headers.add(new BasicHeader(entry.getKey(), entry.getValue()));
        }

        return headers;
    }

    private static List<NameValuePair> getPairs(Map<String, String> postData) {
        if (null == postData || postData.isEmpty()) {
            return null;
        }

        Set<Map.Entry<String, String>> entrySet = postData.entrySet();
        int dataLength = entrySet.size();
//        NameValuePair[] pairs = new NameValuePair[dataLength];
        List<NameValuePair> pairs = new ArrayList<NameValuePair>(dataLength);
        int i = 0;
        for (Map.Entry<String, String> entry : entrySet) {
//            pairs[i++] = new BasicNameValuePair(entry.getKey(), entry.getValue());
            pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return pairs;
    }

    public static byte[] readStreamToBytes(Charset charsetObj, InputStream inputStream) throws IOException {

        byte[] htmlBytes = null;
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        try {
            String responseStr;

            byte[] b = new byte[max_bytes];
            int bytesRead = 0;
            while (true) {
                bytesRead = inputStream.read(b, 0, max_bytes);
                if (bytesRead == -1) {
                    break;
                }
                swapStream.write(b, 0, bytesRead);
            }
            htmlBytes = swapStream.toByteArray();
            return htmlBytes;

        } finally {
            if (swapStream != null) {
                swapStream.close();
            }
        }
    }

    public static String readStreamToString(Charset charsetObj, InputStream inputStream) throws IOException {
        byte[] htmlBytes = readStreamToBytes(charsetObj, inputStream);
        String charSet = getHtmlCharset(htmlBytes, charsetObj);
        String responseStr = new String(htmlBytes, charSet);
        return responseStr;
    }

    /**
     * 根据html的bytes内容反推网页编码格式
     *
     * @param htmlBytes
     * @param charsetObj
     */
    private static String getHtmlCharset(byte[] htmlBytes, Charset charsetObj) {
        String charSet = "UTF-8";
        ;// 如果头部中没有，那么我们需要 查看页面源码，这个方法虽然不能说完全正确，因为有些粗糙的网页编码者没有在页面中写头部编码信息
        if (charsetObj == null || Validate.isEmpty(charsetObj.name())) {

            String regEx = "(?=<meta).*?(?<=charset=[\\'|\\\"]?)([[a-z]|[A-Z]|[0-9]|-]*)";

            String htmlTmp = new String(htmlBytes);

            String charSetTmp = RegxUtils.matchRegxWithPrefix(htmlTmp, regEx, false);

            if (Validate.isEmpty(charSetTmp)) {
                regEx = "<meta.*charset=[\\'|\\\"]?([[a-z]|[A-Z]|[0-9]|-]*).*>";
                charSetTmp = RegxUtils.matchRegxWithPrefix(htmlTmp, regEx, false);
            }

            if (!Validate.isEmpty(charSetTmp)) {
                charSet = charSetTmp;
            }
        }
        return charSet;
    }

    /**
     * 设置Http请求的Header
     *
     * @param httpMethod Http Method
     */
//    public void setHeaders(HttpRequestBase httpMethod, String refer, String requestEncoding) {
//        headers.add(new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml,application/json,text/javascript,*/*;");
//        headers.add(new BasicHeader("Accept-Language", "zh-cn");
//
//        if (!Validate.isEmpty(refer)) {
//            headers.add(new BasicHeader("User-Agent", refer);
//        } else {
//            headers.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.97 Safari/537.11");
//        }
//
//        headers.add(new BasicHeader("Cache-Control", "no-cache");
//        headers.add(new BasicHeader("Referer", refer);
//        headers.add(new BasicHeader("Accept-Charset", requestEncoding);
//        headers.add(new BasicHeader("Connection", "close");
//    }


    /**
     * 将输入流按照特定的编码转换成字符串
     *
     * @param is       输入流
     * @param encoding
     * @return 字符串
     * @throws IOException
     */
    private String readInputStream(InputStream is, Charset encoding) throws IOException {

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

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }


    public static void main(String[] args) throws Exception {
        testDoGet();

    }

    private static void testDoGet() throws Exception {
        HttpClientHelper httpClientHelper = new HttpClientHelper();
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("X-Requested-With", "com.JEEFRAMEWORK.mm");
        headerMap.put("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.3; zh-cn; Galaxy Nexus Build /JWR66Y) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30 MicroMessenger/5.3.1.51_r733746.462 NetType/WIFI");
        HttpResponse htmlContent = httpClientHelper.doGet("http://mp.weixin.qq.com/s?__biz=MzA3ODQ1NjYyOQ==&mid=201970220&idx=1&sn=8301e46b6a2684ecf46a32c2cb310463&3rd=MzA3MDU4NTYzMw==&scene=6#rd", "utf-8", "utf-8", headerMap, null);

        System.out.println(htmlContent.getContent());
    }

    private void testDoPost() throws Exception {
        HttpClientHelper httpClientHelper = new HttpClientHelper();
        Map<String, String> headerMap = new HashMap<String, String>();

        headerMap.put("Referer", "http://www.discuz.net/search.php?mod=portal");

        Map<String, String> postDatarMap = new HashMap<String, String>();

        postDatarMap.put("srchtxt", "a");
        postDatarMap.put("searchsubmit", "yes");
        HttpResponse htmlContent = httpClientHelper.doPost("http://www.discuz.net/search.php?mod=portal", postDatarMap, "utf-8", "utf-8", headerMap, null);

    }
}

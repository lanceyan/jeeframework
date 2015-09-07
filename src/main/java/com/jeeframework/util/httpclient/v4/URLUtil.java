/*
 * @project: hyfaycrawler 
 * @package: com.hyfaycrawler.util.crawler.common
 * @title:   CrawlerUtil.java 
 *
 * Copyright (c) 2014 Hyfay Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.util.httpclient.v4;

import com.jeeframework.util.validate.Validate;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

/**
 * 抓取类的通用方法函数
 *
 * @author lance
 * @version 1.0 2014/6/30 10:26
 */
public class URLUtil {

    /**
     * 获取网页编码
     *
     * @param pageEncodeParam
     * @return
     */
    public static String getPageEncode(int pageEncodeParam) {
        String pageEncode = "UTF-8";
        if (pageEncodeParam == 1) {
            pageEncode = "GB2312";
        } else if (pageEncodeParam == 2) {
            pageEncode = "UTF-8";
        } else if (pageEncodeParam == 3) {
            pageEncode = "GBK";
        } else if (pageEncodeParam == 4) {
            pageEncode = "iso-8859-1";
        } else if (pageEncodeParam == 5) {
            pageEncode = "euc-jp";
        }
        return pageEncode;
    }

    /**
     * 根据子url获取Url的全部,拼接 http://url/1/2/3/1.html ./sdfsdf.jsp 为
     * http://url/1/2/3/./sdfsdf.jsp 拼接 http://url/1/2/3/1.html /fff.jsp 为
     * http://url/fff.jsp
     *
     * @param oriFullUrl
     * @param subUrl
     * @return
     */
    public static String getFullUrlBySubUrl(String oriFullUrl, String subUrl) {
        String retStr = "";
        if (Validate.isEmpty(oriFullUrl)) {
            return retStr;
        }
        if (Validate.isEmpty(subUrl)) {
            return oriFullUrl;
        }
        if (subUrl.startsWith("http")) {
            return subUrl;
        }

        if (Validate.isUrl(oriFullUrl)) {
            // 在原始url有效的情况下，分两种情况 1、点号开头 2、斜线开头
            if (subUrl.startsWith("/")) {
                try {
                    URL aURL = new URL(oriFullUrl);

                    String host = aURL.getHost();
                    String protocol = aURL.getProtocol();

                    retStr = protocol + "://" + host + subUrl;

                } catch (MalformedURLException e) {
                    throw new RuntimeException("url格式错误", e);
                }

            } else { // if (subUrl.startsWith(".")) 有可能有点号，有可能没有点号，都是按照这种方式
                int lastSlashPos = oriFullUrl.lastIndexOf("/");
                if (lastSlashPos != -1) {
                    oriFullUrl = oriFullUrl.substring(0, lastSlashPos + 1);
                    retStr = oriFullUrl + subUrl;
                }
            }

        }
        return retStr;

    }

    public static String getEncodeUrl(String urlString, String encode) {

        StringBuilder buf = new StringBuilder();

        // byte[] rawdata = URLCodec.encodeUrl(URI.allowed_fragment,
        // EncodingUtil.getBytes(urlString, encode));
        // return new
        // String(EncodingUtil.getAsciiString(rawdata).toCharArray());

        int questIndex = urlString.indexOf('?');
        if (questIndex == -1) {
            return urlString;
        }
        String url = urlString.substring(0, questIndex);
        String queryString = urlString.substring(questIndex + 1,
                urlString.length());

        Map<String, String[]> paramMap = getParamsMap(queryString, encode);

        buf.append(url);

        if (paramMap.size() > 0) {
            buf.append("?");
            for (Map.Entry<String, String[]> e : paramMap.entrySet()) {
                buf.append(e.getKey()).append("=")
                        .append(StringUtils.join(e.getValue(), ","))
                        .append("&");
            }
            buf.deleteCharAt(buf.length() - 1);
        }

        return buf.toString();
    }

    private static Map<String, String[]> getParamsMap(String queryString,
                                                      String enc) {
        Map<String, String[]> paramsMap = new HashMap<String, String[]>();
        if (queryString != null && queryString.length() > 0) {
            int ampersandIndex, lastAmpersandIndex = 0;
            String subStr, param, value;
            String[] paramPair, values, newValues;
            do {
                ampersandIndex = queryString.indexOf('&', lastAmpersandIndex) + 1;
                if (ampersandIndex > 0) {
                    subStr = queryString.substring(lastAmpersandIndex,
                            ampersandIndex - 1);
                    lastAmpersandIndex = ampersandIndex;
                } else {
                    subStr = queryString.substring(lastAmpersandIndex);
                }
                // ticket=ST-MjEwMTQwNzI3MA==-1328862542-xd-BE27A54218FA11939D0AEB97CADD0440
                int i = subStr.indexOf("=");
                if (i >= 0) {
                    param = subStr.substring(0, i);
                    value = (subStr.length() - 1) == i ? "" : subStr
                            .substring(i + 1);
                } else {
                    param = subStr;
                    value = "";
                }
                /*
                 * paramPair = subStr.split("="); param = paramPair[0]; value =
				 * paramPair.length == 1 ? "" : paramPair[1];
				 */
                if (!Validate.isEmpty(enc)) {
                    try {
                        value = URLEncoder.encode(value, enc);
                    } catch (UnsupportedEncodingException ignored) {
                    }
                }
                if (paramsMap.containsKey(param)) {
                    values = paramsMap.get(param);
                    int len = values.length;
                    newValues = new String[len + 1];
                    System.arraycopy(values, 0, newValues, 0, len);
                    newValues[len] = value;
                } else {
                    newValues = new String[]{value};
                }
                paramsMap.put(param, newValues);
            } while (ampersandIndex > 0);
        }
        return paramsMap;
    }


    /**
     * @param requestUrl   请求的url
     * @param encode      url的参数需要编码字符集
     * @param urlParamMap  url中的参数和值
     * @return
     */
    final static String DOLLAR_TOKEN = "$";
    final static String AT_TOKEN = "@";
    final static String PAGE_TOKEN = "page";
    final static String AT_PAGE_TOKEN = "@page@";
//    final static String SPLIT_SYMBOL = "$@\t\r\n ";

    public static String buildUrlWithParameters(String requestUrl, String encode, Map<String, String> urlParameterMap, Map<String, String> queryParameterMap,  String curPageNo) {

        StringBuffer newUrlBuffer = new StringBuffer();
        String newURL = buildParamURL(requestUrl, encode, urlParameterMap,   curPageNo);

        newUrlBuffer.append(newURL);
        //先拼接URL中的参数，在拼接URL后面？的参数
        List<String> queryList = new ArrayList<String>();

        if(!Validate.isEmpty(queryParameterMap)) {
            for (Map.Entry<String, String> queryParam : queryParameterMap.entrySet()) {
                String key = queryParam.getKey();
                String value = queryParam.getValue();
                key = key.trim();
                value = value.trim();
                if (value.equals(AT_PAGE_TOKEN) && !Validate.isEmpty(curPageNo)) {
                    value = curPageNo;
                }

                queryList.add(key.trim() + "=" + value.trim());
            }
        }
        if (!Validate.isEmpty(queryList)) {
            if (newUrlBuffer.indexOf("?") > 0) {
                if (!newUrlBuffer.toString().endsWith("&")) {
                    newUrlBuffer.append("&");
                }
            } else {
                newUrlBuffer.append("?");
            }
            newUrlBuffer.append(StringUtils.join(queryList, "&"));
        }

        return newUrlBuffer.toString();
    }

    public static String buildParamURL(String requestUrl, String encode, Map<String, String> urlParameterMap,   String curPageNo) {
        StringBuffer newUrlBuffer = parseURL(requestUrl, encode, DOLLAR_TOKEN,  urlParameterMap, curPageNo);

        newUrlBuffer = parseURL(newUrlBuffer.toString(), encode, AT_TOKEN,  urlParameterMap, curPageNo);
        return newUrlBuffer.toString();
    }

    public static StringBuffer parseURL(String requestUrl, String encode, String tokenSymbol,   Map<String, String> urlParamMap, String curPageNo) {

        StringTokenizer parser = new StringTokenizer(requestUrl, tokenSymbol, true);
        StringBuffer newUrlBuffer = new StringBuffer();

        String token = null;
        String lastToken = null;
        String curToken = null;
        String nextToken = null;
        while (parser.hasMoreTokens()) {

            nextToken = parser.nextToken();
            token = nextToken;
            if (tokenSymbol.equals(lastToken)) {

                if (tokenSymbol.equals(token)) {
                    if (!Validate.isEmpty(lastToken)) {
                        newUrlBuffer.append(lastToken);
                    }
                    newUrlBuffer.append(token);
                    lastToken = null;
                } else {

//                    if (!parser.hasMoreTokens()) {
//                        throw new BizException(tokenSymbol + "不配对");
//                    }
                    curToken = token;
                    String tokenTemp = "";
                    if (parser.hasMoreTokens()) {
                        tokenTemp = parser.nextToken();
                    }
                    if (!tokenSymbol.equals(tokenTemp)) {
                        newUrlBuffer.append(tokenSymbol + curToken + tokenTemp);
                    } else {

                        //URL里面有@page@替换
                        if (curToken.equals(PAGE_TOKEN) && !Validate.isEmpty(curPageNo)) {
                            newUrlBuffer.append(curPageNo);
                        } else {

                            Object fieldValue = null;
                            if (urlParamMap != null && urlParamMap.size() > 0) {
                                fieldValue = urlParamMap.remove(curToken);
                            }

                            if (fieldValue == null) {
                                newUrlBuffer.append(tokenSymbol + curToken + tokenSymbol);
                            } else {
                                if (encode != null) {
                                    try {
                                        String fieldVal = fieldValue + "";
                                        fieldValue = URLEncoder.encode(fieldVal, encode);
                                    } catch (UnsupportedEncodingException e) {
                                    }
                                }
                                newUrlBuffer.append(fieldValue);
                            }

                        }
                    }
                    lastToken = token;
                }

                nextToken = null;

            } else {
                if (!tokenSymbol.equals(token)) {
                    newUrlBuffer.append(token);
                    nextToken = null;
                }
                lastToken = token;
            }

        }
        if (!Validate.isEmpty(nextToken)) {
            newUrlBuffer.append(nextToken);
        }
        return newUrlBuffer;
    }


}

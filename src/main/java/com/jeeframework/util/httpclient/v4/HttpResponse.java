/*
 * @project: hyfaycrawler 
 * @package: com.hyfaycrawler.util.crawler.client
 * @title:   Response.java 
 *
 * Copyright (c) 2014 Hyfay Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.util.httpclient.v4;

/**
 * httpclient 请求数据的返回类
 *
 * @author lance
 * @version 1.0 2014/7/11 18:00
 */
public class HttpResponse {
    private String requestUrl; //原始请求的URL
    private String targetUrl; //经过请求重定向的URL
    private int statusCode;  //http请求返回的状态码
    private String requestEncode; //响应的编码格式
    private String responseEncode;//请求的编码格式
    private String content; //http请求的内容
    private int contentType;  //http请求返回的状态码
    private byte[] contentBytes;

    private String plainTextContent; //去除html,\r\n\t后的内容


    public static final int CONTENT_TYPE_STRING = 0;
    public static final int CONTENT_TYPE_BYTE = 1;

    public String getPlainTextContent() {
        return plainTextContent;
    }

    public void setPlainTextContent(String plainTextContent) {
        this.plainTextContent = plainTextContent;
    }

    public byte[] getContentBytes() {
        return contentBytes;
    }

    public void setContentBytes(byte[] contentBytes) {
        this.contentBytes = contentBytes;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getRequestEncode() {
        return requestEncode;
    }

    public void setRequestEncode(String requestEncode) {
        this.requestEncode = requestEncode;
    }


    public String getResponseEncode() {
        return responseEncode;
    }

    public void setResponseEncode(String responseEncode) {
        this.responseEncode = responseEncode;
    }
}

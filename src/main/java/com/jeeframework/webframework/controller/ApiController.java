/**
 * @project: Jeeframework
 * @Title: ApiController.java
 * @Package: com.jeeframework.webframework.controller
 * <p>
 * Copyright (c) 2014-2017 Jeeframework Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.webframework.controller;

import com.jeeframework.util.httpclient.HttpClientHelper;
import com.jeeframework.util.httpclient.HttpResponse;
import com.mangofactory.swagger.annotations.ApiIgnore;
import org.apache.http.HttpException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 接口文档访问入口
 * <p/>
 *
 * @Description: 通过api.html访问文档页面
 * @author lance
 * @version 1.0 2015-2-26 上午09:39:15
 */
@Controller
@ApiIgnore
public class ApiController {


    private final static String COLON = ":";
    private final static String API_DOC = "/api-docs";

    @RequestMapping("/api.html")
    public void api(HttpServletRequest request, HttpServletResponse response) throws IOException, HttpException {
        //获取url地址
        String reqUrl = "http://" + request.getServerName() + COLON + request.getServerPort() + "/api";// request.getRequestURL().toString();
//        logger.info("api页面请求：" + reqUrl);
        reqUrl = reqUrl.replace("api", "api") + "/index.html";
        HttpClientHelper httpClientHelper = new HttpClientHelper();
        HttpResponse httpResponse = httpClientHelper.doGet(reqUrl, "UTF-8", "UTF-8", null, null);
        //api url
        String apiUrl = request.getServerName() + COLON + request.getServerPort() + API_DOC;

        String html = httpResponse.getContent();
        //处理html页面内容,使其可以访问静态资源
        String body = html.replace("petstore.swagger.io/v2/swagger.json", apiUrl)
                .replace("css/", "/api/css/")
                .replace("lib/", "/api/lib/")
                .replace("swagger-ui.js", "/api/swagger-ui.js");
        response.getWriter().write(body);
        response.getWriter().flush();
    }


}

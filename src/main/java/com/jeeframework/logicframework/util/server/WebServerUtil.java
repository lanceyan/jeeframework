/*
 * @project: webdemo 
 * @package: com.jeeframework.logicframework.util.server
 * @title:   WebServerUtil.java 
 *
 * Copyright (c) 2016 JeeFrameWork.com Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.logicframework.util.server;

import com.jeeframework.util.validate.Validate;

import java.io.File;
import java.security.ProtectionDomain;

/**
 * web服务器相关的工具类
 *
 * @author lance
 * @version 1.0 2016-03-08 19:01
 */
public class WebServerUtil {

    /**
     * 查找web工程的根目录
     *
     * @return
     */
    public static String findWebDescriptor() {
        //开发环境获取到工程根目录
        String devProjectDir = System.getProperty("user.dir");
        if (!Validate.isEmpty(devProjectDir)) {
            devProjectDir = devProjectDir + File.separator + "src" + File.separator + "main" + File.separator + "webapp";
            String webDescriptor = devProjectDir + File.separator + "WEB-INF" + File.separator + "web.xml";
            File webDescFile = new File(webDescriptor);
            if (webDescFile.exists()) {
                return webDescriptor;
            }
        }

        //部署运行环境，根据class判断文件路径
        ProtectionDomain protectionDomain = WebServerUtil.class.getProtectionDomain();//getClass 是Object类的方法
        String path = protectionDomain.getCodeSource().getLocation().getPath();

        if (path.contains("WEB-INF")) {
            String web_inf_path = path.substring(0, path.indexOf("WEB-INF") + 7);
            String webDescriptor = web_inf_path + File.separator + "web.xml";
            File webDescFile = new File(webDescriptor);
            if (webDescFile.exists()) {
                return webDescriptor;
            }
        }
        return null;
    }

}

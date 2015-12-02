/**
 * @project: with 
 * @Title: SessionUtils.java 
 * @Package: com.webdemo.util.cache
 *
 * Copyright (c) 2014-2014 Transing Limited, Inc.
 * All rights reserved.
 * 
 */
package com.jeeframework.webdemo.util.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO 简单描述
 * <p>
 * 
 * @Description: TODO 详细描述
 * @author TODO
 * @version 1.0 2015-3-2 上午11:05:09
 */
public class SessionUtils {

    private static Map<String, String> checkCodeMap = new HashMap<String, String>();//校验码缓存

    public static String getCheckCode(String mobile) {
        return checkCodeMap.get(mobile);
    }

    public static String putCheckCode(String mobile, String checkCode) {
        return checkCodeMap.put(mobile, checkCode);
    }

}

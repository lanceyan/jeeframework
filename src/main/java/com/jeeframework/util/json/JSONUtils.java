/*
 * @project: com.jeeframework 
 * @package: com.jeeframework.util.json
 * @title:   JSONUtils.java 
 *
 * Copyright (c) 2017 jeeframework Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.util.json;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * json操作工具类
 *
 * @author lance
 * @version 1.0 2017-02-14 13:45
 */
public class JSONUtils {
    /**
     * 是否是有效的json格式
     * @param jsonInString
     * @return
     */
    public static boolean isJSONValid(String jsonInString) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(jsonInString);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}

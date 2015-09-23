/*
 * @project: jeeframework 
 * @package: com.jeeframework.webframework.xwork2
 * @title:   SecurityRequestWrapper.java 
 *
 * Copyright (c) 2015 Hyfay Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.webframework.xwork2;

import com.jeeframework.core.exception.BaseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * struts SecurityRequestWrapper 封装
 *
 * @author lance
 * @version 1.0 2015-09-06 18:12
 */
public class SecurityRequestWrapper extends HttpServletRequestWrapper {
    Map<String, String[]> dirtyWordFilterMap = new HashMap<String, String[]>();

    HttpServletRequest localReq = null;

    public SecurityRequestWrapper(HttpServletRequest req) {
        super(req);
        this.localReq = req;
        initSecurityParameterMap();
    }

    public String getParameter(String strName) {
        String[] paramValues = dirtyWordFilterMap.get(strName);
        if (paramValues != null && paramValues.length > 0) {
            return paramValues[0];
        }

        return super.getParameter(strName);
    }

    public String[] getParameterValues(String strName) {
        if (dirtyWordFilterMap.get(strName) != null) {
            return dirtyWordFilterMap.get(strName);
        }
        return super.getParameterValues(strName);
    }

    //
    public Map getParameterMap() {
        if (dirtyWordFilterMap.size() == 0) {
            return super.getParameterMap();
        }
        return dirtyWordFilterMap;
    }

    public void initSecurityParameterMap() {
        Enumeration enumeration = getParameterNames();
        String[] paramValues = null;
        String filterdata = null;
        while (enumeration.hasMoreElements()) {
            String name = (String) enumeration.nextElement();
            paramValues = super.getParameterValues(name);
            if (null != paramValues) {
                for (int i = 0; i < paramValues.length; i++) {

                    filterdata = paramValues[i];
                    try {
                        paramValues[i] = new String(filterdata.getBytes("UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        throw new BaseException(e);
                    }
                }
                dirtyWordFilterMap.put(name, paramValues);
            }
        }
    }
}

/*
 * @project: web_demo 
 * @package: com.webdemo.web.form
 * @title:   JQGridRequest.java 
 *
 * Copyright (c) 2015 Hyfay Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.webdemo.web.form;

/**
 * JQGrid数据请求对象
 *
 * @author lance
 * @version 1.0 2015-08-13 18:24
 */
public class JQGridRequest {
    private String _search;
    private String nd;
    private int page;
    private int rows;
    private String sidx;
    private String sord;

    public String get_search() {
        return _search;
    }

    public void set_search(String _search) {
        this._search = _search;
    }

    public String getNd() {
        return nd;
    }

    public void setNd(String nd) {
        this.nd = nd;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getSidx() {
        return sidx;
    }

    public void setSidx(String sidx) {
        this.sidx = sidx;
    }

    public String getSord() {
        return sord;
    }

    public void setSord(String sord) {
        this.sord = sord;
    }
}

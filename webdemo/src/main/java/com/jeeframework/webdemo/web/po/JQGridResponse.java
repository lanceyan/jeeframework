/*
 * @project: web_demo 
 * @package: com.webdemo.web.po
 * @title:   JQGridResponse.java 
 *
 * Copyright (c) 2015 Hyfay Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.webdemo.web.po;

import java.util.ArrayList;

/**
 * jqgrid 响应内容
 *
 * {
 "total": "xxx", //总页数
 "page": "yyy",  //当前页
 "records": "zzz", //本次查询的记录数
 "rows" : [
 {"id" :"1", "name" : "xxxx", "addr" : "xxxx", ...},
 {"id" :"2", "name" : "xxxx", "addr" : "xxxx", ...},
 ...
 ]
 }
 *
 * @author lance
 * @version 1.0 2015-08-13 18:09
 */
public class JQGridResponse {
    private int total; //total pages
    private int page; //current page
    private int records; //total records
    private ArrayList<Object> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRecords() {
        return records;
    }

    public void setRecords(int records) {
        this.records = records;
    }

    public ArrayList<Object> getRows() {
        return rows;
    }

    public void setRows(ArrayList<Object> rows) {
        this.rows = rows;
    }
}

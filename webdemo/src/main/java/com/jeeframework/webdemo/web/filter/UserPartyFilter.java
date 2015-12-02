/**
 * @project: with 
 * @Title: UserPartyFilter.java 
 * @Package: com.webdemo.web.filter
 *
 * Copyright (c) 2014-2014 Transing Limited, Inc.
 * All rights reserved.
 * 
 */
package com.jeeframework.webdemo.web.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户频道查询对象
 * <p>
 * 
 * @Description: 用户频道查询对象
 * @author lance
 * @version 1.0 2015-3-4 下午08:07:47
 */
public class UserPartyFilter {
    private long uid;//用户id
    private Integer lessId; //大于id的查询条件，用于增量查询

    private Integer greatId;
    private int getNum; //一次性获取的数据条数
    private String orderBy;

    private Long lessPhotoModifyId; //最后修改的id

    private List<Long> partyIds = new ArrayList<Long>();//查询的频道id列表

    private Integer inviteStatus; //是否是订阅还是参加

    private List<Integer> inviteStatuses = null;

    public List<Integer> getInviteStatuses() {
        return inviteStatuses;
    }

    public void setInviteStatuses(List<Integer> inviteStatuses) {
        this.inviteStatuses = inviteStatuses;
    }

    public Integer getInviteStatus() {
        return inviteStatus;
    }

    public void setInviteStatus(Integer inviteStatus) {
        this.inviteStatus = inviteStatus;
    }

    public Long getLessPhotoModifyId() {
        return lessPhotoModifyId;
    }

    public void setLessPhotoModifyId(Long lessPhotoModifyId) {
        this.lessPhotoModifyId = lessPhotoModifyId;
    }

    public List<Long> getPartyIds() {
        return partyIds;
    }

    public void setPartyIds(List<Long> partyIds) {
        this.partyIds = partyIds;
    }

    public Integer getGreatId() {
        return greatId;
    }

    public void setGreatId(Integer greatId) {
        this.greatId = greatId;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    /**
     * get getNum 值
     * 
     * @return the getNum int
     */
    public int getGetNum() {
        return getNum;
    }

    /**
     * set getNum 值
     * 
     * @param getNum 设置 getNum 变量的值
     */
    public void setGetNum(int getNum) {
        this.getNum = getNum;
    }

    /**
     * get uid 值
     * 
     * @return the uid int
     */
    public long getUid() {
        return uid;
    }

    /**
     * set uid 值
     * 
     * @param uid 设置 uid 变量的值
     */
    public void setUid(long uid) {
        this.uid = uid;
    }

    /**
     * get lessId 值
     * 
     * @return the lessId Integer
     */
    public Integer getLessId() {
        return lessId;
    }

    /**
     * set lessId 值
     * 
     * @param lessId 设置 lessId 变量的值
     */
    public void setLessId(Integer lessId) {
        this.lessId = lessId;
    }

}

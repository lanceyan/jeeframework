/**
 * @project: with
 * @Title: UserPartyFilter.java
 * @Package: com.webdemo.web.filter
 * <p/>
 * Copyright (c) 2014-2014 Transing Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.webdemo.web.filter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户查询对象
 * <p>
 *
 * @Description: 用户频道查询对象
 * @author lance
 * @version 1.0 2015-3-4 下午08:07:47
 */
public class UserFilter {
    private List<Long> uids = new ArrayList<Long>();//查询的用户id列表

    private String orderBy;
    private String orderDirect;

    private Long uid;
    private String mobile;
    private String passwd;
    private String newPassword;

    private String token;
    private String avatar;
    private String nickName;
    private String description;

    private String birthday;
    private Integer openBirth;
    private Integer openMobile;

    private Integer sex;
    private String city;
    private String province;
    private String country;
    private Integer source;

    private Date createTime;

    private Integer startRow;
    private Integer pageSize;

    private String greatCreateTime;
    private String lessCreateTime;

    public String getGreatCreateTime() {
        return greatCreateTime;
    }

    public void setGreatCreateTime(String greatCreateTime) {
        this.greatCreateTime = greatCreateTime;
    }

    public String getLessCreateTime() {
        return lessCreateTime;
    }

    public void setLessCreateTime(String lessCreateTime) {
        this.lessCreateTime = lessCreateTime;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderDirect() {
        return orderDirect;
    }

    public void setOrderDirect(String orderDirect) {
        this.orderDirect = orderDirect;
    }

    public Integer getStartRow() {
        return startRow;
    }

    public void setStartRow(Integer startRow) {
        this.startRow = startRow;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    //    只能填 1或者 2,1代表weixin，2代表微博

    public final static int USER_SOURCE_WEIXIN = 1;
    public final static int USER_SOURCE_WEIBO = 2;


    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * get uid 值
     *
     * @return the uid Long
     */
    public Long getUid() {
        return uid;
    }

    /**
     * set uid 值
     *
     * @param uid 设置 uid 变量的值
     */
    public void setUid(Long uid) {
        this.uid = uid;
    }

    /**
     * get birthday 值
     *
     * @return the birthday String
     */
    public String getBirthday() {
        return birthday;
    }

    /**
     * set birthday 值
     *
     * @param birthday 设置 birthday 变量的值
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    /**
     * get openBirth 值
     *
     * @return the openBirth int
     */
    public Integer getOpenBirth() {
        return openBirth;
    }

    /**
     * set openBirth 值
     *
     * @param openBirth 设置 openBirth 变量的值
     */
    public void setOpenBirth(Integer openBirth) {
        this.openBirth = openBirth;
    }

    /**
     * get openMobile 值
     *
     * @return the openMobile int
     */
    public Integer getOpenMobile() {
        return openMobile;
    }

    /**
     * set openMobile 值
     *
     * @param openMobile 设置 openMobile 变量的值
     */
    public void setOpenMobile(Integer openMobile) {
        this.openMobile = openMobile;
    }

    /**
     * get nickName 值
     *
     * @return the nickName String
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * set nickName 值
     *
     * @param nickName 设置 nickName 变量的值
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * get avatar 值
     *
     * @return the avatar String
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * set avatar 值
     *
     * @param avatar 设置 avatar 变量的值
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * get token 值
     *
     * @return the token String
     */
    public String getToken() {
        return token;
    }

    /**
     * set token 值
     *
     * @param token 设置 token 变量的值
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * get mobile 值
     *
     * @return the mobile String
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * set mobile 值
     *
     * @param mobile 设置 mobile 变量的值
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * get passwd 值
     *
     * @return the passwd String
     */
    public String getPasswd() {
        return passwd;
    }

    /**
     * set passwd 值
     *
     * @param passwd 设置 passwd 变量的值
     */
    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    /**
     * get newPassword 值
     *
     * @return the newPassword String
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * set newPassword 值
     *
     * @param newPassword 设置 newPassword 变量的值
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * get uids 值
     *
     * @return the uids List<Integer>
     */
    public List<Long> getUids() {
        return uids;
    }

    /**
     * set uids 值
     *
     * @param uids 设置 uids 变量的值
     */
    public void setUids(List<Long> uids) {
        this.uids = uids;
    }

}

package com.jeeframework.webdemo.integration.bo;

import com.jeeframework.logicframework.integration.bo.AbstractBO;

import java.util.Date;

/**
 * 用户对象
 * 
 * @author lanceyan
 * @version 1.0
 * @see AbstractBO
 */
public class User extends AbstractBO {
    private long uid;
    private String mobile;
    private String passwd;
    private String nickName;
    private String description;
    private String avatar;
    private String birthday;
    private String token;
    private Date createTime;
    private Date lastModifyTime;

    private Integer sex;
    private String city;
    private String province;
    private String country;
    private Integer source;


    public final static int SEX_MALE = 1; //男
    public final static int SEX_FEMALE = 1;//女

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
     * get createTime 值
     * 
     * @return the createTime Date
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * set createTime 值
     * 
     * @param createTime 设置 createTime 变量的值
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * get lastModifyTime 值
     * 
     * @return the lastModifyTime Date
     */
    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    /**
     * set lastModifyTime 值
     * 
     * @param lastModifyTime 设置 lastModifyTime 变量的值
     */
    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

}
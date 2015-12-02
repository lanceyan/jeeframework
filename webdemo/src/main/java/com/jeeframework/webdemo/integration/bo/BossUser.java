package com.jeeframework.webdemo.integration.bo;

import com.jeeframework.logicframework.integration.bo.AbstractBO;

import java.util.Date;

/**
 * 用户对象
 *
 * @author lanceyan
 * @version 1.0
 * @see com.jeeframework.logicframework.integration.bo.AbstractBO
 */
public class BossUser extends AbstractBO {
    private long uid;
    private String passwd;
    private String userName;
    private String nickName;
    private String description;
    private String avatar;
    private Integer type;
    private String email;
    private Date createTime;
    private Date lastModifyTime;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    //
//    uid` int(11) NOT NULL AUTO_INCREMENT COMMENT 'userid自增序列',
//            `passwd` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
//    `nickname` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '昵称',
//            `description` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '描述信息',
//            `avatar` varchar(300) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '头像',
//            `type` tinyint(3) NOT NULL DEFAULT '1' COMMENT '用户类型， 1 是普通用户 ，2 是管理员',
//            `email` varchar(100) NOT NULL DEFAULT '' COMMENT '邮件地址',
//            `createtime` datetime NOT NULL COMMENT '创建日期',
//            `lastmodifytime


    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }
}
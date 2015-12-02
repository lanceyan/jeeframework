package com.jeeframework.webdemo.web.form;


/**
 *
 */
public class UserQueryRequest  extends JQGridRequest
{
    private String nickName;
    private String createTime;


    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
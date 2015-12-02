package com.jeeframework.webdemo.biz.service;

import com.jeeframework.logicframework.biz.exception.BizException;
import com.jeeframework.logicframework.biz.service.BizService;
import com.jeeframework.webdemo.integration.bo.User;
import com.jeeframework.webdemo.web.filter.UserFilter;

import java.util.List;

/**
 *
 * @author lanceyan
 * @version 1.0
 */
public interface UserService extends BizService {

    /**
     * 简单描述：根据userFilter返回用户对象列表
     * <p/>
     *
     * @param userFilter
     * @throws BizException
     */
    public List<User> getUsers(UserFilter userFilter) throws BizException;

    public long getUserCount(UserFilter userFilter) throws BizException;

}
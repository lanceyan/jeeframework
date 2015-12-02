package com.jeeframework.webdemo.biz.service.impl.local;

import com.jeeframework.logicframework.biz.exception.BizException;
import com.jeeframework.logicframework.biz.service.BaseService;
import com.jeeframework.logicframework.common.logging.LoggerUtil;
import com.jeeframework.logicframework.integration.dao.DAOException;
import com.jeeframework.webdemo.biz.service.UserService;
import com.jeeframework.webdemo.integration.UserDataService;
import com.jeeframework.webdemo.integration.bo.User;
import com.jeeframework.webdemo.web.filter.UserFilter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @author lanceyan
 * @version 1.0
 */
@Service("userService")
public class UserServicePojo extends BaseService implements UserService {
    public static int refCount = 0;

    public UserServicePojo() {
        refCount++;
    }

    protected void finalize() throws Throwable {
        super.finalize();

        refCount--;
    }

    @Resource
    private UserDataService userDataService;

    public UserDataService getUserDataService() {
        return userDataService;
    }

    public void setUserDataService(UserDataService userDataService) {
        this.userDataService = userDataService;
    }


    public int getObjCount() {
        return refCount;
    }

    public List<User> getUsers(UserFilter userFilter) throws BizException {
        try {
            return userDataService.getUsers(userFilter);
        } catch (DAOException e) {
            LoggerUtil.errorTrace("addUser", "添加用户出错", e);
            throw new BizException("添加用户数据库出错" + e, e);
        } finally {
        }
    }

    public long getUserCount(UserFilter userFilter) throws BizException {
        try {
            return userDataService.getUserCount(userFilter);
        } catch (DAOException e) {
            LoggerUtil.errorTrace("getUserCount", "查询用户数量出错", e);
            throw new BizException("查询用户数量出错" + e, e);
        } finally {
        }
    }

}
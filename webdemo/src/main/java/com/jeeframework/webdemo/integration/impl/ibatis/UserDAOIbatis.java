package com.jeeframework.webdemo.integration.impl.ibatis;

import com.jeeframework.logicframework.integration.DataServiceException;
import com.jeeframework.logicframework.integration.dao.DAOException;
import com.jeeframework.logicframework.integration.dao.ibatis.BaseDaoiBATIS;
import com.jeeframework.logicframework.integration.error.ErrorInfo;
import com.jeeframework.webdemo.integration.UserDataService;
import com.jeeframework.webdemo.integration.bo.User;
import com.jeeframework.webdemo.web.filter.UserFilter;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户数据访问对象
 *
 * @author lanceyan
 * @version 1.0
 */
@Scope("prototype")
@Repository("userDataService")
public class UserDAOIbatis extends BaseDaoiBATIS implements UserDataService {
    public static int refCount = 0; //���󴴽�������

    public UserDAOIbatis() {
        refCount++;
    }

    protected void finalize() throws Throwable {
        super.finalize();

        refCount--;
    }

    public int getObjCount() {
        return refCount;
    }

    /* 
     * 简单描述：TODO
     * <p>
     * 详细描述：TODO
     *
     * @see com.jeeframework.logicframework.integration.DataService#getLastErrorInfo()
     */
    public ErrorInfo getLastErrorInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    /* 
     * 简单描述：TODO
     * <p>
     * 详细描述：TODO
     *
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * 简单描述：根据userFilter返回用户对象列表
     * <p/>
     *
     * @param userFilter
     * @throws DataServiceException
     */
    public List<User> getUsers(UserFilter userFilter) throws DataServiceException {
        try {
            return sqlSessionTemplate.selectList("userMapper.getUsers", userFilter);
        } catch (DataAccessException e) {
            throw new DAOException("根据token查询用户信息失败", e);
        } finally {
        }
    }

    public long getUserCount(UserFilter userFilter) throws DataServiceException {
        try {
            return sqlSessionTemplate.selectOne("userMapper.getUserCount", userFilter);
        } catch (DataAccessException e) {
            throw new DAOException("查询用户数量出错" + e, e);
        } finally {
        }
    }


}
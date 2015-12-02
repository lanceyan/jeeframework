package com.jeeframework.webdemo.integration.impl.ibatis;

import com.jeeframework.logicframework.integration.DataServiceException;
import com.jeeframework.logicframework.integration.dao.DAOException;
import com.jeeframework.logicframework.integration.dao.ibatis.BaseDaoiBATIS;
import com.jeeframework.logicframework.integration.error.ErrorInfo;
import com.jeeframework.webdemo.integration.bo.BossUser;
import com.jeeframework.webdemo.web.filter.BossUserFilter;
import com.jeeframework.webdemo.integration.BossUserDataService;
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
@Repository("bossUserDataService")
public class BossUserDAOIbatis extends BaseDaoiBATIS implements BossUserDataService {
    public static int refCount = 0;

    public BossUserDAOIbatis() {
        refCount++;
    }

    protected void finalize() throws Throwable {
        super.finalize();

        refCount--;
    }

    public int addBossUser(BossUser bossUser) throws DAOException {
        try {
            return sqlSessionTemplate.insert("bossBossUserMapper.addBossUser", bossUser);
        } catch (DataAccessException e) {
            throw new DAOException("添加用户出错" + e, e);
        } finally {
        }
    }

    public BossUser getBossUser(BossUserFilter bossUserFilter) throws DAOException {
        try {
            BossUser bossUser = sqlSessionTemplate.selectOne("bossBossUserMapper.getBossUser", bossUserFilter);
            return bossUser;
        } catch (DataAccessException e) {
            throw new DAOException("查询用户出错" + e, e);
        } finally {
        }
    }

    public int updateBossUser(BossUserFilter bossUserFilter) throws DAOException {
        try {
            return sqlSessionTemplate.update("bossBossUserMapper.updateBossUser", bossUserFilter);
        } catch (DataAccessException e) {
            throw new DAOException("修改用户出错" + e, e);
        } finally {
        }
    }

    public void deleteBossUser(String mobile) throws DAOException {
        try {
            sqlSessionTemplate.delete("bossBossUserMapper.deleteBossUser", mobile);
        } catch (DataAccessException e) {
            // ����ݿ������װΪ��������
            throw new DAOException("刪除用户出错" + e, e);
        } finally {
        }
    }

    /**
     * ����ʵ�ֻ�ȡ��ǰ����ʵ��ĸ�������־����
     *
     * @param ����˵����ÿ������һ�У�ע����ȡֵ��Χ��
     * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
     * @throws �쳣��ע�ͳ�ʲô�����»���ʲô����쳣
     * @see com.jeeframework.logicframework.integration.dao.ibatis.BaseDaoiBATIS#getObjCount()
     */
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

    /* 
     * 简单描述：TODO
     * <p>
     * 详细描述：TODO
     *
     * @see BossUserDataService#getBossUserByPasswd(BossUser)
     */
    public BossUser getBossUserByPasswd(BossUser bossUserParam) throws DataServiceException {
        try {
            BossUser bossUser = sqlSessionTemplate.selectOne("bossBossUserMapper.getBossUserByPasswd", bossUserParam);
            return bossUser;
        } catch (DataAccessException e) {
            throw new DAOException("根据密码查询用户信息失败", e);
        } finally {
        }
    }

    /**
     * 简单描述：根据userFilter返回用户对象列表
     * <p/>
     *
     * @param bossUserFilter
     * @throws DataServiceException
     */
    public List<BossUser> getBossUsers(BossUserFilter bossUserFilter) throws DataServiceException {
        try {
            return sqlSessionTemplate.selectList("bossBossUserMapper.getBossUsers", bossUserFilter);
        } catch (DataAccessException e) {
            throw new DAOException("根据token查询用户信息失败", e);
        } finally {
        }
    }


}
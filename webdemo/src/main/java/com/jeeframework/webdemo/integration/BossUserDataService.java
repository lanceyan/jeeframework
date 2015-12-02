package com.jeeframework.webdemo.integration;

import com.jeeframework.logicframework.integration.DataService;
import com.jeeframework.logicframework.integration.DataServiceException;
import com.jeeframework.webdemo.integration.bo.BossUser;
import com.jeeframework.webdemo.web.filter.BossUserFilter;

import java.util.List;

/**
 * �û�db���ʲ����Ľӿ���
 * 
 * @author lanceyan�������޸��ߣ�
 * @version 1.0���°汾�ţ�
 * @see �ο���JavaDoc
 */
public interface BossUserDataService extends DataService {

    public BossUser getBossUser(BossUserFilter bossUserFilter) throws DataServiceException;

    public int addBossUser(BossUser bossUser) throws DataServiceException;

    public int updateBossUser(BossUserFilter bossUserFilter) throws DataServiceException;

    public void deleteBossUser(String mobile) throws DataServiceException;

    /**
     * 简单描述：根据用户名、密码返回用户对象
     * <p>
     * 
     * @param bossUser
     * @throws DataServiceException
     */
    public BossUser getBossUserByPasswd(BossUser bossUser) throws DataServiceException;

    /**
     * 简单描述：根据userFilter返回用户对象列表
     * <p>
     * 
     * @param bossUserFilter
     * @throws DataServiceException
     */
    public List<BossUser> getBossUsers(BossUserFilter bossUserFilter) throws DataServiceException;


}
package com.jeeframework.webdemo.constant;

import com.jeeframework.webdemo.integration.bo.BossUser;

import java.util.HashMap;
import java.util.Map;

/**
 * �����࣬�������й̶�����
 *
 * @author lanceyan�������޸��ߣ�
 * @version 1.0{�°汾�ţ�
 * @see �ο���JavaDoc
 */
public class Constants {

    //ϵͳ���ļ��ָ��
    public static final String FILE_SEP = System.getProperty("file.separator");

    //ȡ���û���homeĿ¼
    public static final String USER_HOME = System.getProperty("user.home") + FILE_SEP;

    // web��ĳ���
    public static final String ACTION = ".action";

    public final static long COOKIE_MAX_AGE = 60 * 60 * 24 * 7 * 4; // 默认cookie保存4个星期
    public final static int COOKIE_TWO_YEAR_AGE = 60 * 60 * 24 * 365 * 2; // 两年的cookie
    public static final  int COOKIE_ONE_YEAR_AGE = 60 * 60 * 24 * 365 * 1;

    public static final String WEB_LOGIN_KEY = "WEIXIN_VERIFY_KEY";
    public static final String LINK_USER_COOKIE_LOGIN = "lk_sess";

    // 用于自动登录，后续采用分离式cache
    public static Map<String, BossUser> SESSION_CACHE = new HashMap<String, BossUser>();


    /**
     * 用户注册来源常量
     */
    public static int USER_REGISTE_FROM_WEIXIN = 1; //从微信来源注册
    public static int USER_REGISTE_FROM_WEIBO = 2; //从微博哦来源注册
    public static int USER_REGISTE_FROM_SNAPCHAT = 21; //从微信来源注册


    /**
     * 字段注释
     * session存放的用户对象key值
     */
    public static final String WITH_SESSION_USER = "withSessionUser";
    /**
     * 字段注释
     * cookie自动登陆保存key
     */
    public static final String LOGIN_COOKIE_SIGN = "with_cookie_sign";

    /**
     * 后台系统的高亮菜单id
     */
    public static final String BOSS_MENU_ID = "trans_boss_menu_id";

    /**
     * 字段注释
     * request 对象里的userbo数据
     */
    public static final String REQUEST_USERBO = "user";

    /**
     * 字段注释
     * 登陆验证秘钥key
     */
    public static final String LOGIN_KEY = "WITH_LOGIN_VERIFY_KEY";


}

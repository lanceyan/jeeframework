package com.jeeframework.webframework.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * SystemCode
 * 系统错误编码枚举
 *
 * @author lance
 * @date 2016/3/21 0021
 */
public class SystemCode {
    public SystemCode() {

    }

    /**
     * 错误枚举  第一位表示异常级别  1 业务  2系统保留前100位 101  102
     * <p>
     * 第2位表示 异常模块 保留前100位
     * 第3位表示 错误参数 保留前100位
     * //////枚举命名规则///////
     * 业务_模块_错误名称_异常
     */

    public static Map<Integer, String> errorMessageMap = new HashMap<Integer, String>();


    //////登陆异常相关模块//////    1 1  111    11 11 1    111 1 1      1000   1000   1000
    public final static int BIZ_LOGIN_NAME_EXCEPTION = 1_10_10;
    public final static String BIZ_LOGIN_NAME_EXCEPTION_MESSAGE = "请输入用户名!";

    static {
        errorMessageMap.put(BIZ_LOGIN_NAME_EXCEPTION, BIZ_LOGIN_NAME_EXCEPTION_MESSAGE);
    }

    public final static int BIZ_LOGIN_PASSWORD_EXCEPTION = 1_10_11;
    public final static String BIZ_LOGIN_PASSWORD_EXCEPTION_MESSAGE = "请输入密码!";

    static {
        errorMessageMap.put(BIZ_LOGIN_PASSWORD_EXCEPTION, BIZ_LOGIN_PASSWORD_EXCEPTION_MESSAGE);
    }

    public final static int BIZ_LOGIN_PASSNOTRIGHT_EXCEPTION = 1_10_12;
    public final static String BIZ_LOGIN_PASSNOTRIGHT_EXCEPTION_MESSAGE = "用户名密码不对!";

    static {
        errorMessageMap.put(BIZ_LOGIN_PASSNOTRIGHT_EXCEPTION, BIZ_LOGIN_PASSNOTRIGHT_EXCEPTION_MESSAGE);
    }

    public final static int BIZ_LOGIN_EXPIRED_EXCEPTION = 1_10_13;
    public final static String BIZ_LOGIN_EXPIRED_EXCEPTION_MESSAGE = "用户登录失效，请重新登录!";

    static {
        errorMessageMap.put(BIZ_LOGIN_EXPIRED_EXCEPTION, BIZ_LOGIN_EXPIRED_EXCEPTION_MESSAGE);
    }


    //    //!!!!!!系统级异常!!!!!!
    public final static int SYS_APPSERVER_EXCEPTION = 2_10_10;
    public final static String SYS_APPSERVER_EXCEPTION_MESSAGE = "服务器严重错误!";

    static {
        errorMessageMap.put(SYS_APPSERVER_EXCEPTION, SYS_APPSERVER_EXCEPTION_MESSAGE);
    }

    //
    public final static int SYS_REQUEST_EXCEPTION = 2_10_11;
    public final static String SYS_REQUEST_EXCEPTION_MESSAGE = "必须传入请求参数!";

    static {
        errorMessageMap.put(SYS_REQUEST_EXCEPTION, SYS_REQUEST_EXCEPTION_MESSAGE);
    }

    //
    public final static int SYS_CONTROLLER_EXCEPTION = 2_10_12;
    public final static String SYS_CONTROLLER_EXCEPTION_MESSAGE = "调用出错了!";

    static {
        errorMessageMap.put(SYS_CONTROLLER_EXCEPTION, SYS_CONTROLLER_EXCEPTION_MESSAGE);
    }

    //
    public final static int SYS_DB_EXCEPTION = 2_10_13;
    public final static String SYS_DB_EXCEPTION_MESSAGE = "调用数据库操作出错!";

    static {
        errorMessageMap.put(SYS_DB_EXCEPTION, SYS_DB_EXCEPTION_MESSAGE);
    }

    /**
     * 错误码
     */
    public int errorCode;
    /**
     * 系统错误信息
     */
    public String errorMessage;

    /**
     * 用来定义系统错误码常量的枚举定义
     *
     * @param errorCode
     * @param errorMessage
     */
    private SystemCode(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static String getSystemCodeMessageByCode(int errorCode) {

        String errorMessage = errorMessageMap.get(errorCode);
        return errorMessage;
    }
}

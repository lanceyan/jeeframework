package com.jeeframework.core.logging;

/**
 * <p>
 * 抽象日志API的接口，为了让进行初始化，实现该接口的类的构造函数 需要一个参数"name"来命名该日志
 * </p>
 * <p/>
 * <p>
 * 本系统提供4种级别的日志:
 * <ol>
 * <li>调试 debug</li>
 * <li>性能 performance</li>
 * <li>运营 info</li>
 * <li>错误 error</li>
 * </ol>
 * <p>
 * 另增加一种log记录异常的堆栈信息(保留)
 * <p>
 * 具体日志系统的配置使用由相应外部日志系统来决定.
 * </p>
 *
 * @author lanceyan（最新修改者）
 * @version 1.0（新版本号）
 */
public interface Logger {

    /**
     * 错误log的文件名
     */
    static final String ERRORLOG = "error.log";
    /**
     * 调试log的文件名
     */
    static final String DEBUGLOG = "debug.log";
    /**
     * 信息log的文件名
     */
    static final String INFOLOG = "info.log";


    /**
     * 描述： 将调试信息记录到调试级别日志
     *
     * @param DBG 调试级别日志要记录的信息
     */
    void debugTrace(String DBG);

    /**
     * 描述： 将运营信息记录到调试级别日志
     *
     * @param message 调试级别日志要记录的信息
     */
    void infoTrace(String message);

    /**
     * 描述： 将异常错误信息记录到异常日志
     *
     * @param message 错误信息
     */
    void errorTrace(String message);

    /**
     * 异常日志
     *
     * @param message 消息
     * @param t       异常
     */
    void errorTrace(String message, Throwable t);


    /**
     * 将调试信息记录到调试级别日志(主要由平台内部组件调用)
     *
     * @param busiModule 模块名
     * @param message    日志消息
     */
    void debugTrace(String busiModule, String message);


    /**
     * 将调试信息记录到INFO级别日志(主要由平台内部组件调用)
     *
     * @param busiModule 模块名
     * @param message    日志消息
     */
    void infoTrace(String busiModule, String message);


    /**
     * 描述： 将错误信息记录到错误级别日志(主要由平台内部组件调用)
     *
     * @param busiModule 模块名
     * @param message    日志消息
     */
    void errorTrace(String busiModule, String message);


    /**
     * 描述： 将错误信息记录到错误级别日志(主要由平台内部组件调用)
     *
     * @param busiModule 模块名
     * @param message    日志消息
     * @param t          异常
     */
    void errorTrace(String busiModule,  String message, Throwable t);



}

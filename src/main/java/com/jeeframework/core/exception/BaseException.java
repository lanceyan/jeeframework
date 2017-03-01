package com.jeeframework.core.exception;

/**
 * 平台运行时异常。各层的基类。
 *
 * @author lanceyan（最新修改者）
 * @version 1.0（新版本号）
 */

public class BaseException extends RuntimeException {
    public final static int DEFAULT_BIZ_ERROR_CODE = 9;
    public final static int DEFAULT_SYS_ERROR_CODE = 99;

    protected int errorCode = DEFAULT_BIZ_ERROR_CODE; // 错误码默认为9

    protected String errorMessage = null; // 错误消息默认为null

    /**
     * @param errorCode
     * @param msg       设置错误消息，并设置errorMessage/errMsgForView为msg
     */
    public BaseException(int errorCode, String msg) {
        super(msg);
        this.errorMessage = msg;
        this.errorCode = errorCode;
    }

    /**
     * @param errorCode
     * @param msg        设置错误消息，并设置errorMessage为msg
     * @param msgForView 设置错误消息页面展现，并设置errMsgForView为msgForView
     */
    public BaseException(int errorCode, String msg, String msgForView) {
        super(msg);
        this.errorMessage = msg;
        this.errorCode = errorCode;
    }

    /**
     * @param msg        设置错误消息，并设置errorMessage为msg
     * @param msgForView 设置错误消息页面展现，并设置errMsgForView为msgForView
     */
    public BaseException(String msg, String msgForView) {
        super(msg);
        this.errorMessage = msg;
    }

    /**
     * @param msg 设置错误消息，并设置errorMessage/errMsgForView为msg
     */
    public BaseException(String msg) {
        super(msg);
        this.errorMessage = msg;
    }

    /**
     * @param errorCode
     * @param msg       设置错误消息，并设置errorMessage/errMsgForView为msg
     * @param cause     将当前的异常包装为Exception
     */
    public BaseException(int errorCode, String msg, Throwable cause) {
        super(msg, cause);
        this.errorMessage = msg;
        this.errorCode = errorCode;
    }

    /**
     * @param msg   设置错误消息，并设置errorMessage/errMsgForView为msg
     * @param cause 将当前的异常包装为Exception
     */
    public BaseException(String msg, Throwable cause) {
        super(msg, cause);
        this.errorMessage = msg;
        if (cause instanceof BaseException) {
            this.errorCode = ((BaseException) cause).getErrorCode();
        }
    }

    /**
     * @param cause 将当前的异常包装为Exception
     */
    public BaseException(Throwable cause) {
        super(cause);
        if (cause instanceof BaseException) {
            this.errorCode = ((BaseException) cause).getErrorCode();
        }
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * 得到当前的错误消息。
     *
     * @return errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


//    public static void main(String[] args)
//    {
//        // Exception ex = new Exception(new RuntimeException());
//        Exception ex = new Exception("testmsg %s ^ %s", "testmsgview %s ^ %s");
//        ex.setErrorMessageArgs("####", "$$$$");
//        ex.setErrMsgForViewArgs("####", "$$$$");
//
//        System.out.println(ex.getErrMsgForView());
//        System.out.println(ex.getErrorMessage());
//
//    }

}

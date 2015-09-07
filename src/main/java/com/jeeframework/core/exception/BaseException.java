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

    protected String errMsgForView = null; // 显示在页面上的错误消息，带格式化的字符串

    protected String errorMessageFmt = null; // 格式化后的字符串

    protected String errMsgForViewFmt = null; // 显示在页面上的格式化后的字符串

    /**
     * @param errorCode
     * @param msg       设置错误消息，并设置errorMessage/errMsgForView为msg
     */
    public BaseException(int errorCode, String msg) {
        super(msg);
        this.errorMessage = msg;
        this.errorCode = errorCode;
        this.errMsgForView = msg;
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
        this.errMsgForView = msgForView;
    }

    /**
     * @param msg        设置错误消息，并设置errorMessage为msg
     * @param msgForView 设置错误消息页面展现，并设置errMsgForView为msgForView
     */
    public BaseException(String msg, String msgForView) {
        super(msg);
        this.errorMessage = msg;
        this.errMsgForView = msgForView;
    }

    /**
     * @param msg 设置错误消息，并设置errorMessage/errMsgForView为msg
     */
    public BaseException(String msg) {
        super(msg);
        this.errorMessage = msg;
        this.errMsgForView = msg;
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
        this.errMsgForView = msg;
    }

    /**
     * @param msg   设置错误消息，并设置errorMessage/errMsgForView为msg
     * @param cause 将当前的异常包装为Exception
     */
    public BaseException(String msg, Throwable cause) {
        super(msg, cause);
        this.errorMessage = msg;
        this.errMsgForView = msg;
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
     * 得到当前的错误消息。 如果带格式的错误信息errMsgForViewFmt为空，则直接显示错误信息errMsgForView。
     *
     * @return errMsgForView 需要显示给用户的信息。
     */
    public String getErrorMessage() {
        if (errorMessageFmt != null) {
            return errorMessageFmt;
        }
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * 得到当前的错误消息（显示给用户)。 如果带格式的错误信息errMsgForViewFmt为空，则直接显示错误信息errMsgForView。
     *
     * @return errMsgForView 需要显示给用户的信息。
     */
    public String getErrMsgForView() {
        if (errMsgForViewFmt != null) {
            return errMsgForViewFmt;
        }
        return errMsgForView;
    }

    public void setErrMsgForView(String errMsgForView) {
        this.errMsgForView = errMsgForView;
    }

    /**
     * 设置带格式的message的参数进行替换显示
     *
     * @param args 格式化消息串
     */
    public void setErrMsgForViewArgs(Object... args) {
        if (errMsgForView == null) {
            throw new BaseException("设置格式化参数时，请先设置errMsgForView为带格式化的字符串！");
        }
        errMsgForViewFmt = String.format(errMsgForView, args);
    }

    /**
     * 设置带格式的message的参数进行替换
     *
     * @param args 格式化消息串
     */
    public void setErrorMessageArgs(Object... args) {
        if (errorMessage == null) {
            throw new BaseException("设置格式化参数时，请先设置errorMessage为带格式化的字符串！");
        }
        errorMessageFmt = String.format(errorMessage, args);
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

package com.jeeframework.logicframework.biz.exception;

import com.jeeframework.core.exception.BaseException;


    
public class BizException extends BaseException
{
    /**
     * @param errorCode
     * @param msg
     */
    public BizException(int errorCode, String msg)
    {
        super(msg);
        this.errorMessage = msg;
        this.errorCode = errorCode;
    }

    public BizException(int errorCode)
    {
        super("");
        this.errorCode = errorCode;
    }

    public BizException(int errorCode, Throwable cause){
        super(cause);
        this.errorCode = errorCode;
    }

    /**
     * @param msg
     */
    public BizException(String msg)
    {
        super(msg);
        this.errorMessage = msg;
    }

    /**
     * 
     * @param errorCode
     * @param msg
     * @param cause
     */
    public BizException(int errorCode, String msg, Throwable cause)
    {
        super(msg, cause);
        this.errorMessage = msg;
        this.errorCode = errorCode;
    }

    /**
     * @param msg
     * @param cause
     */
    public BizException(String msg, Throwable cause)
    {
        super(msg, cause);
        this.errorMessage = msg;
    }

    /**
     * @param cause
     */
    public BizException(Throwable cause)
    {
        super(cause);
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    public void setErrorCode(int errorCode)
    {
        this.errorCode = errorCode;
    }

    /**
     *
     */
    public String getErrorMessage()
    {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

}

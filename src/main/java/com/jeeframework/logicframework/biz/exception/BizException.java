package com.jeeframework.logicframework.biz.exception;

import com.jeeframework.core.exception.BaseException;

/** 
 * ҵ��������쳣��Ϣ�������
 * @author lanceyan�������޸��ߣ�
 * @version 1.0���°汾�ţ�
 * @see com.jeeframework.core.exception.BaseException
 */
    
public class BizException extends BaseException
{
    /**
     * @param errorCode
     * @param msg
     *            ���ô�����Ϣ��������errorMessage/errMsgForViewΪmsg
     */
    public BizException(int errorCode, String msg)
    {
        super(msg);
        this.errorMessage = msg;
        this.errorCode = errorCode;
        this.errMsgForView = msg;
    }

    /**
     * @param errorCode
     * @param msg
     *            ���ô�����Ϣ��������errorMessageΪmsg
     * @param msgForView
     *            ���ô�����Ϣҳ��չ�֣�������errMsgForViewΪmsgForView
     */
    public BizException(int errorCode, String msg, String msgForView)
    {
        super(msg);
        this.errorMessage = msg;
        this.errorCode = errorCode;
        this.errMsgForView = msgForView;
    }

    /**
     * @param msg
     *            ���ô�����Ϣ��������errorMessageΪmsg
     * @param msgForView
     *            ���ô�����Ϣҳ��չ�֣�������errMsgForViewΪmsgForView
     */
    public BizException(String msg, String msgForView)
    {
        super(msg);
        this.errorMessage = msg;
        this.errMsgForView = msgForView;
    }

    /**
     * @param msg
     *            ���ô�����Ϣ��������errorMessage/errMsgForViewΪmsg
     */
    public BizException(String msg)
    {
        super(msg);
        this.errorMessage = msg;
        this.errMsgForView = msg;
    }

    /**
     * 
     * @param errorCode
     * @param msg
     *            ���ô�����Ϣ��������errorMessage/errMsgForViewΪmsg
     * @param cause
     *            ����ǰ���쳣��װΪException
     */
    public BizException(int errorCode, String msg, Throwable cause)
    {
        super(msg, cause);
        this.errorMessage = msg;
        this.errorCode = errorCode;
        this.errMsgForView = msg;
    }

    /**
     * @param msg
     *            ���ô�����Ϣ��������errorMessage/errMsgForViewΪmsg
     * @param cause
     *            ����ǰ���쳣��װΪException
     */
    public BizException(String msg, Throwable cause)
    {
        super(msg, cause);
        this.errorMessage = msg;
        this.errMsgForView = msg;
    }

    /**
     * @param cause
     *            ����ǰ���쳣��װΪException
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
     * �õ���ǰ�Ĵ�����Ϣ�� �����ʽ�Ĵ�����ϢerrMsgForViewFmtΪ�գ���ֱ����ʾ������ϢerrMsgForView��
     * 
     * @return errMsgForView ��Ҫ��ʾ���û�����Ϣ��
     */
    public String getErrorMessage()
    {
        if (errorMessageFmt != null)
        {
            return errorMessageFmt;
        }
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

    /**
     * �õ���ǰ�Ĵ�����Ϣ����ʾ���û�)�� �����ʽ�Ĵ�����ϢerrMsgForViewFmtΪ�գ���ֱ����ʾ������ϢerrMsgForView��
     * 
     * @return errMsgForView ��Ҫ��ʾ���û�����Ϣ��
     */
    public String getErrMsgForView()
    {
        if (errMsgForViewFmt != null)
        {
            return errMsgForViewFmt;
        }
        return errMsgForView;
    }

    public void setErrMsgForView(String errMsgForView)
    {
        this.errMsgForView = errMsgForView;
    }

    /**
     * ���ô��ʽ��message�Ĳ�������滻��ʾ
     * 
     * @param args
     *            ��ʽ����Ϣ��
     */
    public void setErrMsgForViewArgs(Object... args)
    {
        if (errMsgForView == null)
        {
            throw new BaseException("���ø�ʽ������ʱ����������errMsgForViewΪ���ʽ�����ַ�");
        }
        errMsgForViewFmt = String.format(errMsgForView, args);
    }

    /**
     * ���ô��ʽ��message�Ĳ�������滻
     * 
     * @param args
     *            ��ʽ����Ϣ��
     */
    public void setErrorMessageArgs(Object... args)
    {
        if (errorMessage == null)
        {
            throw new BaseException("���ø�ʽ������ʱ����������errorMessageΪ���ʽ�����ַ�");
        }
        errorMessageFmt = String.format(errorMessage, args);
    }
}

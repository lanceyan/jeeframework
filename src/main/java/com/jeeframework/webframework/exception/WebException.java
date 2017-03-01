package com.jeeframework.webframework.exception;

import com.jeeframework.core.exception.BaseException;


/**

 */
public class WebException extends BaseException
{

	public WebException(int errorCode, String msg) {
		super(msg);
		this.errorMessage = msg;
		this.errorCode = errorCode;
	}

	public WebException(int errorCode, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
	}

	public WebException(int errorCode) {
		super("");
		String msg = SystemCode.getSystemCodeMessageByCode(errorCode);
		this.errorMessage = msg;
		this.errorCode = errorCode;
	}

	public WebException(String msg) {
		super(msg);
		this.errorMessage = msg;
	}


	public WebException(int errorCode, String msg, Throwable cause) {
		super(msg, cause);
		this.errorMessage = msg;
		this.errorCode = errorCode;
	}

	public WebException(String msg, Throwable cause) {
		super(msg, cause);
		this.errorMessage = msg;
	}

	public WebException(Throwable cause) {
		super(cause);
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}


}

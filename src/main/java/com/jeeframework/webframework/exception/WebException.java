package com.jeeframework.webframework.exception;

import com.jeeframework.core.exception.BaseException;


/**

 */
public class WebException extends BaseException
{
	public WebException(String message)
	{
		super(message);
		//logger.errorTrace(message, this);
	}

	public WebException(String message, Throwable cause)
	{
		super(message, cause);
		//logger.errorTrace(message, this);
	}
}

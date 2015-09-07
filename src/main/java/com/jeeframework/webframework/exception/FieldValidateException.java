package com.jeeframework.webframework.exception;

import com.jeeframework.core.exception.BaseException;


/**

 */
public class FieldValidateException extends BaseException
{
	public FieldValidateException(String message)
	{
		super(message);
		//logger.errorTrace(message, this);
	}

	public FieldValidateException(String message, Throwable cause)
	{
		super(message, cause);
		//logger.errorTrace(message, this);
	}
}

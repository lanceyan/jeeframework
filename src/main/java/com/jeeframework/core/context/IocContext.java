package com.jeeframework.core.context;

import com.jeeframework.core.context.support.ConsoleContextHelper;

public class IocContext
{
	private static IocContextHelper	helper	= null;

	public static IocContextHelper getHelper()
	{
		if(helper == null)
			ConsoleContextHelper.load();
		
		return helper;
	}

	public static void setHelper(IocContextHelper value)
	{
		helper = value;
	}
}

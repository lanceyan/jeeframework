package com.jeeframework.util.so;

import java.io.File;

import com.jeeframework.util.os.OsHelper;

/**
 * @author lanceyan
 *
 */
public class SoHelper
{
	private static boolean	isLinux	= false;
	static
	{
		isLinux = !OsHelper.isWindowOs();
	}


	public static void loadSoByEnvName(String soEnvName)
	{
		if (isLinux)
		{
			try
			{
				String soname = System.getenv(soEnvName);
				if (soname == null || !new File(soname).exists())
				{
					System.err.println("missing ENV parameter " + soEnvName + "! please config right value!");
					System.err.println("JVM EXIT NOW!");
					System.exit(-1);
				}
				else
				{
					System.load(soname);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				System.err.println("load " + soEnvName + " failed!" + e.getMessage());
				System.err.println("JVM EXIT NOW!");
				System.exit(-1);
			}
		}
	}
}

package com.jeeframework.util.os;

/**
 * ����ϵͳ������
 * 
 * @author lanceyan
 *
 */
public class OsHelper
{
	/**
	 * �жϲ���ϵͳ�Ƿ��� windows
	 * @return true �� false ����
	 */
	public static boolean isWindowOs()
	{
		return System.getProperty("os.name").toLowerCase().indexOf("window") > -1;
	}
}

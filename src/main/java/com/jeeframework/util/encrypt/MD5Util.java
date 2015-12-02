/**
 * Copyright (C) 2015-2015 JEEFRAMEWORK Inc.All Rights Reserved.
 * 																	
 * FileName��MD5Util.java					
 *			
 * Description����Ҫ�������ļ�������							 												
 * History��
 * �汾��    ����           ����          ��Ҫ������ز���
 *  1.0   lanceyan        2008-6-30           Create	
 */

package com.jeeframework.util.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5��Ӧ�ü�����
 * 
 * @author lanceyan�������޸��ߣ�
 * @version 1.0���°汾�ţ�
 * @see �ο���JavaDoc
 */

public class MD5Util
{
	private static final String	MD5	= "MD5";

	public static String encrypt(String value)
	{
		if (value == null)
			return "";

		MessageDigest md = null;
		String strDes = null;

		try
		{
			md = MessageDigest.getInstance(MD5);
			md.update(value.getBytes());
			strDes = bytes2Hex(md.digest()); // to HexString
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new RuntimeException(e);
		}
		return strDes;
	}

	public static String bytes2Hex(byte[] byteArray)
	{
		StringBuffer strBuf = new StringBuffer();
		String tmp = null;
		for (int i = 0; i < byteArray.length; i++)
		{
			tmp = Integer.toHexString(byteArray[i] & 0xFF);
			if (tmp.length() == 1)
			{
				strBuf.append("0");
			}
			strBuf.append(tmp);
		}
		return strBuf.toString();
	}

	public static void main(String[] args)
	{
		System.out.println(MD5Util.encrypt("12312412123"));
	}
}

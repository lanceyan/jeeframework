/**
 * Copyright (C) 1998-2007 JEEFRAMEWORK Inc.All Rights Reserved.
 * 																	
 * FileName��CookieHelper.java					
 *			
 * Description��cookie��д��						 												
 * History��
 * �汾��    ����      ����       ��Ҫ������ز���
 *  1.0   lanceyan  2008-6-19  Create	
 */

package com.jeeframework.util.cookie;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ����cookie�Ļ���
 * 
 * @author lanceyan�������޸��ߣ�
 * @version 1.0���°汾�ţ�
 * @see �ο���JavaDoc
 */

public class CookieHelper {
	private static String code = "UTF-8"; // Ĭ�ϰ���gb2312����

	/**
	 * ���Ĭ�ϵı����ʽ������URL encode��decode��
	 * 
	 * @param newCode
	 *            ��Ҫ���õı����ʽ��
	 * @return ���ؾɵı����ʽ��
	 * @throws UnsupportedEncodingException
	 *             ���ָ�����µı����ʽ����java VM֧�֣����׳����쳣�������ʽ����ı䡣
	 */
	public static String setCharacterEncoding(String newCode)
			throws UnsupportedEncodingException {
		if (!Charset.isSupported(newCode)) {
			throw new UnsupportedEncodingException();
		}
		String oldCode = code;
		code = newCode;
		return oldCode;
	}

	/**
	 * ���request��nameȡ��Cookie δ��Ĭ�ϵ�url decode
	 * 
	 * @param ����˵����ÿ������һ�У�ע����ȡֵ��Χ��
	 * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
	 * @exception �쳣��ע�ͳ�ʲô�����»���ʲô����쳣
	 * @see �ο���JavaDoc
	 */
	public static Cookie getCookie(HttpServletRequest request, String name) {
		Cookie cookies[] = request.getCookies();
		if (cookies == null || name == null || name.length() == 0) {
			return null;
		}
		for (Cookie cookie : cookies) {
			if (name.equalsIgnoreCase(cookie.getName())) {
				return cookie;
			}
		}
		return null;
	}

	/**
	 * ȡ��cookie��ָ��name��Ӧ��value, Ĭ�Ͻ���gbk�����url decode
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request, String name) {
		if (request == null || name == null || name.length() == 0) {
			return null;
		}
		Cookie cookies[] = request.getCookies();
		if (cookies == null) {
			return null;
		}
		// cookie�д���'@'ʱ����������ֱ�ӷ���

		try {
			String newName = URLEncoder.encode(name, code);
			for (Cookie cookie : cookies) {
				if (newName.equalsIgnoreCase(cookie.getName())) {
					return URLDecoder.decode(cookie.getValue(), code);
				}
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage());
		}
		return null;
	}

	/**
	 * ȡ��cookie��ָ��name��Ӧ��long value, �÷����ر��ʺϴ� cookie �л�ȡ qquin
	 * 
	 * @param request
	 *            http����
	 * @param name
	 *            cookie��ֵ��
	 * @return
	 */
	public static long getCookieLongValue(HttpServletRequest request,
			String name) {
		long ret = 0;

		String value = getCookieValue(request, name);
		if (value == null)
			return ret;

		try {
			ret = Long.valueOf(value);
		} catch (Exception e) {
		}

		return ret;
	}

	/**
	 * ����cookie Ĭ�Ͻ���gbk�����url encode
	 * 
	 * @param ����˵����response
	 *            http��Ӧ
	 * @param ����˵����name
	 *            cookie�Ĺؼ���
	 * @param ����˵����value
	 *            cookie��ֵ
	 * @param ����˵����domain
	 *            cookie����
	 * @param ����˵����path
	 *            cookie·��
	 * @param ����˵����age
	 *            cookie���ʱ��
	 * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
	 * @exception �쳣��ע�ͳ�ʲô�����»���ʲô����쳣
	 * @see �ο���JavaDoc
	 */
	public static void setCookie(HttpServletResponse response, String name,
			String value, String domain, String path, int age) {
		if (response == null) {
			throw new RuntimeException("����cookie���?responseΪ��");
		}
		if (name == null) {
			throw new RuntimeException("����cookie���?nameΪ��");
		}

		if (value == null) {
			throw new RuntimeException("����cookie���?valueΪ��");
		}

		try {
			String newName = URLEncoder.encode(name, code);
			String newValue = URLEncoder.encode(value, code);
			Cookie cookie = new Cookie(newName, newValue);
			// ��������
			cookie.setMaxAge(age);

			if (null != domain) {
				cookie.setDomain(domain);
			}

			if (null != path) {
				cookie.setPath(path);
			}
			response.addCookie(cookie);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}

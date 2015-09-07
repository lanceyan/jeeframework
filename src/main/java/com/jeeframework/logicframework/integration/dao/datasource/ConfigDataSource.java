/**
 * Copyright (C) 1998-2007 JEEFRAMEWORK Inc.All Rights Reserved.
 * 																	
 * FileName��ConfigDataSource.java					
 *			
 * Description����Ҫ�������ļ�������							 * 												 * History��
 * �汾��    ����      ����       ��Ҫ������ز���
 *  1.0   lanceyan  2008-5-16  Create	
 */

package com.jeeframework.logicframework.integration.dao.datasource;

import org.apache.commons.dbcp.BasicDataSource;

import com.jeeframework.util.encrypt.EncryptUtil;

/**
 * ���Դ�Ĵ����࣬���ڻ�ȡ�������ĵ����
 * 
 * @author lanceyan�������޸��ߣ�
 * @version 1.0���°汾�ţ�
 * 
 */

public class ConfigDataSource extends BasicDataSource {
	private boolean isConfig = false; // �Ƿ���������Ļ�ȡ��Ĭ�ϴ���ݿ�

	private boolean encrypt = false;// 默认不加密

	private String encryptPass = null;// 默认的加密密码

	public boolean getIsConfig() {
		return isConfig;
	}

	public void setIsConfig(boolean isConfig) {
		this.isConfig = isConfig;
	}

	/**
	 * ʵ��������ݿ����룬��isConfigΪtrueʱͨ���������Ļ�ȡ
	 * 
	 * @param ����˵����ÿ������һ�У�ע����ȡֵ��Χ��
	 * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
	 * @exception �쳣��ע�ͳ�ʲô�����»���ʲô����쳣
	 * @see org.apache.commons.dbcp.BasicDataSource#setPassword(java.lang.String)
	 */
	@Override
	public synchronized void setPassword(String passwd) {
		if (!isConfig) {
			if (encrypt) {
				passwd = EncryptUtil.desDecrypt(encryptPass, passwd);
			}

			super.setPassword(passwd);
		} else {
			// TODO �����������ĵ�API��ȡ����
			System.out.println("in false");
		}

	}

	/**
	 * ʵ��������ݿ�url����isConfigΪtrueʱͨ���������Ļ�ȡ
	 * 
	 * @param ����˵����ÿ������һ�У�ע����ȡֵ��Χ��
	 * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
	 * @exception �쳣��ע�ͳ�ʲô�����»���ʲô����쳣
	 * @see org.apache.commons.dbcp.BasicDataSource#setUrl(java.lang.String)
	 */
	@Override
	public synchronized void setUrl(String url) {
		if (!isConfig) {
			super.setUrl(url);
		} else {
			// TODO �����������ĵ�API��ȡurl
			System.out.println("in false");
		}
	}

	/**
	 * ʵ�������û���isConfigΪtrueʱͨ���������Ļ�ȡ
	 * 
	 * @param ����˵����ÿ������һ�У�ע����ȡֵ��Χ��
	 * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
	 * @exception �쳣��ע�ͳ�ʲô�����»���ʲô����쳣
	 * @see org.apache.commons.dbcp.BasicDataSource#setUsername(java.lang.String)
	 */
	@Override
	public synchronized void setUsername(String userName) {
		if (!isConfig) {
			super.setUsername(userName);
		} else {
			// TODO �����������ĵ�API��ȡ�û���
			System.out.println("in false");
		}
	}

	public String getEncryptPass() {
		return encryptPass;
	}

	public void setEncryptPass(String encryptPass) {
		this.encryptPass = encryptPass;
	}

	public boolean isEncrypt() {
		return encrypt;
	}

	public void setEncrypt(boolean encrypt) {
		this.encrypt = encrypt;
	}

	public void setConfig(boolean isConfig) {
		this.isConfig = isConfig;
	}

}

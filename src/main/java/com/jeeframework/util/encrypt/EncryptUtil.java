/**
 * Copyright (C) 2015-2015 JEEFRAMEWORK Inc.All Rights Reserved.
 * 																	
 * FileName��MD5Util.java					
 *			
 * Description����Ҫ�������ļ�������							 												
 * History��
 *  1.0   lanceyan        2008-6-30           Create	
 */

package com.jeeframework.util.encrypt;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.jeeframework.util.validate.Validate;

/**
 * java加密解密类
 * 
 * @author lanceyan
 * @version 1.0
 * @see
 */

public class EncryptUtil {

	public static final String ENCRY_STR = "*!#$%suffix123*!#$%abcde"; // 123456781234567812345678

	// 

	public static javax.crypto.SecretKey genDESKey(byte[] key_byte) throws RuntimeException {
		SecretKey k = null;

		k = new SecretKeySpec(key_byte, "DESede");

		return k;

	}

	public static byte[] desEncryptBytes(String encKey, String encVal) throws RuntimeException {
		try {
			if (Validate.isNull(encKey)) {
				encKey = "";
			}

			if (encKey.length() > 24) {
				encKey = encKey.substring(0, 24);
			}

			int leftSize = 24 - encKey.length();

			String curEncKey = ENCRY_STR.substring(0, leftSize) + encKey;

			javax.crypto.SecretKey secretKey = genDESKey(curEncKey.getBytes());
			javax.crypto.Cipher cipher;

			cipher = javax.crypto.Cipher.getInstance("DESede");

			cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, secretKey);

			return cipher.doFinal(encVal.getBytes());

		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (NoSuchPaddingException e) {
			throw new RuntimeException(e);
		} catch (IllegalBlockSizeException e) {
			throw new RuntimeException(e);
		} catch (BadPaddingException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e);
		}

	}

	public static String desEncrypt(String encKey, String encVal) throws RuntimeException {
		byte[] binaryData = desEncryptBytes(encKey, encVal);
		String base64 = BASE64Util.encode(binaryData);
		return base64;
	}

	public static String desDecryptBytes(String encKey, byte[] crypt) throws RuntimeException {
		try {
			javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("DESede");

			if (Validate.isNull(encKey)) {
				encKey = "";
			}

			if (encKey.length() > 24) {
				encKey = encKey.substring(0, 24);
			}

			int leftSize = 24 - encKey.length();

			String curEncKey = ENCRY_STR.substring(0, leftSize) + encKey;

			javax.crypto.SecretKey secretKey = genDESKey(curEncKey.getBytes());

			cipher.init(javax.crypto.Cipher.DECRYPT_MODE, secretKey);

			byte[] crypts;

			crypts = cipher.doFinal(crypt);

			String cryptStr = new String(crypts);

			return cryptStr;

		} catch (IllegalBlockSizeException e) {
			throw new RuntimeException(e);
		} catch (BadPaddingException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (NoSuchPaddingException e) {
			throw new RuntimeException(e);
		}

	}

	public static String desDecrypt(String encKey, String encryptStr) throws RuntimeException {

		byte[] encrptByte;
		try {
			encrptByte = BASE64Util.decode(encryptStr);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return desDecryptBytes(encKey, encrptByte);
	}

	public static void main(String[] args) {

		String key = "transing";
		String passwd = "trans";

		try {

			// 生成DES密钥
			String encryptStr = desEncrypt(key, passwd);

			// String encryptStr = new String(encryptByte, "GBK");
			// DES加解密

			// 加密

			System.out.println("encrypt=" + encryptStr);

			// 解密
			String encrypt = desDecrypt(key, encryptStr);

			System.out.println("decrypt=" + encrypt);

		}

		catch (Exception ex) {

			ex.printStackTrace();

		}
	}
}

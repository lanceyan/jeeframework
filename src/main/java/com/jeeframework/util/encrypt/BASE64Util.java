package com.jeeframework.util.encrypt;

import sun.misc.BASE64Encoder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;


/**
 * BASE64������빤��
 * <p>Title: </p>
 * <p>Description: </p>
 */

public class BASE64Util {
    /**
     * �����������ݱ���Ϊbase64�ַ�
     *
     * @param inContent byte[] ����
     * @return String ������
     */
    public static String encode(byte[] inContent) {
        return new sun.misc.BASE64Encoder().encode(inContent);
    }

    /**
     * �����������ݱ���Ϊ������url������ַ���
     * BASE64�е�+��=��url�����У���������ĺ��壬��Ҫ��ת��
     * ��+ת��Ϊ-
     * ��=ת��Ϊ~
     *
     * @param inContent byte[]
     * @return String
     * @throws IOException
     */
    public static String encodeURLLine(byte[] inContent) throws IOException {
        return encodeLine(inContent).replace('+', '-').replace('=', '~');
    }

    /**
     * �����������ݱ���Ϊ�����е�base64�ַ�
     *
     * @param inContent byte[]
     * @return String
     * @throws IOException
     */
    public static String encodeLine(byte[] inContent) throws IOException {
        BufferedReader br = null;
        BufferedWriter bw = null;
        StringWriter sw = new StringWriter();
        try {
            String sContent = new BASE64Encoder().encodeBuffer(inContent);
            /**
             * sun��ʵ�ֻ�ÿ76���ַ��������һ���س�������Ӧ����˵����Ҫ��
             * ��Ҫɾ��
             */
            br = new BufferedReader(new java.io.StringReader(sContent));
            bw = new BufferedWriter(sw);
            String bRead = null;
            while ((bRead = br.readLine()) != null) {
                bw.write(bRead);
            }

            bw.flush();

            return sw.toString();
        } catch (IOException ex) {
            throw ex;
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (Exception ex) {

            }

            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception ex) {

            }
        }
    }

    /**
     * ��base64�ַ����Ϊ�ֽ�����
     *
     * @param inBase64 String
     * @return byte[]
     * @throws IOException
     */
    public static byte[] decode(String inBase64)
            throws IOException {
        return new sun.misc.BASE64Decoder().decodeBuffer(inBase64);
    }

    /**
     * ��URL��BASE64��Line����Ϊ�ֽ�����
     * ��encodeURLLine��Ϊ����Ĺ��
     *
     * @param inBase64URLLine String
     * @return byte[]
     * @throws IOException
     */
    public static byte[] decodeURLLine(String inBase64URLLine)
            throws IOException {
        return decode(inBase64URLLine.replace('-', '+').replace('~', '='));
    }
}

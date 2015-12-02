package com.jeeframework.util.io;



/**
 * @author lanceyan
 * 
 * �����л���һ��������߻ظ�Э��ӿ�
 *
 */
public interface IServiceObject {
	/**
	 * ����Э���������
	 * 
	 * @return long:Э���������
	 */
	public long getCmdId();
	
	/**
	 * Э�����л�
	 * 
	 * @param bs�����л�����ֽ�
	 * 
	 * @return �������л��ֽ��ܳ���
	 * 
	 * @throws Exception
	 */
	public int serialize(ByteStream bs) throws Exception;
	
	/**
	 * Э�鷴���л�
	 * 
	 * @param bs��Ҫ�����л����ֽڶ���
	 * @return Ҫ�����л����ֽڶ����ֽ��ܳ���
	 * @throws Exception
	 */
	public int unSerialize(ByteStream bs) throws Exception;
}

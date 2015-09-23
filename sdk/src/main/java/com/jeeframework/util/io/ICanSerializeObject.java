package com.jeeframework.util.io;

public interface ICanSerializeObject
{
	/**
	 * ������Ϣ�Ĵ�С
	 * 
	 * @return
	 */
	public int getSize();

	/**
	 * ���л�
	 * 
	 * @param bs
	 * @return
	 * @throws Exception
	 */
	public int serialize(ByteStream bs) throws Exception;

	/**
	 * �����л�
	 * 
	 * @param bs
	 * @return
	 * @throws Exception
	 */
	public int unSerialize(ByteStream bs) throws Exception;

}

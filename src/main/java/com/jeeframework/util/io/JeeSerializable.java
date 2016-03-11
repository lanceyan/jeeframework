package com.jeeframework.util.io;

public interface JeeSerializable
{
	public int getSize();
	public int serialize(ByteStream bs) throws Exception;
	public int unSerialize(ByteStream bs) throws Exception;

}

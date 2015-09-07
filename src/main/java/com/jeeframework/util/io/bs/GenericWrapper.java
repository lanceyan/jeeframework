package com.jeeframework.util.io.bs;

public class GenericWrapper
{
	
	/**
	 * 
	 * �����и�������HashMap<String,String>Ҫ��GenericWrapper���
	 * 
	 * ���������д��
	 * GenericWrapper hashMapWrapper = new GenericWrapper();
	 * 
	 * hashMapWrapper.setType(HashMap.class);
	 * hashMapWrapper.setGenericParameters(new GenericWrapper[]{String.class,String.class})
	 * 
	 */
	//����������
	private Class<?>							type;
	//�����������Ƿ��ͣ��ʹ浽����
	private GenericWrapper[]	genericParameters;
    /**
     * 
     * @param type
     * @param genericParameters
     */
	public GenericWrapper(Class<?> type, GenericWrapper[] genericParameters)
	{
		this.type = type;
		this.genericParameters = genericParameters;
	}

	public GenericWrapper()
	{
	}
	/**
	 * 
	 * @param type
	 */
	public GenericWrapper(Class<?> type)
	{
		this.type = type;
	}
    /**
     * 
     * @return genericParameters
     */
	public GenericWrapper[] getGenericParameters()
	{
		return genericParameters;
	}

	public void setGenericParameters(GenericWrapper[] genericParameters)
	{
		this.genericParameters = genericParameters;
	}

	public Class<?> getType()
	{
		return type;
	}

	public void setType(Class<?> type)
	{
		this.type = type;
	}
}

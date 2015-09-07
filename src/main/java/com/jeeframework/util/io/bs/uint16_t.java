package com.jeeframework.util.io.bs;

public class uint16_t extends uint_t
{
	private int value;
    
	private static final int BITSIZE=16;
	
    public static final int   MIN_VALUE = 0x00000000;

    
    public static final int   MAX_VALUE = 0x0000FFFF;
	/**
	 * @return the value
	 */
	public uint16_t(){
		this.value=0;
	}
	public uint16_t(int value){
		this.value=(int) (0x0000FFFF & (value));
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value)  throws Exception{
	
		if(value>MAX_VALUE||value<MIN_VALUE)
			throw new Exception("setValue(int value) ERROR:  value of type int is out of range [MIN_VALUE,MAX_VALUE]:["+MIN_VALUE+","+MAX_VALUE+"]");
		this.value = (int) (0x0000FFFF & (value));
	}
	
	/**
	 * 
	 * @param value
	 * @throws Exception
	 */
	public void setValue(String value)  throws Exception{
	    short tmp=(short)Integer.parseInt(value);
		if(tmp>MAX_VALUE||tmp<MIN_VALUE)
			throw new Exception("setValue(int value) ERROR:  value of type int is out of range [MIN_VALUE,MAX_VALUE]:["+MIN_VALUE+","+MAX_VALUE+"]");
		this.value = (int) (0x0000FFFF & (tmp));
	}
	
	/**
	 * ����value��16����
	 * 
	 * @param 
	 * @return
	 */
	public  String toHexString() {
		return toHexString(value,BITSIZE);
    }
	
	/**
	 * ����value��2����
	 * 
	 * @param
	 * @return
	 */
	public  String toBinaryString() {
		return toBinaryString(this.value,BITSIZE);
    }
	

	
}

package com.jeeframework.util.io.bs;

public class uint8_t extends uint_t{
	private short value;
    
	private static final short BITSIZE=8;
	
    public static final short   MIN_VALUE = 0x0000;
    
    
    public static final short   MAX_VALUE = 0x00FF;
	/**
	 * @return the value
	 */
	public uint8_t(){
		this.value=0;
	}
	/**
	 * 
	 * @param value
	 */
	public uint8_t(int value){
		this.value=(short) (0x000000FF & (value));
	}
	/**
	 * 
	 * @return
	 */
	public int getValue() {
		return value;
	}
	/**
	 * 
	 * @param value
	 * @throws Exception
	 */
	public void setValue(short value)  throws Exception{
	
		if(value>MAX_VALUE||value<MIN_VALUE)
			throw new Exception("setValue(int value) ERROR:  value of type int is out of range [MIN_VALUE,MAX_VALUE]:["+MIN_VALUE+","+MAX_VALUE+"]");
		this.value = (short) (0x000000FF & (value));
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
		this.value = (short) (0x000000FF & (tmp));
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

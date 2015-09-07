package com.jeeframework.util.io.bs;

public class uint32_t extends uint_t
{
	private long value;

    public static final long   MIN_VALUE = 0x0000000000000000L;
    
    private static final int BITSIZE=32;
	
    
    public static final long   MAX_VALUE = 0x00000000FFFFFFFFL;
	/**
	 * @return the value
	 */
	public uint32_t(){
		this.value=0;
	}
	public uint32_t(long value){
		this.value=(long) (0x00000000FFFFFFFFL & (value));
	}
	public long getValue() {
		return value;
	}
	public void setValue(long value)  throws Exception{
	
		if(value>MAX_VALUE||value<MIN_VALUE)
			throw new Exception("setValue(int value) ERROR:  value of type int is out of range [MIN_VALUE,MAX_VALUE]:["+MIN_VALUE+","+MAX_VALUE+"]");
		this.value = (long) (0x00000000FFFFFFFFL & (value));
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
		this.value = (long) (0x00000000FFFFFFFFL & (tmp));
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

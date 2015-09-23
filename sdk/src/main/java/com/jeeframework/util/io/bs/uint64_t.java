package com.jeeframework.util.io.bs;

import java.util.regex.Pattern;

public class uint64_t {
	
	/**
	 * ���ֵ
	 */
	private String value="0";
    public static final String   MAX_VALUE = "18446744073709551615";
    
    /**
     * ��Сֵ
     */
    
    public final static Pattern pattern=Pattern.compile("^\\d{0,20}$");
   
    public static final String   MIN_VALUE = "0";
	
	public uint64_t(){
		
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	
	
	public void setValue(byte[] value) throws Exception{
		if(value==null||value.length > 20)
			throw new Exception("setValue(int value) ERROR:  value of type int is out of range [MIN_VALUE,MAX_VALUE]:["+MIN_VALUE+","+MAX_VALUE+"]");
		else{
			String tmp=new String(value);
			if(!pattern.matcher(tmp).find()|| (tmp.length()==20&& tmp.compareTo(MAX_VALUE)>-1))
				throw new Exception("setValue(int value) ERROR:  value of type int is out of range [MIN_VALUE,MAX_VALUE]:["+MIN_VALUE+","+MAX_VALUE+"]");
			this.value=tmp;
		}
		
	}
	
	
}

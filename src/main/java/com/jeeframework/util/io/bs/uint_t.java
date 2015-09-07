package com.jeeframework.util.io.bs;

public abstract class uint_t {
	
	 private final static char[] digits = {
	    	'0' , '1' , '2' , '3' , '4' , '5' ,
	    	'6' , '7' , '8' , '9' , 'a' , 'b' ,
	    	'c' , 'd' , 'e' , 'f' , 'g' , 'h' ,
	    	'i' , 'j' , 'k' , 'l' , 'm' , 'n' ,
	    	'o' , 'p' , 'q' , 'r' , 's' , 't' ,
	    	'u' , 'v' , 'w' , 'x' , 'y' , 'z'
	        };
	
	
	/**
	 * ����i��shift����
	 * 
	 * @param i
	 * @param shift
	 * @param radix1
	 * @return
	 */
	private  String toUnsignedString(long i, int shift,int radix1) {
		int length=32;
		if(radix1<64)
			length=32;
		else
			length=64;
		char[] buf = new char[length];
		int charPos = length;
		int radix = 1 << shift;
		long mask = radix - 1;
		do {
		    buf[--charPos] = digits[(int)((long)i & mask)];
		    i >>>= shift;
		} while (i != 0);
		return  new String(buf, charPos, (length - charPos));
	}
	/**
	 * ����i��16����
	 * 
	 * @param i
	 * @return
	 */
	protected  String toHexString(long i,int radix) {
		return toUnsignedString(i, 4,radix);
    }
	
	/**
	 * ����i��2����
	 * 
	 * @param i
	 * @return
	 */
	protected  String toBinaryString(long i,int radix) {
			return toUnsignedString(i, 1,radix);
     }
   

}

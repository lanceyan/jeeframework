package com.jeeframework.util.string;

/**
 * 用于转码的通用类
 * 
 * @author lanceyan（最新修改者）
 * @version 1.0{新版本号）
 */

public class EscapeHelper
{

	   private static final char[] hexDigit = {
	        '0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'
	    };
	    
	    private static char toHex(int nibble) {
	        return hexDigit[(nibble & 0xF)];
	    }
	    
	    /**
	     * 将字符串编码成 Unicode 。
	     * @param theString 待转换成Unicode编码的字符串。
	     * @param escapeSpace 是否忽略空格。
	     * @return 返回转换后Unicode编码的字符串。
	     */
	    public static String toUnicode(String theString, boolean escapeSpace) {
	        int len = theString.length();
	        int bufLen = len * 2;
	        if (bufLen < 0) {
	            bufLen = Integer.MAX_VALUE;
	        }
	        StringBuffer outBuffer = new StringBuffer(bufLen);

	        for(int x=0; x<len; x++) {
	            char aChar = theString.charAt(x);
	            // Handle common case first, selecting largest block that
	            // avoids the specials below
	            if ((aChar > 61) && (aChar < 127)) {
	                if (aChar == '\\') {
	                    outBuffer.append('\\'); outBuffer.append('\\');
	                    continue;
	                }
	                outBuffer.append(aChar);
	                continue;
	            }
	            switch(aChar) {
	                case ' ':
	                    if (x == 0 || escapeSpace) 
	                        outBuffer.append('\\');
	                    outBuffer.append(' ');
	                    break;
	                case '\t':outBuffer.append('\\'); outBuffer.append('t');
	                          break;
	                case '\n':outBuffer.append('\\'); outBuffer.append('n');
	                          break;
	                case '\r':outBuffer.append('\\'); outBuffer.append('r');
	                          break;
	                case '\f':outBuffer.append('\\'); outBuffer.append('f');
	                          break;
	                case '=': // Fall through
	                case ':': // Fall through
	                case '#': // Fall through
	                case '!':
	                    outBuffer.append('\\'); outBuffer.append(aChar);
	                    break;
	                default:
	                    if ((aChar < 0x0020) || (aChar > 0x007e)) {
	                        outBuffer.append('\\');
	                        outBuffer.append('u');
	                        outBuffer.append(toHex((aChar >> 12) & 0xF));
	                        outBuffer.append(toHex((aChar >>  8) & 0xF));
	                        outBuffer.append(toHex((aChar >>  4) & 0xF));
	                        outBuffer.append(toHex( aChar        & 0xF));
	                    } else {
	                        outBuffer.append(aChar);
	                    }
	            }
	        }
	        return outBuffer.toString();
	    }
		/**
		 * unicode转换成汉字
		 * @param s
		 * @return
		 */
	    public static String fromUnicode(String curStr)
	    {
	    	char[]  in = curStr.toCharArray();
	    	char[] convtBuf = new char[in.length];
	    	return fromUnicode( in, 0,  in.length, convtBuf);
	    }
	    /**
	     * 从 Unicode 码转换成编码前的特殊字符串。
	     * @param in Unicode编码的字符数组。
	     * @param off 转换的起始偏移量。
	     * @param len 转换的字符长度。
	     * @param convtBuf 转换的缓存字符数组。
	     * @return 完成转换，返回编码前的特殊字符串。
	     */
	    public static String fromUnicode(char[] in, int off, int len, char[] convtBuf) {
	        if (convtBuf.length < len) {
	            int newLen = len * 2;
	            if (newLen < 0) {
	                newLen = Integer.MAX_VALUE;
	            }
	            convtBuf = new char[newLen];
	        }
	        char aChar;
	        char[] out = convtBuf;
	        int outLen = 0;
	        int end = off + len;

	        while (off < end) {
	            aChar = in[off++];
	            if (aChar == '\\') {
	                aChar = in[off++];
	                if (aChar == 'u') {
	                    // Read the xxxx
	                    int value = 0;
	                    for (int i = 0; i < 4; i++) {
	                        aChar = in[off++];
	                        switch (aChar) {
	                        case '0':
	                        case '1':
	                        case '2':
	                        case '3':
	                        case '4':
	                        case '5':
	                        case '6':
	                        case '7':
	                        case '8':
	                        case '9':
	                            value = (value << 4) + aChar - '0';
	                            break;
	                        case 'a':
	                        case 'b':
	                        case 'c':
	                        case 'd':
	                        case 'e':
	                        case 'f':
	                            value = (value << 4) + 10 + aChar - 'a';
	                            break;
	                        case 'A':
	                        case 'B':
	                        case 'C':
	                        case 'D':
	                        case 'E':
	                        case 'F':
	                            value = (value << 4) + 10 + aChar - 'A';
	                            break;
	                        default:
	                            throw new IllegalArgumentException(
	                                    "Malformed \\uxxxx encoding.");
	                        }
	                    }
	                    out[outLen++] = (char) value;
	                } else {
	                    if (aChar == 't') {
	                        aChar = '\t';
	                    } else if (aChar == 'r') {
	                        aChar = '\r';
	                    } else if (aChar == 'n') {
	                        aChar = '\n';
	                    } else if (aChar == 'f') {
	                        aChar = '\f';
	                    }
	                    out[outLen++] = aChar;
	                }
	            } else {
	                out[outLen++] = (char) aChar;
	            }
	        }
	        return new String(out, 0, outLen);
	    }
    private final static String[] hex = { "00", "01", "02", "03", "04", "05",
            "06", "07", "08", "09", "0A", "0B", "0C", "0D", "0E", "0F", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "1A", "1B",
            "1C", "1D", "1E", "1F", "20", "21", "22", "23", "24", "25", "26",
            "27", "28", "29", "2A", "2B", "2C", "2D", "2E", "2F", "30", "31",
            "32", "33", "34", "35", "36", "37", "38", "39", "3A", "3B", "3C",
            "3D", "3E", "3F", "40", "41", "42", "43", "44", "45", "46", "47",
            "48", "49", "4A", "4B", "4C", "4D", "4E", "4F", "50", "51", "52",
            "53", "54", "55", "56", "57", "58", "59", "5A", "5B", "5C", "5D",
            "5E", "5F", "60", "61", "62", "63", "64", "65", "66", "67", "68",
            "69", "6A", "6B", "6C", "6D", "6E", "6F", "70", "71", "72", "73",
            "74", "75", "76", "77", "78", "79", "7A", "7B", "7C", "7D", "7E",
            "7F", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89",
            "8A", "8B", "8C", "8D", "8E", "8F", "90", "91", "92", "93", "94",
            "95", "96", "97", "98", "99", "9A", "9B", "9C", "9D", "9E", "9F",
            "A0", "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "AA",
            "AB", "AC", "AD", "AE", "AF", "B0", "B1", "B2", "B3", "B4", "B5",
            "B6", "B7", "B8", "B9", "BA", "BB", "BC", "BD", "BE", "BF", "C0",
            "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "CA", "CB",
            "CC", "CD", "CE", "CF", "D0", "D1", "D2", "D3", "D4", "D5", "D6",
            "D7", "D8", "D9", "DA", "DB", "DC", "DD", "DE", "DF", "E0", "E1",
            "E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9", "EA", "EB", "EC",
            "ED", "EE", "EF", "F0", "F1", "F2", "F3", "F4", "F5", "F6", "F7",
            "F8", "F9", "FA", "FB", "FC", "FD", "FE", "FF" };

    private final static byte[] val = { 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
            0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
            0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
            0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
            0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x00, 0x01,
            0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x3F, 0x3F, 0x3F,
            0x3F, 0x3F, 0x3F, 0x3F, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x3F,
            0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
            0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
            0x3F, 0x3F, 0x3F, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x3F, 0x3F,
            0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
            0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
            0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
            0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
            0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
            0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
            0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
            0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
            0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
            0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
            0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
            0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
            0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
            0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F };

    /**
     * <p>
     * Discription:[加码]
     * </p>
     * 
     * @param s
     * @return
     * @author:lanceyan
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public static String escape(String s)
    {
        StringBuffer sbuf = new StringBuffer();
        int len = s.length();
        for (int i = 0; i < len; i++)
        {
            int ch = s.charAt(i);
            if (ch == ' ')
            { // space : map to '+'
                sbuf.append('+');
            }
            else if ('A' <= ch && ch <= 'Z')
            { // 'A'..'Z' : as it was
                sbuf.append((char) ch);
            }
            else if ('a' <= ch && ch <= 'z')
            { // 'a'..'z' : as it was
                sbuf.append((char) ch);
            }
            else if ('0' <= ch && ch <= '9')
            { // '0'..'9' : as it was
                sbuf.append((char) ch);
            }
            else if (ch == '-'
                    || ch == '_' // unreserved : as it was
                    || ch == '.' || ch == '!' || ch == '~' || ch == '*'
                    || ch == '\'' || ch == '(' || ch == ')')
            {
                sbuf.append((char) ch);
            }
            else if (ch <= 0x007F)
            { // other ASCII : map to %XX
                sbuf.append('%');
                sbuf.append(hex[ch]);
            }
            else
            { // unicode : map to %uXXXX
                sbuf.append('%');
                sbuf.append('u');
                sbuf.append(hex[(ch >>> 8)]);
                sbuf.append(hex[(0x00FF & ch)]);
            }
        }
        return sbuf.toString();
    }

    /**
     * <p>
     * Discription:[解码]
     * </p>
     * 
     * @param s
     * @return
     * @author:lanceyan
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public static String unescape(String s)
    {
        StringBuffer sbuf = new StringBuffer();
        int i = 0;
        int len = s.length();
        while (i < len)
        {
            int ch = s.charAt(i);
            if (ch == '+')
            { // + : map to ' '
                sbuf.append(' ');
            }
            else if ('A' <= ch && ch <= 'Z')
            { // 'A'..'Z' : as it was
                sbuf.append((char) ch);
            }
            else if ('a' <= ch && ch <= 'z')
            { // 'a'..'z' : as it was
                sbuf.append((char) ch);
            }
            else if ('0' <= ch && ch <= '9')
            { // '0'..'9' : as it was
                sbuf.append((char) ch);
            }
            else if (ch == '-'
                    || ch == '_' // unreserved : as it was
                    || ch == '.' || ch == '!' || ch == '~' || ch == '*'
                    || ch == '\'' || ch == '(' || ch == ')')
            {
                sbuf.append((char) ch);
            }
            else if (ch == '%')
            {
                int cint = 0;
                if ('u' != s.charAt(i + 1))
                { // %XX : map to ascii(XX)
                    cint = (cint << 4) | EscapeHelper.val[s.charAt(i + 1)];
                    cint = (cint << 4) | EscapeHelper.val[s.charAt(i + 2)];
                    i += 2;
                }
                else
                { // %uXXXX : map to unicode(XXXX)
                    cint = (cint << 4) | EscapeHelper.val[s.charAt(i + 2)];
                    cint = (cint << 4) | EscapeHelper.val[s.charAt(i + 3)];
                    cint = (cint << 4) | EscapeHelper.val[s.charAt(i + 4)];
                    cint = (cint << 4) | EscapeHelper.val[s.charAt(i + 5)];
                    i += 5;
                }
                sbuf.append((char) cint);
            }
            i++;
        }
        return sbuf.toString();
    }
    
    public static void main(String args[]) throws Exception
    {
        String code = EscapeHelper.escape("http://localhost:8080/bossreport2/tradeStat!querySubClass.action?beginDate=2008-1-11&endDate=2008-01-17&_beginDate=2008-01-11&_endDate=2008-01-17&parentId=2001&thirdId=1&statType=1&subClassMajorId=trade_all&classLevel=1&classId=2001&isTenpay=0&flag5188=0&isMoney=0&needDistribute=1&needOrderDetail=1&tableName=bossStatsDataPlus傅抱石");

        code = EscapeHelper.unescape(code);

        
        code = "\"\" &quot;&quot; ''哈哈";
        
        
        String code1  = java.net.URLEncoder.encode(code,"utf-8");
        code1=code1.replaceAll("\\+", "%20");//处理空格
        
        
        code = EscapeHelper.escape(code);
        
        code = EscapeHelper.unescape(code);

    }

}

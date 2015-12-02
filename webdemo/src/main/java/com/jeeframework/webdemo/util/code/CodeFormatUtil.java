package com.jeeframework.webdemo.util.code;

public class CodeFormatUtil {
    /**
     * 用户id
     */
    public final static String USER_CODE_FORMAT_STR = "%8s";
    /**
     * 频道id
     */
    public final static String PARTY_CODE_FORMAT_STR = "%8s001%016X";

    /**
     * 将long参数转换成网络字节序的字符串
     * 
     * @param v
     * @return
     */
    public static String htonls(long v) {
        return String.format("%02X%02X%02X%02X", new Object[] { (byte) (v >>> 0), (byte) (v >>> 8), (byte) (v >>> 16), (byte) (v >>> 24) });
    }

    /**
     * 将long参数转换成网络字节序的字符串
     * 
     * @param v
     * @return
     */
    public static long ntohls(String s) {
        try {
            Long v = Long.valueOf(s, 16);
            return (((v & 255) << 24) + ((v >>> 8 & 255) << 16) + ((v >>> 16 & 255) << 8) + ((v >>> 24 & 255) << 0));
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 从频道编码中提取出频道ID
     * 
     * @param partyCode 频道编码 code 长度是 32 的字符串，符合 %8s--------%016X 这种格式
     * @return 0 失败， 大于0 成功
     */
    public static long getPartyIdFromPartyCode(String partyCode) {
        long ret = 0;

        if (partyCode == null) {
            return 0;
        }
        if (partyCode.length() < 27)
            return ret;

        try {
            ret = Long.valueOf(partyCode.substring(11), 16);
        } catch (Exception e) {
        }

        return ret;
    }

    /**
     * 从编码中提取uin
     * 
     * @param code 长度是 32 的字符串，符合 %8s--------%016X 这种格式
     * @return 0 失败, 大于0 成功
     * @return
     */
    public static long getUinFromCode(String code) {
        long ret = 0;

        if (code == null) {
            return 0;
        }
        if (code.length() < 8)
            return ret;

        return ntohls(code.substring(0, 8));
    }

    /**
     * 从频道编码中提取uin
     * 
     * @param code 长度是 32 的字符串，符合 %8s--------%016X 这种格式
     * @return 0 失败, 大于0 成功
     * @return
     */
    public static long getUinFromPartyCode(String code) {
        long ret = 0;

        if (code == null) {
            return 0;
        }
        if (code.length() < 27)
            return ret;

        return ntohls(code.substring(0, 8));
    }

    /**
     * 根据用户id 生成频道编码
     * 
     * @param uin 用户id
     * @return 生成频道编码
     */
    public static String makeUserCode(long uin) {
        return String.format(CodeFormatUtil.USER_CODE_FORMAT_STR, CodeFormatUtil.htonls(uin));
    }

    /**
     * 根据用户id、频道id 生成频道编码
     * 
     * @param uin 用户id
     * @param partyID 频道id
     * @return 生成频道编码
     */
    public static String makePartyCode(long uin, long partyID) {
        return String.format(CodeFormatUtil.PARTY_CODE_FORMAT_STR, CodeFormatUtil.htonls(uin), partyID);
    }

    public static void main(String[] args) {

        System.out.println(makeUserCode(10157));
        System.out.println(makePartyCode(10000, 1001));

        System.out.println(getUinFromCode("C3270000"));
        System.out.println(getPartyIdFromPartyCode("112700000010000000000000403"));
    }

}

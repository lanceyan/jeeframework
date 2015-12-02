package com.jeeframework.util.net;

import com.jeeframework.util.validate.Validate;
import sun.net.util.IPAddressUtil;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;

/**
 * IPת�����ߣ���Ϊ�ų�ǿ���javaû���ṩ��
 *
 * @author hokyhu
 * @version 1.0���°汾�ţ�
 * @see �ο���JavaDoc
 */
public class IPUtil {
    /**
     * ��ipv4��ʽ���ַ�תΪ�����ʽ����������C API��inet_pton
     *
     * @param ip �ַ��ʽ��IP
     * @return IP��Ӧ������ֵ������ʱ���ظ���
     */
    public static long IP2Long(String ip) {
        if (null == ip) {
            return -1;
        }
        String[] ipArray = ip.split("[.]");
        if (4 != ipArray.length) {
            return -2;
        }
        long ipLong = 0;
        for (int i = 3; i >= 0; --i) {
            try {
                long section = Long.valueOf(ipArray[i]);
                ipLong = (ipLong << 8) + section;
            } catch (Exception e) {
                return -3;
            }
        }
        return ipLong;
    }

    /**
     * ��һ������תΪipv4��ʽ���ַ���������C API��inet_ntop
     *
     * @param ip ����
     * @return �����Ӧ��ipv4�ַ�����ʱ����null��
     */
    public static String Long2IP(long ip) {
        if (0 != (ip & 0xFFFFFFFF00000000L)) {
            return null;
        }
        if (0 == ip) {
            return "0.0.0.0";
        }
        String sIP = "";
        for (int i = 0; i < 4; ++i) {
            if (0 != sIP.length()) {
                sIP += ".";
            }
            sIP += String.valueOf(ip & 0xFF);
            ip >>= 8;
        }
        return sIP;
    }

    /**
     * 简单描述：判断是否是内网ip
     * <p/>
     * <p/>
     * tcp/ip协议中，专门保留了三个IP地址区域作为私有地址，其地址范围如下： 10.0.0.0/8：10.0.0.0～10.255.255.255 172.16.0.0/12：172.16.0.0～172.31.255.255 192.168.0.0/16：192.168.0.0～192.168.255.255
     *
     * @param ip
     * @return
     */
    public static boolean internalIp(String ip) {
        byte[] addr = IPAddressUtil.textToNumericFormatV4(ip);
        return internalIp(addr);
    }

    public static boolean internalIp(byte[] addr) {
        final byte b0 = addr[0];
        final byte b1 = addr[1];
        //10.x.x.x/8
        final byte SECTION_1 = 0x0A;
        //172.16.x.x/12
        final byte SECTION_2 = (byte) 0xAC;
        final byte SECTION_3 = (byte) 0x10;
        final byte SECTION_4 = (byte) 0x1F;
        //192.168.x.x/16
        final byte SECTION_5 = (byte) 0xC0;
        final byte SECTION_6 = (byte) 0xA8;
        switch (b0) {
            case SECTION_1:
                return true;
            case SECTION_2:
                if (b1 >= SECTION_3 && b1 <= SECTION_4) {
                    return true;
                }
            case SECTION_5:
                switch (b1) {
                    case SECTION_6:
                        return true;
                }
            default:
                return false;
        }
    }

    /**
     * 简单描述：获取局域网的地址
     * <p/>
     *
     * @return
     */
    public static String getLocalIpV4() {
        String sIP = "";
        InetAddress ip = null;
        boolean bFindIP = false;
        List<String> ipList = new ArrayList<String>();
        try {
            Enumeration netInterfaces = (Enumeration) NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
//                if (bFindIP) {
//                    break;
//                }
                NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                Enumeration ips = ni.getInetAddresses();// 遍历所有ip
                while (ips.hasMoreElements()) {
                    ip = (InetAddress) ips.nextElement();
                    if (ip instanceof Inet6Address) {
                        continue;
                    }
                    if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {// 127.开头的都是lookback地址

                        String currentIPTemp = ip.getHostAddress();

                        if (!currentIPTemp.equals("127.0.0.1") && IPUtil.internalIp(currentIPTemp)) {
                            bFindIP = true;
                            ipList.add(ip.getHostAddress());
//                            break;
                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if (bFindIP && null != ip) {
//            sIP = ip.getHostAddress();
//        }
        if (bFindIP && !Validate.isEmpty(ipList)) {
            Collections.sort(ipList);
            sIP = ipList.get(0);
        }
        return sIP;
    }

    /**
     * 简单描述：获取外网的地址
     * <p/>
     *
     * @return
     */
    public static String getOutNetIPV4() {
        String sIP = "";
        InetAddress ip = null;
        boolean bFindIP = false;
        try {
            // System.out.println("linux111");

            Enumeration netInterfaces = (Enumeration) NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                if (bFindIP) {
                    break;
                }
                NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                Enumeration ips = ni.getInetAddresses();// 遍历所有ip
                while (ips.hasMoreElements()) {
                    ip = (InetAddress) ips.nextElement();
                    // System.out.println(ip.toString() +
                    // ip.isSiteLocalAddress()+" " +
                    // ip.isLoopbackAddress()+" " +
                    // ip.getHostAddress());

                    if (ip instanceof Inet6Address) {
                        continue;
                    }
                    if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {// 127.开头的都是lookback地址

                        String currentIPTemp = ip.getHostAddress();

                        if (!currentIPTemp.equals("127.0.0.1") && !IPUtil.internalIp(currentIPTemp)) {
                            bFindIP = true;
                            break;
                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bFindIP && null != ip) {
            sIP = ip.getHostAddress();
        }
        return sIP;
    }

    public static void main(String[] args) {

        System.out.println(getLocalIpV4());
    }

}

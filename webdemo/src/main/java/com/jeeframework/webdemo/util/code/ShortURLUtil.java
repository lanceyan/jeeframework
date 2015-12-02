/**
 * @project: with 
 * @Title: ShortURLUtil.java 
 * @Package: com.webdemo.util.code
 *
 * Copyright (c) 2014-2014 Transing Limited, Inc.
 * All rights reserved.
 * 
 */
package com.jeeframework.webdemo.util.code;

import java.util.Stack;

/**
 * 短链接的生成算法
 * <p>
 * 
 * @author TODO
 * @version 1.0 2015-3-13 下午04:35:42
 */
public class ShortURLUtil {
    public static void main(String[] args) {

        System.out.println(_10_to_62(1029));
//        for (long ss = 26; ss <= 40; ss++) {
////                        System.out.println(ss);
//            System.out.println(_10_to_62(ss));
//            //            System.out.println(_62_to_10(_10_to_62(ss)));
//        }
    }

    public static final char[] array = { 'w', 'p', 'e', 'q', 'h', 'r', 't', 'y', 'u', 'i', 'o', 'a', 's', 'd', 'f', 'g', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm', '0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9', 'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Z', 'X', 'C', 'V', 'B', 'N', 'M' };

    public static String _10_to_62(long number) {
        Long rest = number;
        Stack<Character> stack = new Stack<Character>();
        StringBuilder result = new StringBuilder(0);
        while (rest != 0) {
            stack.add(array[new Long((rest - (rest / 62) * 62)).intValue()]);
            rest = rest / 62;
        }
        for (; !stack.isEmpty();) {
            result.append(stack.pop());
        }
        return result.toString();

    }

    public static long _62_to_10(String sixty_str) {
        int multiple = 1;
        long result = 0;
        Character c;
        for (int i = 0; i < sixty_str.length(); i++) {
            c = sixty_str.charAt(sixty_str.length() - i - 1);
            result += _62_value(c) * multiple;
            multiple = multiple * 62;
        }
        return result;
    }

    private static int _62_value(Character c) {
        for (int i = 0; i < array.length; i++) {
            if (c == array[i]) {
                return i;
            }
        }
        return -1;
    }
}

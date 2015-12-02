/**
 * @project: com.hyfaycrawler
 * @Title: RuleAction.java
 * @Package: com.hyfaycrawler.web.action
 * @author tag
 * Copyright (c) 2014-2014 Transing Limited, Inc.
 * All rights reserved.
 *
 */
package com.jeeframework.webdemo.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 * 
 * @author lance
 */
public class RegexUtil {

    private static final Map<Character, String> SPECIAL_REGX_ELEMENT_STABLE = new HashMap<Character, String>();

    static {
        SPECIAL_REGX_ELEMENT_STABLE.put('.', "\\.");// "\\u002E");
        SPECIAL_REGX_ELEMENT_STABLE.put(' ', "\\s");// "\\u0024");
        SPECIAL_REGX_ELEMENT_STABLE.put('-', "\\-");// "\\u005E");
        SPECIAL_REGX_ELEMENT_STABLE.put('{', "\\{");// "\\u007B");
        SPECIAL_REGX_ELEMENT_STABLE.put('}', "\\}");// "\\u007B");
        SPECIAL_REGX_ELEMENT_STABLE.put('[', "\\[");// "\\u005B");
        SPECIAL_REGX_ELEMENT_STABLE.put('(', "\\(");// "\\u0028");
        SPECIAL_REGX_ELEMENT_STABLE.put('|', "\\|");// "\\u007C");
        SPECIAL_REGX_ELEMENT_STABLE.put(')', "\\)");// "\\u0029");
        SPECIAL_REGX_ELEMENT_STABLE.put('*', ".*?");// "\\u002A");
        SPECIAL_REGX_ELEMENT_STABLE.put('+', "\\+");// "\\u002B");
        SPECIAL_REGX_ELEMENT_STABLE.put('?', ".{1}");// "\\u003F");
        SPECIAL_REGX_ELEMENT_STABLE.put('\\', "\\\\");// "\\u005C");
    }

    /**
     * 字段注释
     */
    public static final String ALLTONGPEI = "(?:\\\\s*?)";

    /**
     * 将需要正则表达式里的特殊字符转换为正常的字符
     * 
     * @param source 要编码的输入串
     * @return 编码后的串，可以直接插入HTML正文中
     */
    public static String specailRegxEncode(String source) {
        if (source == null || source.isEmpty()) {
            return source;
        }
        // pre guard

        StringBuilder sb = new StringBuilder(source.length() + 32);
        char[] chars = source.toCharArray();
        char ch;
        for (int temp_ = 0; temp_ < chars.length; ++temp_) {
            ch = chars[temp_];
            String rep = SPECIAL_REGX_ELEMENT_STABLE.get(ch);
            if (rep != null) {
                sb.append(rep);
            } else {
                sb.append(ch);
            }
        }

        return sb.toString();
    }

    /**
     * <p/>
     * 
     * @param html t
     * @param regex r
     * @return
     */
    public static String matchOne(String html, String regex) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(regex)) {
            return html;
        }
        String[] data = regex.split("#");
        if (data.length == 2) {
            return matchOne(html, data[0], data[1]);
        }
        if (data.length == 1) {
            return matchOne(html, data[0], "");
        }
        return html;
    }

    /**
     * <p/>
     * 
     * @param html t
     * @param regex r
     * @return
     */
    public static List<String> matchList(String html, String regex) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(regex)) {
            return matchList(html, "", "");
        }
        String[] data = regex.split("#");
        if (data.length == 2) {
            return matchList(html, data[0], data[1]);
        }
        if (data.length == 1) {
            return matchList(html, data[0], "");
        }
        return matchList(html, "", "");
    }

    /**
     * @param html htmls
     * @param prefix prefixs
     * @param suffix suffixs
     * @return df suffixgf
     */
    public static String matchOne(String html, String prefix, String suffix) {
        //		
        if (org.apache.commons.lang3.StringUtils.isEmpty(prefix) || org.apache.commons.lang3.StringUtils.isEmpty(suffix)) {
            html = html.replaceAll(prefix, "-+=transing=+-");
            String[] tStr = html.split("-+=transing=+-");
            if (tStr.length == 2) {
                return html.split("-+=transing=+-")[1];
            } else {
                return html.split("-+=transing=+-")[0];
            }

        }
        prefix = specailRegxEncode(prefix);
        String c = specailRegxEncode(suffix);
        String regx = prefix + "(.*?)" + c;
        String result = getCrawlRuleMatchString(html, regx, null);
        return result;
    }

    /**
     * @param html htmls
     * @param prefix prefixs
     * @param suffix suffixs
     * @return df suffixgf
     */
    public static List<String> matchList(String html, String prefix, String suffix) {
        //不需要分项
        if (org.apache.commons.lang3.StringUtils.isEmpty(prefix) && org.apache.commons.lang3.StringUtils.isEmpty(suffix)) {
            List<String> data = new ArrayList<String>(1);
            data.add(html);
            return data;
        }

        //split
        if (org.apache.commons.lang3.StringUtils.isEmpty(prefix) || org.apache.commons.lang3.StringUtils.isEmpty(suffix)) {
            return Arrays.asList(html.split(prefix));
        }
        prefix = specailRegxEncode(prefix);

        suffix = specailRegxEncode(suffix);

        String regx = prefix + "(.*?)" + suffix;
        return getCrawlRuleMatchStringForList(html, regx, null);
    }

    /**
     * <p/>
     * 
     * @param html t
     * @param regx e
     * @param curCaseSensitives x
     * @return
     */
    public static String getCrawlRuleMatchString(String html, String regx, String curCaseSensitives) {
        String retContent = "";

        html = html.replaceAll("([\\r\\t\\n]+)", "");

        Integer curPattern = Pattern.DOTALL;
        if (curCaseSensitives != null && curCaseSensitives.equals("2")) {
            curPattern = curPattern | Pattern.CASE_INSENSITIVE;
        }
        Pattern p = Pattern.compile(regx, curPattern);

        Matcher m = p.matcher(html);// 开始编译
        while (m.find()) {
            retContent = m.group(1);
            break;
        }
        return retContent;
    }

    public static String matchRegx(String content, String regx, boolean bCaseSensitives) {
        String retContent = "";

        content = content.replaceAll("([\\r\\t\\n]+)", "");

        Integer curPattern = Pattern.DOTALL;
        if (!bCaseSensitives) {
            curPattern = curPattern | Pattern.CASE_INSENSITIVE;
        }
        Pattern p = Pattern.compile(regx, curPattern);

        Matcher m = p.matcher(content);// 开始编译
        while (m.find()) {
            retContent = m.group(0);
            break;
        }
        return retContent;
    }

    /**
     * 判断是否匹配上了正则表达式
     * 
     * @param content 被匹配的内容
     * @param regx 被匹配的正则表达式
     * @param bCaseSensitives 是否区分大小写
     * @return
     */
    public static boolean isMatchRegx(String content, String regx, boolean bCaseSensitives) {

        content = content.replaceAll("([\\r\\t\\n]+)", "");

        Integer curPattern = Pattern.DOTALL;
        if (!bCaseSensitives) {
            curPattern = curPattern | Pattern.CASE_INSENSITIVE;
        }
        Pattern p = Pattern.compile(regx, curPattern);

        Matcher m = p.matcher(content);// 开始编译
        if (m.find()) {
            return true;
        }
        return false;
    }

    public static String matchRegxWithPrefix(String content, String regx, boolean bCaseSensitives) {
        String retContent = "";

        content = content.replaceAll("([\\r\\t\\n]+)", "");

        Integer curPattern = Pattern.DOTALL;
        if (!bCaseSensitives) {
            curPattern = curPattern | Pattern.CASE_INSENSITIVE;
        }
        Pattern p = Pattern.compile(regx, curPattern);

        Matcher m = p.matcher(content);// 开始编译
        while (m.find()) {
            retContent = m.group(1);
            break;
        }
        return retContent;
    }

    /**
     * <p/>
     * 
     * @param html t
     * @param regx e
     * @param bCaseSensitives x
     * @return
     */
    public static List<String> matchRegxList(String html, String regx, boolean bCaseSensitives) {
        List<String> retList = new ArrayList<String>();

        html = html.replaceAll("([\\r\\t\\n]+)", "");

        Integer curPattern = Pattern.DOTALL;
        if (!bCaseSensitives) {
            curPattern = curPattern | Pattern.CASE_INSENSITIVE;
        }
        Pattern p = Pattern.compile(regx, curPattern);// 匹配<title>开头，</title>结尾的文档,Pattern.DOTALL

        Matcher m = p.matcher(html);// 开始编译
        while (m.find()) {
            // 不获取第一个整个表达式，只是获取里面的值就可以了
            // 第0个是整个表达式,只要找到了，就有个位置为1的绝对串
            //            int groupCount = m.groupCount();
            //            for (int countTmp = 0; countTmp <= groupCount; countTmp++) {
            String content = m.group(1);
            retList.add(content);
            //            }
        }
        return retList;
    }

    /**
     * <p/>
     * 
     * @param html t
     * @param regx e
     * @param curCaseSensitives x
     * @return
     */
    public static List<String> getCrawlRuleMatchStringForList(String html, String regx, String curCaseSensitives) {
        List<String> retList = new ArrayList<String>();

        html = html.replaceAll("([\\r\\t\\n]+)", "");

        Integer curPattern = Pattern.DOTALL;
        if (curCaseSensitives != null && curCaseSensitives.equals("2")) {
            curPattern = curPattern | Pattern.CASE_INSENSITIVE;
        }
        Pattern p = Pattern.compile(regx, curPattern);// 匹配<title>开头，</title>结尾的文档,Pattern.DOTALL

        Matcher m = p.matcher(html);// 开始编译
        while (m.find()) {
            // 不获取第一个整个表达式，只是获取里面的值就可以了
            // 第0个是整个表达式,只要找到了，就有个位置为1的绝对串
            String content = m.group(1);
            retList.add(content);
        }
        return retList;
    }

    /**
     * <p/>
     * 
     * @param args
     */
    public static void main(String[] args) {
        //        List<String> filterIdStrTmpList = RegexUtil.matchRegx("$f13333d", "\\$f(\\d+)", false);
        //
        //        if (!Validate.isEmpty(filterIdStrTmpList) && filterIdStrTmpList.size() > 1) {
        //            System.out.println(filterIdStrTmpList);
        //        }

        Date date = new Date(0);
        System.out.println(date);

        String strTmp = "转发(123)";//RegexUtil.matchRegxWithPrefix("$f13333d", "\\$f(\\d+)", false);
        System.out.println(RegexUtil.matchRegxWithPrefix(strTmp, "(\\d+)", false));

        String regEx = "<meta.*charset=[\\'|\\\"]?([[a-z]|[A-Z]|[0-9]|-]*).*>";
        strTmp = RegexUtil.matchRegxWithPrefix("aasdfsd<meta charset=\"gbk\">fsfsdfsdf<body>sdfsdfsd<html>", regEx, false);
        System.out.println(strTmp);

        strTmp = RegexUtil.matchRegxWithPrefix("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />", regEx, false);
        System.out.println(strTmp);

        String html = "<content></content>";
        String str = "\\{\"items\":(.*?)true\\}";
    }

}

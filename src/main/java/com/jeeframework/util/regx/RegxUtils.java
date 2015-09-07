package com.jeeframework.util.regx;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegxUtils {
	/**
	 * html元素过滤字符集合
	 */
	private static final Map<Character, String> specialRegxElementsTable = new HashMap<Character, String>();
	static {
		specialRegxElementsTable.put('.', "\\.");// "\\u002E");
		specialRegxElementsTable.put('$', "\\$");// "\\u0024");
		specialRegxElementsTable.put('^', "\\^");// "\\u005E");
		specialRegxElementsTable.put('{', "\\{");// "\\u007B");
		specialRegxElementsTable.put('[', "\\[");// "\\u005B");
		specialRegxElementsTable.put('(', "\\(");// "\\u0028");
		specialRegxElementsTable.put('|', "\\|");// "\\u007C");
		specialRegxElementsTable.put(')', "\\)");// "\\u0029");
		specialRegxElementsTable.put('*', "\\*");// "\\u002A");
		specialRegxElementsTable.put('+', "\\+");// "\\u002B");
		specialRegxElementsTable.put('?', "\\?");// "\\u003F");
		specialRegxElementsTable.put('\\', "\\\\");// "\\u005C");
	}

	public static final String allTongpei = "(?:\\\\s*?)";


	/**
	 * 将需要正则表达式里的特殊字符转换为正常的字符
	 * 
	 * 
	 * @param source
	 *            要编码的输入串
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
		for (int _ = 0; _ < chars.length; ++_) {
			ch = chars[_];
			String rep = specialRegxElementsTable.get(ch);
			if (rep != null) {
				sb.append(rep);
			} else {
				sb.append(ch);
			}
		}

		return sb.toString();

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
}

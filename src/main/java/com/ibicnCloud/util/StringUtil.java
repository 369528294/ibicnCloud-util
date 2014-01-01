package com.ibicnCloud.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class StringUtil extends StringUtils{
	
	/**
	 * 获取数组的长度
	 * @param object
	 * @return
	 */
	public static int size(Object[] object) {
		if (CollectionUtil.isEmpty(object))
			return 0;
		else
			return object.length;
	}
	
	public static int size(String str) {
		if (isEmpty(str))
			return 0;
		else
			return str.length();
	}
	
	/**
	 * 检测某个字符变量是否为空；<br>
	 * 为空的情况，包括：null，空串或只包含可以被 trim() 的字符；
	 */
	public static boolean isEmpty(String value) {
		if (value == null || value.trim().length() == 0)
			return true;
		else
			return false;
	}

	public static boolean isEmpty(Object[] array) {
		if (array == null || array.length == 0)
			return true;
		else
			return false;
	}

	public static boolean isEmpty(StringBuffer sb) {
		if (sb == null || sb.length() == 0)
			return true;
		else
			return false;
	}

	/**
	 * 格式化字符串显示；
	 */
	public static String format(String value) {
		return format(value, "");
	}

	public static String format(String value, String defaultValue) {
		if (isEmpty(value))
			return defaultValue;
		else
			return value.trim();
	}
	
	/**
	 * 格式化字符串
	 * 
	 * 例：formateString("xxx{0}bbb",1) = xxx1bbb
	 * 
	 * @param str
	 * @param params
	 * @return
	 */
	public static String formateString(String str, String... params) {
		for (int i = 0; i < params.length; i++) {
			str = str.replace("{" + i + "}", params[i] == null ? "" : params[i]);
		}
		return str;
	}
	
	/**
	 * 解析一个字符串为整数；
	 */
	public final static int parseInt(String value) {
		if (isInt(value))
			return Integer.parseInt(value);
		return 0;
	}
	
	/**
	 * 检测变量的值是否为一个整型数据；
	 */
	public final static boolean isInt(String value) {
		if (isEmpty(value))
			return false;

		try {
			Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return false;
		}

		return true;
	}
	
	/**
	 * 大写转下划线，驼峰转下划线
	 * @param param
	 * @return
	 */
	public static String camel4underline(String param) {
		Pattern p = Pattern.compile("[A-Z]");
		if (param == null || param.equals("")) {
			return "";
		}
		StringBuilder builder = new StringBuilder(param);
		Matcher mc = p.matcher(param);
		int i = 0;
		while (mc.find()) {
			builder.replace(mc.start() + i, mc.end() + i, "_" + mc.group().toLowerCase());
			i++;
		}

		if ('_' == builder.charAt(0)) {
			builder.deleteCharAt(0);
		}
		return builder.toString();
	}

}

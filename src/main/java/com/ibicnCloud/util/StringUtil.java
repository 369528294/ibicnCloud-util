package com.ibicnCloud.util;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.apache.commons.lang3.StringUtils;

public class StringUtil extends StringUtils {

	/**
	 * 获取数组的长度
	 *
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
			str = str
					.replace("{" + i + "}", params[i] == null ? "" : params[i]);
		}
		return str;
	}

	/**
	 * 判断变量的值是否为float类型
	 */
	public final static boolean isFloat(String value) {
		if (isEmpty(value))
			return false;
		try {
			Float.parseFloat(value);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
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
	 * 解析一个字符串为整数；
	 */
	public final static int parseInt(Integer value) {
		if (value != null)
			return value;
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
	 *
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
			builder.replace(mc.start() + i, mc.end() + i, "_"
					+ mc.group().toLowerCase());
			i++;
		}

		if ('_' == builder.charAt(0)) {
			builder.deleteCharAt(0);
		}
		return builder.toString();
	}

	public static String getUUID() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 页面提示框；<br>
	 * 用在页面上控制页面操作流程；通过 javascript 脚本实现；
	 */
	public final static String alert(String message, String action) {
		StringBuffer sb = new StringBuffer();

		if (isEmpty(message)) {
			sb.append("<script>");
		} else {
			message = message.replace("'", "‘");
			message = message.replace("\"", "“");
			sb.append("<script>alert('" + message + "\\t\\n');");
		}

		if ("close".equalsIgnoreCase(action)) {
			sb.append("window.close();");
		} else if ("back".equalsIgnoreCase(action)) {
			sb.append("history.go(-1);");
		} else {
			if (action.contains("#parentForward")) {
				action = action.replace("#parentForward", "");
				sb.append("parent.document.location='" + action + "'");
			} else {
				sb.append("document.location='" + action + "'");
			}
		}
		sb.append("</script>");

		return sb.toString();
	}

	/**
	 * 页面提示框；<br>
	 * 用在页面上控制页面操作流程；通过 javascript 脚本实现；
	 */
	public final static String easyUIAlert(String message, String action) {
		StringBuffer sb = new StringBuffer();
		if (isEmpty(message)) {
			sb.append("<script>");
		} else {
			message = message.replace("'", "‘");
			message = message.replace("\"", "“");
			sb.append("<script>" + "if (parent.$ && parent.$.messager) {"
					+ "	parent.$.messager.progress('close');"
					+ "	parent.$.messager.alert('提示', '" + message + "');"
					+ "} else {" + "	$.messager.progress('close');"
					+ "	$.messager.alert('提示', '" + message + "');" + "}");
		}
		if ("close".equalsIgnoreCase(action)) {
			sb.append("window.close();");
		} else if ("back".equalsIgnoreCase(action)) {
			sb.append("history.go(-1);");
		} else {
			sb.append("document.location='" + action + "'");
		}
		sb.append("</script>");

		return sb.toString();
	}

	public static Long parseLong(int recordCount) {
		try {
			long l = Long.parseLong(recordCount + "");
			return l;
		} catch (Exception e) {
			return 0L;
		}
	}

	/**
	 * 将字符串转换成拼音数组
	 *
	 * @param src
	 * @return
	 */
	public static String[] stringToPinyin(String src) {
		return stringToPinyin(src, false, null);
	}

	/**
	 * 将字符串转换成拼音数组
	 *
	 * @param src
	 * @return
	 */
	public static String[] stringToPinyin(String src, String separator) {

		return stringToPinyin(src, true, separator);
	}

	/**
	 * 将字符串转换成拼音数组
	 *
	 * @param src
	 * @param isPolyphone
	 *            是否查出多音字的所有拼音
	 * @param separator
	 *            多音字拼音之间的分隔符
	 * @return
	 */
	public static String[] stringToPinyin(String src, boolean isPolyphone,
										  String separator) {
		// 判断字符串是否为空
		if ("".equals(src) || null == src) {
			return null;
		}
		char[] srcChar = src.toCharArray();
		int srcCount = srcChar.length;
		String[] srcStr = new String[srcCount];

		for (int i = 0; i < srcCount; i++) {
			srcStr[i] = charToPinyin(srcChar[i], isPolyphone, separator);
		}
		return srcStr;
	}

	/**
	 * 将单个字符转换成拼音
	 *
	 * @param src
	 * @return
	 */
	public static String charToPinyin(char src, boolean isPolyphone,
									  String separator) {
		// 创建汉语拼音处理类
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		// 输出设置，大小写，音标方式
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

		StringBuffer tempPinying = new StringBuffer();

		// 如果是中文
		if (src > 128) {
			try {
				// 转换得出结果
				String[] strs = PinyinHelper.toHanyuPinyinStringArray(src,
						defaultFormat);

				// 是否查出多音字，默认是查出多音字的第一个字符
				if (isPolyphone && null != separator) {
					for (int i = 0; i < strs.length; i++) {
						tempPinying.append(strs[i]);
						if (strs.length != (i + 1)) {
							// 多音字之间用特殊符号间隔起来
							tempPinying.append(separator);
						}
					}
				} else {
					tempPinying.append(strs[0]);
				}

			} catch (BadHanyuPinyinOutputFormatCombination e) {
				e.printStackTrace();
			}
		} else {
			tempPinying.append(src);
		}

		return tempPinying.toString();

	}

	public static String hanziToPinyin(String hanzi) {
		return hanziToPinyin(hanzi, " ");
	}

	/**
	 * 将汉字转换成拼音
	 *
	 * @param hanzi
	 * @param separator 多音字拼音之间的分隔符
	 * @return
	 */
	public static String hanziToPinyin(String hanzi, String separator) {

		// 创建汉语拼音处理类
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		// 输出设置，大小写，音标方式
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

		String pinyingStr = "";
		try {
			pinyingStr = PinyinHelper.toHanyuPinyinString(hanzi, defaultFormat,
					separator);
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pinyingStr;
	}

	/**
	 * 将字符串数组转换成字符串
	 *
	 * @param str
	 * @param separator
	 *            各个字符串之间的分隔符
	 * @return
	 */
	public static String stringArrayToString(String[] str, String separator) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length; i++) {
			sb.append(str[i]);
			if (str.length != (i + 1)) {
				sb.append(separator);
			}
		}
		return sb.toString();
	}

	/**
	 * 简单的将各个字符数组之间连接起来
	 *
	 * @param str
	 * @return
	 */
	public static String stringArrayToString(String[] str) {
		return stringArrayToString(str, "");
	}

	/**
	 * 将字符数组转换成字符串
	 *
	 * @param str
	 * @param separator
	 *            各个字符串之间的分隔符
	 * @return
	 */
	public static String charArrayToString(char[] ch, String separator) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ch.length; i++) {
			sb.append(ch[i]);
			if (ch.length != (i + 1)) {
				sb.append(separator);
			}
		}
		return sb.toString();
	}

	/**
	 * 将字符数组转换成字符串
	 *
	 * @param str
	 * @return
	 */
	public static String charArrayToString(char[] ch) {
		return charArrayToString(ch, " ");
	}

	/**
	 * 取汉字的首字母
	 *
	 * @param src
	 * @param isCapital
	 *            是否是大写
	 * @return
	 */
	public static char[] getHeadByChar(char src, boolean isCapital) {
		// 如果不是汉字直接返回
		if (src <= 128) {
			return new char[] { src };
		}
		// 获取所有的拼音
		String[] pinyingStr = PinyinHelper.toHanyuPinyinStringArray(src);

		// 创建返回对象
		int polyphoneSize = pinyingStr.length;
		char[] headChars = new char[polyphoneSize];
		int i = 0;
		// 截取首字符
		for (String s : pinyingStr) {
			char headChar = s.charAt(0);
			// 首字母是否大写，默认是小写
			if (isCapital) {
				headChars[i] = Character.toUpperCase(headChar);
			} else {
				headChars[i] = headChar;
			}
			i++;
		}

		return headChars;
	}

	/**
	 * 取汉字的首字母(默认是大写)
	 *
	 * @param src
	 * @return
	 */
	public static char[] getHeadByChar(char src) {
		return getHeadByChar(src, true);
	}

	/**
	 * 查找字符串首字母
	 *
	 * @param src
	 * @return
	 */
	public static String[] getHeadByString(String src) {
		return getHeadByString(src, true);
	}

	/**
	 * 查找字符串首字母
	 *
	 * @param src
	 * @param isCapital
	 *            是否大写
	 * @return
	 */
	public static String[] getHeadByString(String src, boolean isCapital) {
		return getHeadByString(src, isCapital, null);
	}

	/**
	 * 查找字符串首字母
	 *
	 * @param src
	 * @param isCapital
	 *            是否大写
	 * @param separator
	 *            分隔符
	 * @return
	 */
	public static String[] getHeadByString(String src, boolean isCapital,
										   String separator) {
		char[] chars = src.toCharArray();
		String[] headString = new String[chars.length];
		int i = 0;
		for (char ch : chars) {

			char[] chs = getHeadByChar(ch, isCapital);
			StringBuffer sb = new StringBuffer();
			if (null != separator) {
				int j = 1;

				for (char ch1 : chs) {
					sb.append(ch1);
					if (j != chs.length) {
						sb.append(separator);
					}
					j++;
				}
			} else {
				sb.append(chs[0]);
			}
			headString[i] = sb.toString();
			i++;
		}
		return headString;
	}

	/**
	 * 按照长度截取字符串，能截取中文字符
	 */
	public static String substrGB(String text, int length) {
		String sRet = "";
		if (isEmpty(text))
			return "";
		if (text.length() <= length)
			sRet = text;
		else
			sRet = text.substring(0, length) + "...";
		return sRet;
	}

	public static String substr(String text, int length) {
		String sRet = "";
		if (isEmpty(text))
			return "";
		if (text.length() <= length)
			sRet = text;
		else
			sRet = text.substring(0, length);
		return sRet;
	}

	/**
	 * 处理url地址中传递中文问题
	 *
	 * @param str
	 * @param format
	 * @return
	 */
	public static String format(String str, boolean format) {
		str = StringUtil.format(str);
//		if (format) {
//			try {
//				str = new String(str.getBytes("iso8859_1"), "UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				str = "";
//			}
//		}
		if (isMessyCode(str)) {
			try {
				return new String(str.getBytes("ISO8859-1"), "UTF-8");
			} catch (Exception e) {
				return "";
			}
		}
		return str;
	}

	/**
	 * 判断是否是乱码
	 *
	 * @param strName
	 * @return
	 */
	public static boolean isMessyCode(String strName) {
		Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
		Matcher m = p.matcher(strName);
		String after = m.replaceAll("");
		String temp = after.replaceAll("\\p{P}", "");
		char[] ch = temp.trim().toCharArray();
		float chLength = 0;
		float count = 0;
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (!Character.isLetterOrDigit(c)) {
				if (!isChinese(c)) {
					count = count + 1;
				}
				chLength++;
			}
		}
		float result = count / chLength;
		if (result > 0.4) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否是中文
	 *
	 * @param c
	 * @return
	 */
	private static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	/**
	 * 从指定的字符列表中生成随机字符串
	 */
	public final static String getRandomString(int length) {
		final String[] s = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };

		if (length < 1)
			return "";

		StringBuffer sb = new StringBuffer(length);
		for (int i = 0; i < length; i++) {
			int position = getRandomNumber(s.length - 1);
			Collections.shuffle(Arrays.asList(s));
			sb.append(s[position]);
		}

		return sb.toString();
	}

	/**
	 * 生成一个随机数字；
	 */
	public final static int getRandomNumber(int max) {
		return getRandomNumber(0, max);
	}

	public final static int getRandomNumber(int min, int max) {
		if (min > max) {
			int k = min;
			min = max;
			max = k;
		}
		Random random = new Random();
		return (random.nextInt(max - min) + min);
	}

	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public static void main(String[] args) {
		System.out.println(getRandomString(4));
	}
}

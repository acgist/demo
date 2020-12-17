package com.acgist.utils;

import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>字符串工具</p>
 * 
 * @author acgist
 */
public final class StringUtils {

	/**
	 * <p>工具禁止创建实例</p>
	 */
	private StringUtils() {
	}
	
	/**
	 * <p>数字正则表达式（正负整数）：{@value}</p>
	 */
	private static final String NUMERIC_REGEX = "\\-?[0-9]+";
	/**
	 * <p>数字正则表达式（正负小数、正负整数）：{@value}</p>
	 */
	private static final String DECIMAL_REGEX = "\\-?[0-9]+(\\.[0-9]+)?";
	
	/**
	 * <p>字符串是否为空</p>
	 * 
	 * @param value 字符串
	 * 
	 * @return true-空；false-非空；
	 */
	public static final boolean isEmpty(String value) {
		return value == null || value.isEmpty();
	}

	/**
	 * <p>字符串是否非空</p>
	 * 
	 * @param value 字符串
	 * 
	 * @return true-非空；false-空；
	 */
	public static final boolean isNotEmpty(String value) {
		return !isEmpty(value);
	}
	
	/**
	 * <p>判断字符串是不是{@linkplain #NUMERIC_REGEX 数值}</p>
	 * <p>正负整数</p>
	 * 
	 * @param value 字符串
	 * 
	 * @return true-是；false-不是；
	 */
	public static final boolean isNumeric(String value) {
		return StringUtils.regex(value, NUMERIC_REGEX, true);
	}

	/**
	 * <p>判断字符串是不是{@linkplain #DECIMAL_REGEX 数值}</p>
	 * <p>正负小数、正负整数</p>
	 * 
	 * @param value 字符串
	 * 
	 * @return true-是；false-不是；
	 */
	public static final boolean isDecimal(String value) {
		return StringUtils.regex(value, DECIMAL_REGEX, true);
	}
	
	/**
	 * <p>判断字符串是不是以前缀开始</p>
	 * 
	 * @param value 字符串
	 * @param prefix 前缀
	 * 
	 * @return true-是；false-不是；
	 */
	public static final boolean startsWith(String value, String prefix) {
		return value != null && prefix != null && value.startsWith(prefix);
	}
	
	/**
	 * <p>判断字符串是不是以后缀结束</p>
	 * 
	 * @param value 字符串
	 * @param suffix 后缀
	 * 
	 * @return true-是；false-不是；
	 */
	public static final boolean endsWith(String value, String suffix) {
		return value != null && suffix != null && value.endsWith(suffix);
	}
	
	/**
	 * <p>将字节数组转为十六进制字符串</p>
	 * 
	 * @param bytes 字节数组
	 * 
	 * @return 十六进制字符串
	 */
	public static final String hex(byte[] bytes) {
		if(bytes == null) {
			return null;
		}
		String hex;
		final StringBuilder builder = new StringBuilder();
		for (int index = 0; index < bytes.length; index++) {
			hex = Integer.toHexString(bytes[index] & 0xFF);
			if (hex.length() < 2) {
				builder.append("0");
			}
			builder.append(hex);
		}
		return builder.toString().toLowerCase();
	}
	
	/**
	 * <p>将十六进制字符串转为字节数组</p>
	 * 
	 * @param content 十六进制字符串
	 * 
	 * @return 字节数组
	 */
	public static final byte[] unhex(String content) {
		if(content == null) {
			return null;
		}
		int length = content.length();
		if (length % 2 == 1) {
			// 奇数
			length++;
			content = "0" + content;
		}
		int jndex = 0;
		final byte[] result = new byte[length / 2];
		for (int index = 0; index < length; index += 2) {
			result[jndex] = (byte) Integer.parseInt(content.substring(index, index + 2), 16);
			jndex++;
		}
		return result;
	}

	/**
	 * <p>计算字节数组的SHA-1散列值</p>
	 * 
	 * @param bytes 字节数组
	 * 
	 * @return SHA-1散列值
	 */
	public static final byte[] sha1(byte[] bytes) {
		final MessageDigest digest = DigestUtils.sha1();
		digest.update(bytes);
		return digest.digest();
	}
	
	/**
	 * <p>计算字节数组的SHA-1散列值并转为十六进制字符串</p>
	 * 
	 * @param bytes 字节数组
	 * 
	 * @return 十六进制SHA-1散列值字符串
	 */
	public static final String sha1Hex(byte[] bytes) {
		return StringUtils.hex(sha1(bytes));
	}
	
	/**
	 * <p>判断字符串是否匹配正则表达式</p>
	 * 
	 * @param value 字符串
	 * @param regex 正则表达式
	 * @param ignoreCase 是否忽略大小写
	 * 
	 * @return true-匹配；false-不匹配；
	 */
	public static final boolean regex(String value, String regex, boolean ignoreCase) {
		if(value == null || regex == null) {
			return false;
		}
		Pattern pattern;
		if(ignoreCase) {
			pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		} else {
			pattern = Pattern.compile(regex);
		}
		final Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}
	
	/**
	 * <p>判断字符串是否相等</p>
	 * 
	 * @param source 原始字符串
	 * @param target 目标字符串
	 * 
	 * @return true-相等；false-不等；
	 */
	public static final boolean equals(String source, String target) {
		if(source == null) {
			return target == null;
		} else {
			return source.equals(target);
		}
	}
	
	/**
	 * <p>判断字符串是否相等（忽略大小写）</p>
	 * 
	 * @param source 原始字符串
	 * @param target 目标字符串
	 * 
	 * @return true-相等；false-不等；
	 */
	public static final boolean equalsIgnoreCase(String source, String target) {
		if(source == null) {
			return target == null;
		} else {
			return source.equalsIgnoreCase(target);
		}
	}

	/**
	 * <p>将字符串转换为Unicode字符串</p>
	 * 
	 * @param content 字符串
	 * 
	 * @return Unicode字符串
	 */
	public static final String toUnicode(String content) {
		char value;
		final StringBuilder builder = new StringBuilder();
		for (int index = 0; index < content.length(); index++) {
			value = content.charAt(index);
			builder.append("\\u");
			if(value <= 0xFF) {
				builder.append("00");
			}
			builder.append(Integer.toHexString(value));
		}
		return builder.toString();
	}
	
	/**
	 * <p>读取Unicode字符串</p>
	 * 
	 * @param unicode Unicode字符串
	 * 
	 * @return 字符串
	 */
	public static final String ofUnicode(String unicode) {
		final String[] hex = unicode.split("\\\\u");
		final StringBuilder builder = new StringBuilder();
		for (int index = 1; index < hex.length; index++) {
			builder.append((char) Integer.parseInt(hex[index], 16));
		}
		return builder.toString();
	}
	
	/**
	 * <p>去掉字符串所有空白字符</p>
	 * 
	 * @param content 原始内容
	 * 
	 * @return 去掉空白字符的字符串
	 */
	public static final String trimAllBlank(String content) {
		if(content == null) {
			return content;
		}
		return content.replaceAll("\\s", "");
	}
	
}

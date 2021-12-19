package com.acgist.ding;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 字符串工具
 * 
 * @author acgist
 */
public final class Strings {
	
	private Strings() {
	}
	
	/**
	 * 文本转为Unicode
	 * 
	 * @param content 原始文本
	 * 
	 * @return Unicode文本
	 */
	public static final String toUnicode(String content) {
		int length;
		char value;
		String hex;
		final int charHexLength = Integer.BYTES;
		final StringBuilder builder = new StringBuilder();
		for (int index = 0; index < content.length(); index++) {
			builder.append("\\u");
			value = content.charAt(index);
			hex = Integer.toHexString(value);
			length = hex.length();
			if(length < charHexLength) {
				builder.append("0".repeat(charHexLength - length));
			}
			builder.append(Integer.toHexString(value));
		}
		return builder.toString();
	}
	
	/**
	 * Unicode转为文本
	 * 
	 * @param unicode Unicode文本
	 * 
	 * @return 文本
	 */
	public static final String ofUnicode(String unicode) {
		final String[] hex = unicode.split("\\\\u");
		final StringBuilder builder = new StringBuilder();
		for (int index = 1; index < hex.length; index++) {
			// 去掉首个空白字符
			builder.append((char) Integer.parseInt(hex[index], Character.SIZE));
		}
		return builder.toString();
	}
	
	/**
	 * URL编码
	 * 
	 * @param value 文本
	 * 
	 * @return URL文本
	 */
	public static final String urlEncode(String value) {
		return URLEncoder.encode(value, StandardCharsets.UTF_8);
	}
	
	/**
	 * URL解码
	 * 
	 * @param value URL文本
	 * 
	 * @return 文本
	 */
	public static final String urlDecode(String value) {
		return URLDecoder.decode(value, StandardCharsets.UTF_8);
	}
	
	/**
	 * 判断文本是否为空
	 * 
	 * @param value 文本
	 * 
	 * @return 是否为空
	 */
	public static final boolean isEmpty(String value) {
		return value == null || value.isEmpty();
	}
	
	/**
	 * 判断文本是否不为空
	 * 
	 * @param value 文本
	 * 
	 * @return 是否不为空
	 */
	public static final boolean isNotEmpty(String value) {
		return !Strings.isEmpty(value);
	}
	
	/**
	 * HEX数据编码
	 * 
	 * @param bytes 原始数据
	 * 
	 * @return HEX文本
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
	 * HEX文本解码
	 * 
	 * @param content HEX文本
	 * 
	 * @return 原始数据
	 */
	public static final byte[] unhex(String content) {
		if(content == null) {
			return null;
		}
		// byte十六进制长度
		final int byteHexLength = 2;
		int length = content.length();
		if (length % byteHexLength != 0) {
			// 填充字符
			length++;
			content = "0" + content;
		}
		int jndex = 0;
		final byte[] hexBytes = new byte[length / byteHexLength];
		for (int index = 0; index < length; index += byteHexLength) {
			hexBytes[jndex] = (byte) Integer.parseInt(content.substring(index, index + byteHexLength), 16);
			jndex++;
		}
		return hexBytes;
	}
	
	/**
	 * 按行读取文本
	 * 
	 * @param value 文本
	 * 
	 * @return 每行数据
	 */
	public static final String[] readLine(String value) {
		if(value.indexOf(Config.NEW_LINE) >= 0) {
			return value.split(Config.NEW_LINE);
		} else if(value.indexOf(Config.NEW_LINE_N) >= 0) {
			return value.split(Config.NEW_LINE_N);
		} else if(value.indexOf(Config.NEW_LINE_R) >= 0) {
			return value.split(Config.NEW_LINE_R);
		}
		return new String[] { value };
	}
	
}

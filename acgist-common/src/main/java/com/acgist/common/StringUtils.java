package com.acgist.common;

public class StringUtils {

	/**
	 * <p>字符串是否为空</p>
	 * 
	 * @param value 字符串
	 * 
	 * @return {@code true}-空；{@code false}-非空；
	 */
	public static final boolean isEmpty(String value) {
		return value == null || value.isEmpty();
	}

	/**
	 * <p>字符串是否非空</p>
	 * 
	 * @param value 字符串
	 * 
	 * @return {@code true}-非空；{@code false}-空；
	 */
	public static final boolean isNotEmpty(String value) {
		return !isEmpty(value);
	}
	
}

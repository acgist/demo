package com.acgist.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>URL工具</p>
 * 
 * @author acgist
 */
public final class UrlUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(UrlUtils.class);
	
	/**
	 * <p>工具禁止创建实例</p>
	 */
	private UrlUtils() {
	}
	
	/**
	 * <p>URL编码</p>
	 * 
	 * @param content 待编码内容
	 * 
	 * @return 编码后内容
	 */
	public static final String encode(String content) {
		try {
			return URLEncoder
				.encode(content, "UTF-8")
				.replace("+", "%20"); // 空格编码变成加号：加号解码变成空格
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("URL编码异常：{}", content, e);
		}
		return content;
	}
	
	/**
	 * <p>URL解码</p>
	 * 
	 * @param content 待解码内容
	 * 
	 * @return 解码后内容
	 */
	public static final String decode(String content) {
		try {
			return URLDecoder.decode(content, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("URL解码异常：{}", content, e);
		}
		return content;
	}
	
}

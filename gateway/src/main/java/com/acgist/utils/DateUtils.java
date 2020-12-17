package com.acgist.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日期工具
 */
public class DateUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);
	
	public static final String FORMAT_PATTERN = "yyyyMMddHHmmss";
	
	/**
	 * 接口时间
	 */
	public static final String apiTime() {
		return apiTime(new Date());
	}
	
	/**
	 * 接口时间
	 */
	public static final String apiTime(Date date) {
		if(date == null) {
			return null;
		}
		final SimpleDateFormat formater = new SimpleDateFormat(FORMAT_PATTERN);
		return formater.format(date);
	}
	
	/**
	 * 接口时间
	 */
	public static final Date apiTime(String time) {
		if(time == null) {
			return null;
		}
		final SimpleDateFormat formater = new SimpleDateFormat(FORMAT_PATTERN);
		try {
			return formater.parse(time);
		} catch (ParseException e) {
			LOGGER.error("时间格式化异常，时间字符串：{}", time, e);
		}
		return null;
	}
	
}

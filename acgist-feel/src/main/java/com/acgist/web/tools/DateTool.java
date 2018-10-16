package com.acgist.web.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具
 */
public class DateTool {

	// 日期格式
	public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
	
	/**
	 * 当前时间格式化：yyyyMMdd
	 */
	public static final String formatNow() {
		return format(new Date(), DATE_FORMAT_YYYYMMDD);
	}
	
	public static final String format(Date date, String format) {
		if(date == null || format == null) {
			return null;
		}
		SimpleDateFormat formater = new SimpleDateFormat(format);
		return formater.format(date);
	}
	
}

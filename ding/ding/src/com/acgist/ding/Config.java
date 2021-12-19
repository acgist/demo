package com.acgist.ding;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 配置读取
 * 
 * @author acgist
 */
public final class Config {
	
	private Config() {
	}
	
	/**
	 * 版本
	 */
	public static final String VERSION = "1.0.3";
	/**
	 * 空白字符
	 */
	public static final String EMPTY = "";
	/**
	 * 换行
	 */
	public static final String NEW_LINE = "\r\n";
	/**
	 * 换行
	 */
	public static final String NEW_LINE_N = "\n";
	/**
	 * 换行
	 */
	public static final String NEW_LINE_R = "\r";
	/**
	 * 定时任务
	 */
	public static final ScheduledExecutorService SCHEDULED = Executors.newScheduledThreadPool(2);
	
}

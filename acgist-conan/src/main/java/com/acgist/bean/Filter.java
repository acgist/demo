package com.acgist.bean;

/**
 * 过滤条件
 */
public enum Filter {

	nofilter, // 不过滤
	
	chinese, // 保留中文
	number, // 保留数字
	english, // 保留英文
	
	length; // 去掉长度小于二的字符
	
}

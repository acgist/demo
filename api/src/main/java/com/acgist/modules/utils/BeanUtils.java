package com.acgist.modules.utils;

import org.springframework.context.ApplicationContext;

/**
 * bean工具
 */
public class BeanUtils {

	/**
	 * 获取对应的类
	 */
	public static final <T> T getInstance(ApplicationContext context, Class<T> clazz) {
		return context.getBean(clazz);
	}

}

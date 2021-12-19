package com.acgist.ding;

import java.util.Collection;

/**
 * 集合工具
 * 
 * @author acgist
 */
public class Collections {

	private Collections() {
	}

	/**
	 * 验证集合是否为空
	 * 
	 * @param collection 集合
	 * 
	 * @return 是否为空
	 */
	public static final boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}
	
	/**
	 * 验证集合是否不为空
	 * 
	 * @param collection 集合
	 * 
	 * @return 是否不为空
	 */
	public static final boolean isNotEmpty(Collection<?> collection) {
		return !Collections.isEmpty(collection);
	}
	
}

package com.acgist.ding;

/**
 * 数组工具
 * 
 * @author acgist
 */
public class Arrays {

	private Arrays() {
	}

	/**
	 * 验证数组是否为空
	 * 
	 * @param array 数组
	 * 
	 * @return 是否为空
	 */
	public static final boolean isEmpty(Object[] array) {
		return array == null || array.length == 0;
	}
	
	/**
	 * 验证数组是否不为空
	 * 
	 * @param array 数组
	 * 
	 * @return 是否不为空
	 */
	public static final boolean isNotEmpty(Object[] array) {
		return !Arrays.isEmpty(array);
	}
	
}

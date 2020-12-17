package com.acgist.utils;

import java.util.Objects;

/**
 * <p>枚举工具</p>
 * 
 * @author acgist
 */
public final class EnumUtils {

	/**
	 * <p>工具禁止创建实例</p>
	 */
	private EnumUtils() {
	}

	/**
	 * <p>枚举拆包</p>
	 * 
	 * @param clazz 类型
	 * @param value 数据
	 * 
	 * @return 枚举
	 */
	public static final Object unpack(Class<?> clazz, Object value) {
		Objects.requireNonNull(clazz);
		if(clazz.isEnum()) {
			final Object[] enums = clazz.getEnumConstants();
			for (Object object : enums) {
				if(object.toString().equals(value.toString())) {
					return object;
				}
			}
		}
		return null;
	}
	
}

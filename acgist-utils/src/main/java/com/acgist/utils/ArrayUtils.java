package com.acgist.utils;

import java.util.Objects;
import java.util.Random;

/**
 * <p>数组工具</p>
 * 
 * @author acgist
 */
public final class ArrayUtils {

	/**
	 * <p>工具禁止创建实例</p>
	 */
	private ArrayUtils() {
	}
	
	/**
	 * <p>数组异或运算</p>
	 * 
	 * @param sources 原始数据
	 * @param targets 目标数据
	 * 
	 * @return 结果数组
	 */
	public static final byte[] xor(byte[] sources, byte[] targets) {
		Objects.requireNonNull(sources);
		Objects.requireNonNull(targets);
		if (sources.length != targets.length) {
			throw new IllegalArgumentException("异或运算参数错误（长度）");
		} else {
			final int length = sources.length;
			final byte[] result = new byte[length];
			for (int index = 0; index < length; index++) {
				result[index] = (byte) (sources[index] ^ targets[index]);
			}
			return result;
		}
	}
	
	/**
	 * <p>获取差异索引</p>
	 * <p>差异索引越小：差距越大</p>
	 * <p>差异索引越大：差距越小</p>
	 * 
	 * @param sources 原始数据
	 * @param targets 目标数据
	 * 
	 * @return 差异索引
	 */
	public static final int diffIndex(byte[] sources, byte[] targets) {
		Objects.requireNonNull(sources);
		Objects.requireNonNull(targets);
		if (sources.length != targets.length) {
			throw new IllegalArgumentException("差异索引参数错误（长度）");
		} else {
			final int length = sources.length;
			for (int index = 0; index < length; index++) {
				if(sources[index] != targets[index]) {
					return index;
				}
			}
			return length;
		}
	}
	
	/**
	 * <p>判断数组是否为空</p>
	 * 
	 * @param objects 数组
	 * 
	 * @return true-空；false-非空；
	 */
	public static final boolean isEmpty(Object[] objects) {
		return objects == null || objects.length == 0;
	}
	
	/**
	 * <p>判断数组是否非空</p>
	 * 
	 * @param objects 数组
	 * 
	 * @return true-非空；false-空；
	 */
	public static final boolean isNotEmpty(Object[] objects) {
		return !isEmpty(objects);
	}
	
	/**
	 * <p>判断字节数组是否为空</p>
	 * 
	 * @param bytes 字节数组
	 * 
	 * @return true-空；false-非空；
	 */
	public static final boolean isEmpty(byte[] bytes) {
		return bytes == null || bytes.length == 0;
	}

	/**
	 * <p>判断字节数组是否非空</p>
	 * 
	 * @param bytes 字节数组
	 * 
	 * @return true-非空；false-空；
	 */
	public static final boolean isNotEmpty(byte[] bytes) {
		return !isEmpty(bytes);
	}
	
	/**
	 * <p>判断数组是否相等</p>
	 * 
	 * @param sources 原始数据
	 * @param targets 目标数据
	 * 
	 * @return true-相等；false-不等；
	 */
	public static final boolean equals(byte[] sources, byte[] targets) {
		if(sources == targets) {
			return true;
		}
		if(sources == null || targets == null) {
			return false;
		}
		final int length = sources.length;
		if(length != targets.length) {
			return false;
		}
		for (int index = 0; index < length; index++) {
			if(sources[index] != targets[index]) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * <p>获取随机字节数组</p>
	 * 
	 * @param length 数组长度
	 * 
	 * @return 字节数组
	 */
	public static final byte[] random(int length) {
		final byte[] bytes = new byte[length];
		final Random random = NumberUtils.random();
		for (int index = 0; index < length; index++) {
			bytes[index] = (byte) random.nextInt(0xFF);
		}
		return bytes;
	}
	
	/**
	 * <p>查找数组索引</p>
	 * 
	 * @param values 查找数组
	 * @param value 查找数据
	 * 
	 * @return 数据索引（没有数据返回：-1）
	 * 
	 * @see #indexOf(byte[], int, int, byte)
	 */
	public static final int indexOf(byte[] values, byte value) {
		if(values == null) {
			return -1;
		}
		return indexOf(values, 0, values.length, value);
	}
	
	/**
	 * <p>查找数组索引</p>
	 * 
	 * @param values 查找数组
	 * @param begin 开始位置
	 * @param end 结束位置
	 * @param value 查找数据
	 * 
	 * @return 数据索引（没有数据返回：-1）
	 */
	public static final int indexOf(byte[] values, int begin, int end, byte value) {
		if(values == null) {
			return -1;
		}
		end = end > values.length ? values.length : end;
		for (int index = begin; index < end; index++) {
			if(values[index] == value) {
				return index;
			}
		}
		return -1;
	}
	
	/**
	 * <p>查找数组索引</p>
	 * 
	 * @param values 查找数组
	 * @param value 查找数据
	 * 
	 * @return 数据索引（没有数据返回：-1）
	 * 
	 * @see #indexOf(char[], int, int, char)
	 */
	public static final int indexOf(char[] values, char value) {
		if(values == null) {
			return -1;
		}
		return indexOf(values, 0, values.length, value);
	}
	
	/**
	 * <p>查找数组索引</p>
	 * 
	 * @param values 查找数组
	 * @param begin 开始位置
	 * @param end 结束位置
	 * @param value 查找数据
	 * 
	 * @return 数据索引（没有数据返回：-1）
	 */
	public static final int indexOf(char[] values, int begin, int end, char value) {
		if(values == null) {
			return -1;
		}
		end = end > values.length ? values.length : end;
		for (int index = begin; index < end; index++) {
			if(values[index] == value) {
				return index;
			}
		}
		return -1;
	}

	/**
	 * <p>查找数组索引</p>
	 * 
	 * @param values 查找数组
	 * @param value 查找数据
	 * 
	 * @return 数据索引（没有数据返回：-1）
	 * 
	 * @see #indexOf(int[], int, int, int)
	 */
	public static final int indexOf(int[] values, int value) {
		if(values == null) {
			return -1;
		}
		return indexOf(values, 0, values.length, value);
	}
	
	/**
	 * <p>查找数组索引</p>
	 * 
	 * @param values 查找数组
	 * @param begin 开始位置
	 * @param end 结束位置
	 * @param value 查找数据
	 * 
	 * @return 数据索引（没有数据返回：-1）
	 */
	public static final int indexOf(int[] values, int begin, int end, int value) {
		if(values == null) {
			return -1;
		}
		end = end > values.length ? values.length : end;
		for (int index = begin; index < end; index++) {
			if(values[index] == value) {
				return index;
			}
		}
		return -1;
	}
	
	/**
	 * <p>查找数组索引</p>
	 * 
	 * @param values 查找数组
	 * @param value 查找数据
	 * 
	 * @return 数据索引（没有数据返回：-1）
	 * 
	 * @see #indexOf(Object[], int, int, Object)
	 */
	public static final int indexOf(Object[] values, Object value) {
		if(values == null) {
			return -1;
		}
		return indexOf(values, 0, values.length, value);
	}
	
	/**
	 * <p>查找数组索引</p>
	 * 
	 * @param values 查找数组
	 * @param begin 开始位置
	 * @param end 结束位置
	 * @param value 查找数据
	 * 
	 * @return 数据索引（没有数据返回：-1）
	 */
	public static final int indexOf(Object[] values, int begin, int end, Object value) {
		if(values == null) {
			return -1;
		}
		end = end > values.length ? values.length : end;
		for (int index = begin; index < end; index++) {
			if(values[index] == value || (values[index] != null && values[index].equals(value))) {
				return index;
			}
		}
		return -1;
	}
	
}

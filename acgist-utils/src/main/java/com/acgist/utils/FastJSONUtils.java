package com.acgist.utils;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * <p>Fastjson工具</p>
 * 
 * @author acgist
 */
public final class FastJSONUtils {

	/**
	 * <p>工具禁止创建实例</p>
	 */
	private FastJSONUtils() {
	}
	
	/**
	 * <p>JSON序列化</p>
	 * 
	 * @param object Java对象
	 * 
	 * @return JSON字符串
	 */
	public static final String serialize(Object object) {
		if(object == null) {
			return null;
		}
		return JSON.toJSONString(object);
	}
	
	/**
	 * <p>JSON反序列化</p>
	 * 
	 * @param <T> 泛型
	 * 
	 * @param json JSON字符串
	 * @param clazz 泛型类型
	 * 
	 * @return Java对象
	 */
	public static final <T> T unserialize(String json, Class<T> clazz) {
		if (json == null) {
			return null;
		}
		return JSON.parseObject(json, clazz);
	}
	
	/**
	 * <p>将JSON字符串转Map对象</p>
	 * 
	 * @param json JSON字符串
	 * 
	 * @return Map对象
	 */
	public static final <K, V> Map<K, V> toMap(String json) {
		if (json == null) {
			return null;
		}
		return JSON.parseObject(json, new TypeReference<Map<K, V>>() {});
	}
	
	/**
	 * <p>将JSON字符串转List对象</p>
	 * 
	 * @param <T> 类型
	 * 
	 * @param json JSON字符串
	 * @param clazz 类型
	 * 
	 * @return List对象
	 */
	public static final <T> List<T> toList(String json) {
		if (json == null) {
			return null;
		}
		return JSON.parseObject(json, new TypeReference<List<T>>() {});
	}
	
}

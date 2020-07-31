package com.acgist.modules.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.api.API;

/**
 * 对象转map工具
 */
public class APIUtils {

	private static final String CLASS_KEY = "class";
	private static final Logger LOGGER = LoggerFactory.getLogger(APIUtils.class);

	/**
	 * JAVA对象转map
	 */
	public static final <T extends API> Map<String, String> beanToMap(T t) {
		if(t == null) {
			return null;
		}
		Map<String, String> data = null;
		try {
			// 对象中有数组时只能获取到第一个数据
			data = BeanUtils.describe(t);
			// 对象中有数组时能获取到完整数据
//			data = PropertyUtils.describe(t);
//			效率差
//			String json = JSONUtils.java2json(t);
//			data = JSONUtils.json2map(json);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			LOGGER.error("java对象转map异常，对象：{}", t, e);
		}
		if (data != null) { // 过滤key和null值
			return data.entrySet().stream()
				.filter(entry -> !entry.getKey().equals(CLASS_KEY))
				.filter(entry -> entry.getKey() != null && entry.getValue() != null)
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		}
		return null;
	}

	/**
	 * map填充JAVA对象
	 */
	public static final void mapSetBean(API api, Map<String, String> data) {
		if(api == null || data == null) {
			return;
		}
		try {
			BeanUtils.populate(api, data);
		} catch (IllegalAccessException | InvocationTargetException e) {
			LOGGER.error("map数据填充JAVA对象异常，map数据：{}", data, e);
		}
	}
	
}

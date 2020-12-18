package com.acgist.fund.utils;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>JSON工具</p>
 * 
 * @author acgist
 */
public class JSONUtils {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(JSONUtils.class);

	private static final ObjectMapper MAPPER;
	
	static {
		final ObjectMapper mapper = new ObjectMapper();
//		mapper.activateDefaultTyping(PolymorphicTypeValidator.Validity.DENIED);
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // 不序列化null值，使用注解：@JsonInclude(Include.NON_NULL)
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 未知属性不反序列化，使用注解：@JsonIgnoreProperties(ignoreUnknown = true)
		MAPPER = mapper;
	}
	
	private JSONUtils() {
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
		try {
			return MAPPER.writeValueAsString(object);
		} catch (Exception e) {
			LOGGER.error("JSON序列化异常：{}", object, e);
		}
		return null;
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
		try {
			return MAPPER.readValue(json, clazz);
		} catch (IOException e) {
			LOGGER.error("JSON反序列化异常：{}", json, e);
		}
		return null;
	}
	
}

package com.acgist.ding;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Map工具
 * 
 * @author acgist
 */
public class MapUtils {
	
	private MapUtils() {
	}

	/**
	 * 判断Map是否为空
	 * 
	 * @param map Map
	 * 
	 * @return 是否为空
	 */
	public static final boolean isEmpty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}
	
	/**
	 * 判断Map是否不为空
	 * 
	 * @param map Map
	 * 
	 * @return 是否不为空
	 */
	public static final boolean isNotEmpty(Map<?, ?> map) {
		return !MapUtils.isEmpty(map);
	}
	
	/**
	 * Map转URL
	 * 
	 * @param map Map
	 * 
	 * @return URL
	 */
	public static final String toUrl(Map<String, String> map) {
		return MapUtils.toUrl(map, true);
	}
	
	/**
	 * Map转URL
	 * 
	 * @param map Map
	 * @param encode 是否编码
	 * 
	 * @return URL
	 */
	public static final String toUrl(Map<String, String> map, boolean encode) {
		return map.entrySet().stream()
			.map(entry -> entry.getKey() + "=" + (encode ? Strings.urlEncode(entry.getValue()) : entry.getValue()))
			.collect(Collectors.joining("&"));
	}
	
	/**
	 * URL转Map
	 * 
	 * @param url URL
	 * 
	 * @return Map
	 */
	public static final Map<String, String> ofUrl(String url) {
		return MapUtils.ofUrl(url, true);
	}
	
	/**
	 * URL转Map
	 * 
	 * @param url URL
	 * @param decode 是否解码
	 * 
	 * @return Map
	 */
	public static final Map<String, String> ofUrl(String url, boolean decode) {
		String key;
		String value;
		int index = 0;
		final String[] querys = url.split("&");
		final Map<String, String> map = new HashMap<>();
		for (String query : querys) {
			index = query.indexOf("=");
			if(index < 0) {
				map.put(query, Config.EMPTY);
				continue;
			}
			key = query.substring(0, index);
			value = query.substring(index + 1);
			if(decode) {
				value = Strings.urlDecode(value);
			}
			map.put(key, value);
		}
		return map;
	}
	
}

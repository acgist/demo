package com.api.core.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * service - redis
 */
@Service
public class RedisService {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	/**
	 * 获取对象
	 * @param key key
	 * @return 对象
	 */
	public Object get(String key) {
		return redisTemplate.opsForValue().get(key);
	}
	
	/**
	 * 放入对象
	 * @param key key
	 * @param value 对象
	 */
	public void set(String key, Object value) {
		redisTemplate.opsForValue().set(key, value);
	}
	
	/**
	 * 放入有时间期限对象
	 * @param key key
	 * @param value 对象
	 * @param timeout 过期时间
	 * @param unit 时间单位
	 */
	public void set(String key, Object value, long timeout, TimeUnit unit) {
		redisTemplate.opsForValue().set(key, value, timeout, unit);
	}
	
	/**
	 * 删除对象
	 * @param key key
	 */
	public void del(String key) {
		redisTemplate.delete(key);
	}

}

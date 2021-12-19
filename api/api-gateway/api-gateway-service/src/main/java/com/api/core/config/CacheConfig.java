package com.api.core.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * config - 缓存
 */
@Configuration
public class CacheConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(CacheConfig.class);
	
	@Bean
	public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
		LOGGER.info("初始化CacheManager");
		RedisCacheConfiguration config = RedisCacheConfiguration
			.defaultCacheConfig()
//			.serializeKeysWith(null) // key 序列化
//			.serializeValuesWith(null) // value 序列化
			.disableCachingNullValues() // 不缓存空值
			.entryTtl(Duration.ofMinutes(30)); // 设置过期时间
		// 初始化缓存配置
		Set<String> caches = new HashSet<>();
		caches.add(APIConstCache.CACHE_KEY_USER_AUTHO);
		// 缓存设置
		Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();
		cacheConfigs.put(APIConstCache.CACHE_KEY_USER_AUTHO, config);
		// 创建缓存
		RedisCacheManager cacheManager = RedisCacheManager.builder(connectionFactory)
			.initialCacheNames(caches)
			.withInitialCacheConfigurations(cacheConfigs)
			.build();
		return cacheManager;
	}

	@Bean
	public KeyGenerator keyGenerator() {
		LOGGER.info("初始化KeyGenerator");
		return new SimpleKeyGenerator();
	}

}

package com.api.core.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * config - 异步线程线程池配置
 */
@Configuration
public class AsyncConfig {

	@Bean
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setMaxPoolSize(100); // 最大线程
		executor.setCorePoolSize(10); // 最少线程
		executor.setQueueCapacity(100); // 缓冲队列：如果缓冲队列未满，不会增加新线程
		executor.setKeepAliveSeconds(60); // 线程空闲时间
		executor.setThreadNamePrefix("API-ASYNC-");
		executor.initialize();
		return executor;
	}

}

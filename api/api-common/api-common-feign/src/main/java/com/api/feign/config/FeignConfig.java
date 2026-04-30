package com.api.feign.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Retryer;

/**
 * config - feign
 */
@Configuration
public class FeignConfig {

	@Bean
	public Retryer feignRetryer() {
		// 时间间隔，最大重试时间，重试次数
		// return new Retryer.Default(100, TimeUnit.SECONDS.toMillis(2), 2);
		return Retryer.NEVER_RETRY;
	}

}

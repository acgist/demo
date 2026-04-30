package com.api.ribbon.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * config - 负载均衡
 */
@Configuration
public class RibbonConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(RibbonConfig.class);
	
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		LOGGER.info("初始化RestTemplate");
		return new RestTemplate();
	}

}

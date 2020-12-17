package com.acgist.gateway.config;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:gateway.properties", encoding = "UTF-8")
@ConfigurationProperties(prefix = "gateway")
public class GatewayConfig {
	
	private Map<String, String> gateways;
	
	@PostConstruct
	public void init() {
	}

}

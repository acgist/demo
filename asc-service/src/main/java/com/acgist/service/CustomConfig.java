package com.acgist.service;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.alibaba.nacos.api.config.annotation.NacosValue;

// 配置默认加载：服务-环境.类型、服务.类型
@Configuration
// 下面配置没有效果：使用配置文件ext-config配置
//@NacosConfigurationProperties(dataId = "acgist.properties", type = ConfigType.PROPERTIES)
public class CustomConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomConfig.class);
	
	// acgist.properties
	// 下面配置没有效果
	@NacosValue(value = "${name:}", autoRefreshed = true)
	private String name;
	@Value("${test:}")
	private String test;
	// service.properties
	@Value("${main:}")
	private String main;
	// service-dev.properties
	@Value("${service:}")
	private String service;
	
	@PostConstruct
	public void init() {
		LOGGER.info("name：{}", this.name);
		LOGGER.info("test：{}", this.test);
		LOGGER.info("main：{}", this.main);
		LOGGER.info("service：{}", this.service);
	}
	
}

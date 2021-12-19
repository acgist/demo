package com.api.core.config;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.api.core.module.freemarker.AuthoTag;

/**
 * config - freemarker配置
 */
@Configuration
public class FreemarkerTagConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(FreemarkerTagConfig.class);
	
	@Autowired
	private AuthoTag authoTag;
	@Autowired
	private freemarker.template.Configuration configuration;

	@PostConstruct
	public void init() throws Exception {
		LOGGER.info("初始化freemarker标签");
		configuration.setSharedVariable("autho", authoTag);
	}
	
}

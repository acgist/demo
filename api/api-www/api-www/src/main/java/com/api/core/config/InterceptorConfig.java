package com.api.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.api.core.interceptor.CsrfInterceptor;
import com.api.core.interceptor.OrderInterceptor;

/**
 * config - 拦截器
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

	private static final Logger LOGGER = LoggerFactory.getLogger(InterceptorConfig.class);
	
	@Autowired
	private CsrfInterceptor csrfInterceptor;
	@Autowired
	private OrderInterceptor orderInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LOGGER.info("拦截器初始化：csrfInterceptor");
		registry.addInterceptor(csrfInterceptor).addPathPatterns("/**");
		LOGGER.info("拦截器初始化：orderInterceptor");
		registry.addInterceptor(orderInterceptor).addPathPatterns("/order/**");
		WebMvcConfigurer.super.addInterceptors(registry);
	}

}

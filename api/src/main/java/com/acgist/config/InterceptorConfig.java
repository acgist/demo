package com.acgist.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.acgist.interceptor.DataVerifyInterceptor;
import com.acgist.interceptor.GatewayInterceptor;
import com.acgist.interceptor.PackageInterceptor;
import com.acgist.interceptor.PayInterceptor;
import com.acgist.interceptor.ProcessInterceptor;
import com.acgist.interceptor.SignVerifyInterceptor;

/**
 * 拦截器配置
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

	private static final Logger LOGGER = LoggerFactory.getLogger(InterceptorConfig.class);
	
	@Autowired
	private PayInterceptor payInterceptor;
	@Autowired
	private GatewayInterceptor gatewayInterceptor;
	@Autowired
	private ProcessInterceptor processInterceptor;
	@Autowired
	private PackageInterceptor packageInterceptor;
	@Autowired
	private SignVerifyInterceptor signVerifyInterceptor;
	@Autowired
	private DataVerifyInterceptor dataVerifyInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LOGGER.info("拦截器初始化：processInterceptor");
		registry.addInterceptor(processInterceptor).addPathPatterns("/gateway/api/**");
		LOGGER.info("拦截器初始化：packageInterceptor");
		registry.addInterceptor(packageInterceptor).addPathPatterns("/gateway/api/**");
		LOGGER.info("拦截器初始化：signVerifyInterceptor");
		registry.addInterceptor(signVerifyInterceptor).addPathPatterns("/gateway/api/**");
		LOGGER.info("拦截器初始化：dataVerifyInterceptor");
		registry.addInterceptor(dataVerifyInterceptor).addPathPatterns("/gateway/api/**");
		LOGGER.info("拦截器初始化：payInterceptor");
		registry.addInterceptor(payInterceptor).addPathPatterns("/gateway/api/pay");
		LOGGER.info("拦截器初始化：gatewayInterceptor");
		registry.addInterceptor(gatewayInterceptor).addPathPatterns("/gateway/api/**");
		WebMvcConfigurer.super.addInterceptors(registry);
	}

}

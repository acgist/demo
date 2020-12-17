package com.acgist.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.acgist.gateway.interceptor.GatewayInterceptor;
import com.acgist.gateway.interceptor.PackageInterceptor;
import com.acgist.gateway.interceptor.PayInterceptor;
import com.acgist.gateway.interceptor.ProcessInterceptor;
import com.acgist.gateway.interceptor.SignatureInterceptor;
import com.acgist.gateway.interceptor.ValidatorInterceptor;

/**
 * <p>拦截器配置</p>
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

	private static final Logger LOGGER = LoggerFactory.getLogger(InterceptorConfig.class);
	
	@Autowired
	private PayInterceptor payInterceptor;
	@Autowired
	private ProcessInterceptor processInterceptor;
	@Autowired
	private PackageInterceptor packageInterceptor;
	@Autowired
	private GatewayInterceptor gatewayInterceptor;
	@Autowired
	private SignatureInterceptor signatureInterceptor;
	@Autowired
	private ValidatorInterceptor validatorInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LOGGER.info("拦截器初始化：processInterceptor");
		registry.addInterceptor(this.processInterceptor).addPathPatterns("/gateway/api/**");
		LOGGER.info("拦截器初始化：packageInterceptor");
		registry.addInterceptor(this.packageInterceptor).addPathPatterns("/gateway/api/**");
		LOGGER.info("拦截器初始化：signatureInterceptor");
		registry.addInterceptor(this.signatureInterceptor).addPathPatterns("/gateway/api/**");
		LOGGER.info("拦截器初始化：dataVerifyInterceptor");
		registry.addInterceptor(this.validatorInterceptor).addPathPatterns("/gateway/api/**");
		LOGGER.info("拦截器初始化：payInterceptor");
		registry.addInterceptor(this.payInterceptor).addPathPatterns("/gateway/api/pay/**");
		LOGGER.info("拦截器初始化：gatewayInterceptor");
		registry.addInterceptor(this.gatewayInterceptor).addPathPatterns("/gateway/api/**");
		WebMvcConfigurer.super.addInterceptors(registry);
	}

}

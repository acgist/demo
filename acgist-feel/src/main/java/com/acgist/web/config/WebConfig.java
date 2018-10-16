package com.acgist.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.acgist.web.interceptor.APIInterceptor;
import com.acgist.web.interceptor.FaceInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	private APIInterceptor apiInterceptor;
	@Autowired
	private FaceInterceptor faceInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(apiInterceptor).addPathPatterns("/api/**");
		registry.addInterceptor(faceInterceptor).addPathPatterns("/api/face/**");
		WebMvcConfigurer.super.addInterceptors(registry);
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("redirect:/swagger-ui.html"); // 配置欢迎页
//		registry.addViewController("/").setViewName("forward:/swagger-ui.html"); // 配置欢迎页
		WebMvcConfigurer.super.addViewControllers(registry);
	}
	
}

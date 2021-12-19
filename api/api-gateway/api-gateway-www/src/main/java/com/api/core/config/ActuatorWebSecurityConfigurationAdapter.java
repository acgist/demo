package com.api.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * config - 端点安全，需要添加spring-boot-starter-security
 */
@Order(0)
@Configuration
public class ActuatorWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

	@Autowired
	private ActuatorConfig actuatorConfig;
	
	@Override
	protected void configure(HttpSecurity security) throws Exception {
		security
//			.cors().disable()
			.csrf().disable() // 解决POST请求403错误
//			.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//			.and()
//			.authorizeRequests().requestMatchers(EndpointRequest.toAnyEndpoint()).denyAll()
//			.and()
//			.csrf().ignoringAntMatchers(actuatorConfig.getActuatorAntMatchers()) // 端点使用IP地址验证
//			.and()
			.authorizeRequests().antMatchers(actuatorConfig.getActuatorAntMatchers()).access(actuatorConfig.getActuatorIpAddresses())
			.and()
			.authorizeRequests().anyRequest().permitAll(); // 允许
	}
	
}
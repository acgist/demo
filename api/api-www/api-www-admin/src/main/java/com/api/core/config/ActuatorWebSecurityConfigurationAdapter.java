package com.api.core.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import com.api.core.controller.APIErrorController;
import com.api.core.gateway.APICode;
import com.api.core.security.APIAccessDecisionManager;
import com.api.core.security.APISecurityMetadataSource;
import com.api.core.security.AdminDetailsService;

/**
 * config - 端点安全，需要添加spring-boot-starter-security
 */
@Order(0)
@Configuration
@EnableWebSecurity
public class ActuatorWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

	@Autowired
	private ActuatorConfig actuatorConfig;
	@Autowired
	private AdminDetailsService adminDetailsService;
	@Autowired
	private APIAccessDecisionManager apiAccessDecisionManager;
	@Autowired
	private APISecurityMetadataSource apiSecurityMetadataSource;
	
	/**
	 * 路径
	 */
	@Override
	protected void configure(HttpSecurity security) throws Exception {
		security
			.csrf().disable() // 解决POST请求403错误
			.headers().frameOptions().sameOrigin() // iframe同源
			.and()
				.formLogin()
				.loginPage(APIConstAdminURL.URL_LOGIN)
				.defaultSuccessUrl(APIConstAdminURL.URL_ADMIN_INDEX, true)
				.usernameParameter("username")
				.passwordParameter("password")
			.and()
				.logout()
				.logoutUrl(APIConstAdminURL.URL_LOGOUT)
				.logoutSuccessUrl(APIConstAdminURL.URL_LOGIN)
			.and()
				.authorizeRequests()
				.antMatchers(actuatorConfig.getActuatorIpAddresses())
				.access(actuatorConfig.getActuatorIpAddresses())
			.and()
				.exceptionHandling()
				.accessDeniedPage(APIErrorController.ERROR_PATH + "?code=" + APICode.CODE_2001.getCode())
			.and()
				.authorizeRequests()
				.anyRequest()
				.authenticated()
				.withObjectPostProcessor(objectPostProcessor());
	}
	
	/**
	 * 角色
	 */
	@Override
	public void configure(AuthenticationManagerBuilder builder) throws Exception {
		builder.authenticationProvider(authenticationProvider());
	}
	
	/**
	 * 用户查询
	 */
	private DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(adminDetailsService);
		provider.setPasswordEncoder(md5PasswordEncoder()); // 系统直接使用明文密码
		return provider;
	}
	
	/**
	 * 密码验证
	 */
    private PasswordEncoder md5PasswordEncoder() {
        return new PasswordEncoder() {
			@Override
			public boolean matches(CharSequence password, String encodedPassword) {
				return StringUtils.equals(encode(password), encodedPassword);
			}
			@Override
			public String encode(CharSequence password) {
				if(password == null) {
					return null;
				}
//				return DigestUtils.md5Hex(password.toString());
				return password.toString();
			}
		};
    }
	
	/**
	 * 权限鉴定
	 */
	private ObjectPostProcessor<FilterSecurityInterceptor> objectPostProcessor() {
		return new ObjectPostProcessor<FilterSecurityInterceptor>() {
			@Override
			public <T extends FilterSecurityInterceptor> T postProcess(T t) {
				t.setAccessDecisionManager(apiAccessDecisionManager); // 权限鉴定
				t.setSecurityMetadataSource(apiSecurityMetadataSource); // 资源拦截
				return t;
			}
		};
	}
    
}
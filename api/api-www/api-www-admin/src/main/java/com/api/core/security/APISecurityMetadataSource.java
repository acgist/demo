package com.api.core.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import com.api.core.service.PermissionService;

/**
 * 角色和权限验证
 */
@Component
@RefreshScope
public class APISecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	private static final Logger LOGGER = LoggerFactory.getLogger(APISecurityMetadataSource.class);

	private static final List<String> ALLOW_ATTRIBUTES = new ArrayList<>();
	
	@Value("${system.security.enable:true}")
	private boolean enable;
	@Value("${system.security.allow.attributes:}")
	private String allowAttributes;
	
	@Autowired
	private PermissionService permissionService;
	
	@PostConstruct
	public void init() {
		initAllowAttributes();
	}
	
	/**
	 * 获取访问角色列表<br>
	 * 返回null表示不拦截
	 */
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		if(!enable) {
			return null;
		}
		FilterInvocation filter = (FilterInvocation) object;
		if (allow(filter)) {
			return null;
		}
		return allowAttributes(filter);
	}

	/**
	 * 验证是否为不需要鉴权的地址
	 */
	private boolean allow(FilterInvocation filter) {
		return ALLOW_ATTRIBUTES.stream()
			.filter(StringUtils::isNotEmpty)
			.map(AntPathRequestMatcher::new)
			.anyMatch(maptcher -> maptcher.matches(filter.getHttpRequest()));
	}
	
	/**
	 * 根据权限地址获取角色列表
	 */
	private List<ConfigAttribute> allowAttributes(FilterInvocation filter) {
		List<String> list = permissionService.permissionRoles(filter.getRequest().getServletPath());
		if(list == null) {
			return deniedAttributes();
		}
		return list.stream()
			.map(role -> new SecurityConfig(role))
			.collect(Collectors.toList());
	}
	
	/**
	 * 禁止访问权限
	 */
	private List<ConfigAttribute> deniedAttributes() {
		return Collections.singletonList(new SecurityConfig("ROLE_DENIED"));
	}
	
	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}
	
	/**
	 * 初始化不需要鉴权的地址
	 */
	private void initAllowAttributes() {
		LOGGER.info("不需要鉴权的地址：{}", this.allowAttributes);
		ALLOW_ATTRIBUTES.clear();
		ALLOW_ATTRIBUTES.addAll(Arrays.asList(this.allowAttributes.split(",")));
	}

}

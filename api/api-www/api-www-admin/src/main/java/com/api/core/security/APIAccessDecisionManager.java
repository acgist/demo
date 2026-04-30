package com.api.core.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * 权限验证
 */
@Component
public class APIAccessDecisionManager implements AccessDecisionManager {

	/**
	 * 验证当前角色是否允许访问所请求权限
	 */
	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
		if (authentication == null) {
			throw new AccessDeniedException("没有访问权限");
		}
		List<String> roles = authentication.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.toList());
		List<String> allowRoles = configAttributes.stream()
			.map(ConfigAttribute::getAttribute)
			.collect(Collectors.toList());
		if(roles.stream().noneMatch(role -> allowRoles.contains(role))) {
			throw new AccessDeniedException("没有访问权限");
		}
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

}

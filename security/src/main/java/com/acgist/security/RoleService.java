package com.acgist.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

	public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
//		SecurityContextHolder.getContext().getAuthentication()
		final String role = authentication.getName();
		return request.getRequestURL().toString().contains(role);
	}
	
}

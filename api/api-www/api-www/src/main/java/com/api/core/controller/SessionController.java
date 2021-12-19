package com.api.core.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * controller - SESSION测试
 */
@RestController
public class SessionController {

	@RequestMapping("/session")
	public String session(String value, HttpServletRequest request) {
		value = value == null ? "session" : value;
		request.getSession().setAttribute("session", value);
		return value;
	}
	
	@RequestMapping("/session/value")
	public String sessionValue(String value, HttpServletRequest request) {
		return (String) request.getSession().getAttribute("session");
	}
	
}

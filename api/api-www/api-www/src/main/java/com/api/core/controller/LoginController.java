package com.api.core.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.api.core.pojo.session.UserSession;
import com.api.core.user.pojo.message.LoginMessage;
import com.api.feign.user.service.UserService;

/**
 * controller - 用户 - 登陆
 */
@Controller
@RequestMapping("login")
public class LoginController {

	@Autowired
	private UserService userService;
	
	@GetMapping
	public String login(HttpServletRequest request) {
		return "/login/index";
	}
	
	@PostMapping
	public String login(String username, String password, HttpServletRequest request) {
		LoginMessage message = userService.login(username, password);
		if(!message.isSuccess()) {
			return "redirect:/login";
		}
		UserSession user = new UserSession();
		user.setId(message.getId());
		user.setName(message.getName());
		user.putSession(request);
		return "redirect:/order";
	}
	
}

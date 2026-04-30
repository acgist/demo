package com.acgist.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

	@GetMapping("/login")
	public String login(String username, String password) {
		return "/login.html";
	}
	
	@GetMapping("/logout")
	@ResponseBody
	public String logout() {
		return "logout";
	}
	
	@GetMapping("/list")
	@ResponseBody
	public String list() {
		return "list";
	}
	
}

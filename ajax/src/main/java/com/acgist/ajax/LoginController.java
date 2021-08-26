package com.acgist.ajax;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

	@GetMapping("/login")
	@ResponseBody
	public String login(HttpSession session) {
		session.setAttribute("login", "login");
		return "success";
	}
	
	@GetMapping("/logout")
	@ResponseBody
	public String logout(HttpSession session) {
		session.removeAttribute("login");
		return "success";
	}
	
	@GetMapping("/login/status")
	@ResponseBody
	public String status(HttpSession session) {
		if(session.getAttribute("login") == null) {
			return "fail";
		} else {
			return "success";
		}
	}
	
}

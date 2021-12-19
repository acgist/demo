package com.api.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.api.core.config.APIConstAdminURL;
import com.api.core.gateway.APICode;

/**
 * controller - 登陆
 */
@Controller
public class LoginController {

	@GetMapping(APIConstAdminURL.URL_LOGIN)
	public String login() {
		return "/login";
	}
	
	@ResponseBody
	@PostMapping(APIConstAdminURL.URL_LOGIN)
	public String login(String username, String password) {
		return APICode.CODE_SUCCESS;
	}
	
	@GetMapping(APIConstAdminURL.URL_LOGOUT)
	public String logout() {
		return "/login";
	}

}

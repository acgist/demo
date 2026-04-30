package com.api.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * controller - 首页
 */
@Controller
public class IndexController {

	@GetMapping("/")
	public String indexs() {
		return "/index";
	}
	
}

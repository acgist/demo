package com.api.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * controller - 聊天室
 */
@Controller
@RequestMapping("/chat")
public class ChatController {

	@GetMapping
	public String index() {
		return "/chat/index";
	}
	
}

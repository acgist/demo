package com.acgist.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acgist.api.response.APIResponse;

/**
 * 欢迎页
 */
@RestController
public class APIIndexController {

	@RequestMapping("/")
	public Map<String, String> index() {
		return APIResponse.builder().success().response();
	}

}

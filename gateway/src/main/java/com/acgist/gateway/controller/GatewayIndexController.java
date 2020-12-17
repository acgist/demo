package com.acgist.gateway.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acgist.gateway.response.GatewayResponse;

@RestController
public class GatewayIndexController {

	@RequestMapping("/")
	public Map<String, String> index() {
		return GatewayResponse.builder().success().response();
	}

}

package com.acgist.controller;

import java.util.Map;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acgist.api.ResponseCode;
import com.acgist.api.response.APIResponse;

/**
 * 错误页面
 */
@RestController
public class APIErrorController implements ErrorController {

	@RequestMapping(value = "/error")
	public Map<String, String> index(String code, String message) {
		final ResponseCode responseCode = ResponseCode.valueOfCode(code);
		if(message == null) {
			message = responseCode.getMessage();
		}
		return APIResponse.builder().message(responseCode, message).response();
	}

	@Override
	public String getErrorPath() {
		return null;
	}

}

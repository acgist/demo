package com.acgist.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.acgist.api.ResponseCode;
import com.acgist.api.response.APIResponse;
import com.acgist.modules.exception.ErrorCodeException;

/**
 * 异常处理
 */
@ControllerAdvice
public class APIControllerAdvice {

	private static final Logger LOGGER = LoggerFactory.getLogger(APIControllerAdvice.class);
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.OK) // 状态码：200
//	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 状态码：500
	@ResponseBody
	public Map<String, String> exception(Exception e) {
		LOGGER.error("系统异常", e);
		if (e instanceof ErrorCodeException) {
			ErrorCodeException exception = (ErrorCodeException) e;
			return APIResponse.builder().message(exception.getErrorCode(), exception.getMessage()).response();
		} else {
			return APIResponse.builder().message(ResponseCode.CODE_9999).response();
		}
	}

}

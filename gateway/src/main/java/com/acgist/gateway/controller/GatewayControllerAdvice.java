package com.acgist.gateway.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.acgist.gateway.ErrorCodeException;
import com.acgist.gateway.config.GatewayCode;
import com.acgist.gateway.response.GatewayResponse;

/**
 * <p>异常处理</p>
 */
@ControllerAdvice
public class GatewayControllerAdvice {

	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayControllerAdvice.class);
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.OK) // 状态码：200
//	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 状态码：500
	@ResponseBody
	public Map<String, String> exception(Exception e) {
		LOGGER.error("系统异常", e);
		if (e instanceof ErrorCodeException) {
			final ErrorCodeException exception = (ErrorCodeException) e;
			return GatewayResponse.builder().message(exception.getErrorCode(), exception.getMessage()).response();
		} else {
			return GatewayResponse.builder().message(GatewayCode.CODE_9999).response();
		}
	}

}

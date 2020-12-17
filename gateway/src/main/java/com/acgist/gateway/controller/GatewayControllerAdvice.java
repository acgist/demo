package com.acgist.gateway.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.acgist.gateway.GatewayCodeException;
import com.acgist.gateway.GatewaySession;
import com.acgist.gateway.config.GatewayCode;

/**
 * <p>异常处理</p>
 */
@ControllerAdvice
public class GatewayControllerAdvice {

	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayControllerAdvice.class);
	
	@Autowired
	private ApplicationContext context;
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.OK) // 状态码：200
//	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 状态码：500
	public void exception(Exception e, HttpServletResponse response) {
		LOGGER.error("系统异常", e);
		final GatewaySession session = GatewaySession.getInstance(this.context);
		if (e instanceof GatewayCodeException) {
			final GatewayCodeException exception = (GatewayCodeException) e;
			session.buildFail(exception.getCode(), exception.getMessage()).response(response);
		} else {
			session.buildFail(GatewayCode.CODE_9999).response(response);;
		}
	}

}

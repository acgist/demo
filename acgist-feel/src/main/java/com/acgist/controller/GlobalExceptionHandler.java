package com.acgist.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acgist.bean.vo.MessageBuilder;

/**
 * 统一异常处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ResponseBody
	@ExceptionHandler(value = Exception.class)
	public Map<String, String> defaultErrorHandler(Exception ex) {
		if(ex != null) {
			LOGGER.error("系统异常", ex);
		}
		return MessageBuilder.fail().put(MessageBuilder.MESSAGE_KEY, "系统异常").buildMessage();
	}

}
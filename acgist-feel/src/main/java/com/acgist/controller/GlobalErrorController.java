package com.acgist.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acgist.bean.vo.MessageBuilder;
import com.acgist.web.module.SessionComponent;

/**
 * 统一错误信息处理
 */
@RestController
public class GlobalErrorController implements ErrorController {

	@Autowired
	private ApplicationContext context;
	
	@Override
	public String getErrorPath() {
		return "/index.html";
	}
	
	@RequestMapping(value = "/error")
	public Map<String, String> error() {
		Map<String, String> data = null;
		SessionComponent sessionComponent = SessionComponent.getInstance(context);
		data = sessionComponent.getResData();
		if(data == null) {
			data = MessageBuilder.fail().put(MessageBuilder.MESSAGE_KEY, "系统错误").buildMessage();
		}
		return data;
	}

}

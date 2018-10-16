package com.acgist.bean.vo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.web.module.SessionComponent;

/**
 * 接口响应信息
 */
public class MessageBuilder {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageBuilder.class);
	
	public static final String CODE_KEY = "code";
	public static final String CODE_FAIL = "fail";
	public static final String CODE_SUCCESS = "success";

	public static final String MESSAGE_KEY = "message";
	public static final String MESSAGE_FAIL = "失败";
	public static final String MESSAGE_SUCCESS = "成功";
	
	private Map<String, String> data = new HashMap<>();
	
	public static final MessageBuilder build() {
		return new MessageBuilder();
	}
	
	public static final MessageBuilder fail() {
		return build().put(CODE_KEY, CODE_FAIL)
			.put(MESSAGE_KEY, MESSAGE_FAIL);
	}
	
	public static final MessageBuilder success() {
		return build().put(CODE_KEY, CODE_SUCCESS)
			.put(MESSAGE_KEY, MESSAGE_SUCCESS);
	}
	
	public MessageBuilder put(String key, String value) {
		this.data.put(key, value);
		return this;
	}
	
	public MessageBuilder failMessage(String value) {
		this.data.put(MESSAGE_KEY, value);
		return this;
	}
	
	public MessageBuilder failMessageRequired(String value) {
		this.data.put(MESSAGE_KEY, "缺少参数：" + value);
		return this;
	}
	
	public MessageBuilder failMessageFormat(String key, String value) {
		this.data.put(MESSAGE_KEY, "参数格式错误：" + value);
		return this;
	}
	
	public Map<String, String> buildMessage() {
		// sign：签名
		return this.data;
	}
	
	public MessageBuilder buildMessage(SessionComponent sessionComponent) {
		sessionComponent.setResData(buildMessage());
		return this;
	}
	
	public void forwardAPIError(HttpServletRequest request, HttpServletResponse response) {
		this.forward("/error", request, response);
	}

	/**
	 * 转发
	 */
	public void forward(String path, HttpServletRequest request, HttpServletResponse response) {
		try {
			request.getRequestDispatcher(path).forward(request, response);
		} catch (ServletException | IOException e) {
			LOGGER.error("转发异常", e);
		}
	}
	
	public void redirectAPIError(HttpServletResponse response) {
		this.redirect("/error", response);
	}
	
	/**
	 * 重定向
	 */
	public void redirect(String location, HttpServletResponse response) {
		try {
			response.sendRedirect(location);
		} catch (IOException e) {
			LOGGER.error("重定向异常", e);
		}
	}
	
}

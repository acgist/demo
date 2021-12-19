package com.api.core.pojo.layui;

import com.api.core.gateway.APICode;
import com.api.core.pojo.message.BaseMessage;
import com.api.utils.DateUtils;

/**
 * layui - 消息
 */
public class LayuiMessage extends BaseMessage {

	private static final long serialVersionUID = 1L;

	private String code;
	private String message;
	private String responseTime;
	
	public static final LayuiMessage buildSuccess() {
		return build(APICode.CODE_0000);
	}
	
	public static final LayuiMessage build(APICode code) {
		return build(code.getCode(), code.getMessage());
	}
	
	public static final LayuiMessage build(String code, String message) {
		LayuiMessage layuiMessage = new LayuiMessage();
		return layuiMessage.buildMessage(code, message);
	}
	
	public LayuiMessage buildMessage(String code, String message) {
		this.code = code;
		this.message = message;
		this.responseTime = DateUtils.nowDate();
		return this;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}

}

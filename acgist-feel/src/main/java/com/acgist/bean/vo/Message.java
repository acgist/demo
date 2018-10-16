package com.acgist.bean.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;

/**
 * 接口演示使用
 */
@ApiModel(value = "APIMessage", description = "接口响应模型")
public class Message implements Serializable {

	private static final long serialVersionUID = 1L;

	private String code;
	private String message;

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

}

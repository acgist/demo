package com.acgist.api;

/**
 * 错误状态码
 * 0000=成功
 * 9999=未知错误
 * 1xxx=系统错误
 * 2xxx=业务错误
 * 3xxx=数据错误
 */
public enum ResponseCode {

	CODE_0000("0000", "成功"),
	CODE_1000("1000", "未知接口"),
	CODE_1001("1001", "该连接已有请求在处理中"),
	CODE_3000("3000", "数据格式错误"),
	CODE_3001("3001", "验签失败"),
	CODE_9999("9999", "未知错误");
	
	private String code;
	private String message;

	private ResponseCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public static final ResponseCode valueOfCode(String code) {
		for (ResponseCode responseCode : ResponseCode.values()) {
			if(responseCode.code.equals(code)) {
				return responseCode;
			}
		}
		return CODE_9999;
	}
	
	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}

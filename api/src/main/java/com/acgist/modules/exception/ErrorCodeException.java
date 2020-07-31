package com.acgist.modules.exception;

import com.acgist.api.ResponseCode;

/**
 * 错误状态码
 */
public class ErrorCodeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ErrorCodeException() {
	}

	public ErrorCodeException(ResponseCode code) {
		this(code.getCode(), code.getMessage());
	}
	
	public ErrorCodeException(ResponseCode code, String message) {
		this(code.getCode(), message);
	}
	
	public ErrorCodeException(String code, String message) {
		super(message);
		this.errorCode = code;
	}

	private String errorCode;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}

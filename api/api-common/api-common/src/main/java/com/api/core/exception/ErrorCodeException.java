package com.api.core.exception;

import com.api.core.gateway.APICode;

/**
 * 异常 - 错误代码
 */
public class ErrorCodeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ErrorCodeException() {
	}

	public ErrorCodeException(APICode code) {
		this(code.getCode(), code.getMessage());
	}
	
	public ErrorCodeException(APICode code, String message) {
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

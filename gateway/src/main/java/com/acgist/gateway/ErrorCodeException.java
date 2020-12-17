package com.acgist.gateway;

import com.acgist.gateway.config.GatewayCode;

/**
 * <p>状态码异常</p>
 */
public class ErrorCodeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ErrorCodeException(GatewayCode code) {
		this(code.getCode(), code.getMessage());
	}
	
	public ErrorCodeException(GatewayCode code, String message) {
		this(code.getCode(), message);
	}
	
	public ErrorCodeException(String code, String message) {
		super(message);
		this.code = code;
	}
	
	public ErrorCodeException(GatewayCode code, Throwable throwable) {
		this(code.getCode(), code.getMessage(), throwable);
	}
	
	public ErrorCodeException(GatewayCode code, String message, Throwable throwable) {
		this(code.getCode(), message, throwable);
	}
	
	public ErrorCodeException(String code, String message, Throwable throwable) {
		super(message, throwable);
		this.code = code;
	}

	private final String code;

	public String getErrorCode() {
		return code;
	}

}

package com.acgist.gateway.config;

/**
 * <p>响应状态码</p>
 * <p>0000=成功</p>
 * <p>1xxx=请求错误</p>
 * <p>2xxx=服务错误</p>
 * <p>9999=未知错误</p>
 */
public enum GatewayCode {

	CODE_0000("0000", "成功"),
	CODE_1000("1000", "未知接口"),
	CODE_1001("1001", "已有请求处理"),
	CODE_3000("3000", "数据格式错误"),
	CODE_3001("3001", "验签失败"),
	CODE_9999("9999", "未知错误");
	
	/**
	 * <p>状态码</p>
	 */
	private final String code;
	/**
	 * <p>响应信息</p>
	 */
	private final String message;

	/**
	 * @param code 状态码
	 * @param message 响应信息
	 */
	private GatewayCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public static final GatewayCode of(String code) {
		final GatewayCode[] gatewayCodes = GatewayCode.values();
		for (GatewayCode gatewayCode : gatewayCodes) {
			if(gatewayCode.code.equals(code)) {
				return gatewayCode;
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

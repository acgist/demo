package com.acgist.gateway.response;

import java.util.Map;

import javax.validation.constraints.NotBlank;

import com.acgist.gateway.ErrorCodeException;
import com.acgist.gateway.Gateway;
import com.acgist.gateway.config.GatewayCode;
import com.acgist.gateway.request.GatewayRequest;
import com.acgist.utils.APIUtils;
import com.acgist.utils.DateUtils;
import com.acgist.utils.ValidatorUtils;

/**
 * 抽象响应，使用：request.buildResponse(session).success().response();
 * 必须要设置响应状态码
 */
public class GatewayResponse extends Gateway {

	private static final long serialVersionUID = 1L;

	protected String queryId; // 请求ID
	protected String requestTime; // 请求时间
	@NotBlank(message = "响应时间不能为空")
	protected String responseTime; // 响应时间
	@NotBlank(message = "响应状态码不能为空")
	protected String responseCode; // 响应码
	protected String responseMsg; // 响应内容

	public String getQueryId() {
		return queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	public String getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}
	
	/**
	 * 将请求数据内容设置到响应内容中
	 */
	public GatewayResponse of(GatewayRequest request) {
		if(request != null) {
			valueOfMap(request.toMap());
		}
		return this;
	}
	
	/**
	 * 设置参数
	 */
	public GatewayResponse valueOfMap(final Map<String, String> data) {
		if(data != null) {
			data.remove(Gateway.PROPERTY_SIGNATURE); // 移除签名
			APIUtils.mapSetBean(this, data);
		}
		return this;
	}
	
	/**
	 * 成功
	 */
	public GatewayResponse success() {
		return message(GatewayCode.CODE_0000);
	}
	
	/**
	 * 失败
	 */
	public GatewayResponse fail() {
		return message(GatewayCode.CODE_9999);
	}
	
	/**
	 * 设置响应信息
	 */
	public GatewayResponse message(GatewayCode code) {
		return message(code.getCode(), code.getMessage());
	}
	
	/**
	 * 设置响应信息
	 */
	public GatewayResponse message(GatewayCode code, String message) {
		return message(code.getCode(), message);
	}
	
	/**
	 * 设置响应信息
	 */
	public GatewayResponse message(String code, String message) {
		this.responseCode = code;
		this.responseMsg = message;
		this.responseTime = DateUtils.apiTime();
		return this;
	}

	/**
	 * 获取签名后响应内容
	 */
	public Map<String, String> response() {
		final Map<String, String> data = signature();
		String message = ValidatorUtils.verify(this); // 数据校验
		if(message != null) {
			throw new ErrorCodeException(GatewayCode.CODE_3000, message);
		}
		return data;
	}
	
	/**
	 * 默认响应
	 */
	public static final GatewayResponse builder() {
		return new GatewayResponse();
	}
	
}

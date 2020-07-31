package com.acgist.api.response;

import java.util.Map;

import javax.validation.constraints.NotBlank;

import com.acgist.api.API;
import com.acgist.api.ResponseCode;
import com.acgist.api.request.APIRequest;
import com.acgist.modules.exception.ErrorCodeException;
import com.acgist.modules.utils.APIUtils;
import com.acgist.modules.utils.DateUtils;
import com.acgist.modules.utils.ValidatorUtils;

/**
 * 抽象响应，使用：request.buildResponse(session).success().response();
 * 必须要设置响应状态码
 */
public class APIResponse extends API {

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
	public APIResponse valueOfRequest(APIRequest request) {
		if(request != null) {
			valueOfMap(request.data());
		}
		return this;
	}
	
	/**
	 * 设置参数
	 */
	public APIResponse valueOfMap(final Map<String, String> data) {
		if(data != null) {
			data.remove(API.PROPERTY_SIGN); // 移除签名
			APIUtils.mapSetBean(this, data);
		}
		return this;
	}
	
	/**
	 * 成功
	 */
	public APIResponse success() {
		return message(ResponseCode.CODE_0000);
	}
	
	/**
	 * 失败
	 */
	public APIResponse fail() {
		return message(ResponseCode.CODE_9999);
	}
	
	/**
	 * 设置响应信息
	 */
	public APIResponse message(ResponseCode code) {
		return message(code.getCode(), code.getMessage());
	}
	
	/**
	 * 设置响应信息
	 */
	public APIResponse message(ResponseCode code, String message) {
		return message(code.getCode(), message);
	}
	
	/**
	 * 设置响应信息
	 */
	public APIResponse message(String code, String message) {
		this.responseCode = code;
		this.responseMsg = message;
		this.responseTime = DateUtils.apiTime();
		return this;
	}

	/**
	 * 获取签名后响应内容
	 */
	public Map<String, String> response() {
		final Map<String, String> data = sign();
		String message = ValidatorUtils.verify(this); // 数据校验
		if(message != null) {
			throw new ErrorCodeException(ResponseCode.CODE_3000, message);
		}
		return data;
	}
	
	/**
	 * 默认响应
	 */
	public static final APIResponse builder() {
		return new APIResponse();
	}
	
}

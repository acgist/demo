package com.acgist.api.request;

import javax.validation.constraints.NotBlank;

import com.acgist.api.API;

/**
 * 抽象请求
 */
public class APIRequest extends API {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "请求时间不能为空")
	protected String requestTime; // 请求时间

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

}

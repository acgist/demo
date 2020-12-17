package com.acgist.gateway.notice;

import java.io.Serializable;

import com.acgist.gateway.request.GatewayRequest;
import com.acgist.gateway.response.GatewayResponse;

/**
 * 异步通知
 */
public class NoticeMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private GatewayRequest apiRequest;
	private GatewayResponse apiResponse;

	public NoticeMessage() {
	}

	public NoticeMessage(GatewayRequest apiRequest, GatewayResponse apiResponse) {
		this.apiRequest = apiRequest;
		this.apiResponse = apiResponse;
	}

	public GatewayRequest getApiRequest() {
		return apiRequest;
	}

	public void setApiRequest(GatewayRequest apiRequest) {
		this.apiRequest = apiRequest;
	}

	public GatewayResponse getApiResponse() {
		return apiResponse;
	}

	public void setApiResponse(GatewayResponse apiResponse) {
		this.apiResponse = apiResponse;
	}

}

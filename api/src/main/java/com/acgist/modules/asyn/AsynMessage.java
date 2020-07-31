package com.acgist.modules.asyn;

import java.io.Serializable;

import com.acgist.api.request.APIRequest;
import com.acgist.api.response.APIResponse;

/**
 * 异步通知
 */
public class AsynMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private APIRequest apiRequest;
	private APIResponse apiResponse;

	public AsynMessage() {
	}

	public AsynMessage(APIRequest apiRequest, APIResponse apiResponse) {
		this.apiRequest = apiRequest;
		this.apiResponse = apiResponse;
	}

	public APIRequest getApiRequest() {
		return apiRequest;
	}

	public void setApiRequest(APIRequest apiRequest) {
		this.apiRequest = apiRequest;
	}

	public APIResponse getApiResponse() {
		return apiResponse;
	}

	public void setApiResponse(APIResponse apiResponse) {
		this.apiResponse = apiResponse;
	}

}

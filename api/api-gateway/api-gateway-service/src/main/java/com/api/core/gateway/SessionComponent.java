package com.api.core.gateway;

import java.io.Serializable;

import com.api.core.gateway.request.APIRequest;
import com.api.core.gateway.response.APIResponse;
import com.netflix.zuul.context.RequestContext;

/**
 * 请求数据
 */
public class SessionComponent implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String SESSION_COMPONENT = "SESSION_COMPONENT";
	
	private String json; // 原始请求数据
	private String queryId; // 唯一标识
	private APIType apiType; // 请求类型
	private APIRequest request; // 请求数据
	private APIResponse response; // 响应数据

	public static final SessionComponent getInstance(RequestContext context) {
		return (SessionComponent) context.get(SESSION_COMPONENT);
	}
	
	public static final SessionComponent newInstance(String queryId, RequestContext context) {
		SessionComponent session = new SessionComponent();
		session.queryId = queryId;
		context.set(SESSION_COMPONENT, session);
		return session;
	}

	public String getQueryId() {
		return queryId;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}
	
	public APIType getApiType() {
		return apiType;
	}
	
	public void setApiType(APIType apiType) {
		this.apiType = apiType;
	}

	public APIRequest getRequest() {
		return request;
	}

	public void setRequest(APIRequest request) {
		this.request = request;
	}

	public APIResponse getResponse() {
		return response;
	}

	public void setResponse(APIResponse response) {
		this.response = response;
	}

	/**
	 * 销毁资源
	 */
	public void destroy(RequestContext context) {
		context.remove(SESSION_COMPONENT);
	}
	
}

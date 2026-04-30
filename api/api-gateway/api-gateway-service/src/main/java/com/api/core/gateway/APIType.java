package com.api.core.gateway;

import javax.servlet.http.HttpServletRequest;

import com.api.core.gateway.request.APIRequest;
import com.api.core.gateway.response.APIResponse;
import com.api.core.order.config.APIConstOrderURL;
import com.api.core.order.gateway.request.PayRequest;
import com.api.core.order.gateway.request.QueryRequest;
import com.api.core.order.gateway.response.PayResponse;
import com.api.core.order.gateway.response.QueryResponse;

/**
 * 接口类型
 */
public enum APIType {

	ORDER_PAY("订单支付", APIConstOrderURL.URL_GATEWAY_ORDER_PAY, PayRequest.class, PayResponse.class, true),
	ORDER_QUERY("订单查询", APIConstOrderURL.URL_GATEWAY_ORDER_QUERY, QueryRequest.class, QueryResponse.class);

	private boolean record; // 是否记录
	private String typeName; // 接口名称
	private String requestURL; // 请求地址
	private Class<APIRequest> requestClazz; // 请求类型
	private Class<APIResponse> responseClazz; // 响应类型

	private <T extends APIRequest, K extends APIResponse> APIType(String typeName, String requestURL, Class<T> requestClazz, Class<K> responseClazz) {
		this(typeName, requestURL, requestClazz, responseClazz, false);
	}

	// TODO 优化泛型
	@SuppressWarnings("unchecked")
	private <T extends APIRequest, K extends APIResponse> APIType(String typeName, String requestURL, Class<T> requestClazz, Class<K> responseClazz, boolean record) {
		this.record = record;
		this.typeName = typeName;
		this.requestURL = requestURL;
		this.requestClazz = (Class<APIRequest>) requestClazz;
		this.responseClazz = (Class<APIResponse>) responseClazz;
	}

	public static final APIType valueOfRequest(HttpServletRequest request) {
		final String requestURL = request.getServletPath();
		for (APIType api : APIType.values()) {
			if (api.requestURL.equals(requestURL)) {
				return api;
			}
		}
		return null;
	}
	
	public boolean record() {
		return record;
	}
	
	public String typeName() {
		return typeName;
	}

	public String requestURL() {
		return requestURL;
	}

	public Class<APIRequest> reqeustClazz() {
		return requestClazz;
	}

	public Class<APIResponse> responseClazz() {
		return responseClazz;
	}

}

package com.api.core.endpoint.impl;

import com.api.core.endpoint.Endpoint;
import com.api.core.endpoint.EndpointExecutor;
import com.api.utils.HttpEntityUtils;

/**
 * 端点 - BUS刷新配置
 */
public class BusRefreshExecutor extends EndpointExecutor<String> {

	public BusRefreshExecutor(String uri, String serviceId) {
		final String requestURI = uri + Endpoint.actuator_bus_refresh.getPath() + "/" + serviceId;
		this.uri = requestURI;
		this.endpoint = Endpoint.actuator_bus_refresh;
	}

	@Override
	public String executeEndpoint() {
		return post(HttpEntityUtils.jsonEntity("{}"));
	}

}

package com.api.core.endpoint.impl;

import com.api.core.endpoint.Endpoint;
import com.api.core.endpoint.EndpointExecutor;
import com.api.utils.HttpEntityUtils;

/**
 * 端点 - 应用关闭
 */
public class ShutdownExecutor extends EndpointExecutor<String> {

	public ShutdownExecutor(String uri) {
		this.uri = uri + Endpoint.actuator_shutdown.getPath();
	}

	@Override
	public String executeEndpoint() {
		return post(HttpEntityUtils.jsonEntity("{}"));
	}

}

package com.api.core.endpoint.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.api.core.endpoint.Endpoint;
import com.api.core.endpoint.EndpointExecutor;
import com.api.core.pojo.vo.EndpointVo;
import com.api.utils.JSONUtils;

/**
 * 端点 - 查询端点
 */
public class ActuatorExecutor extends EndpointExecutor<String> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ActuatorExecutor.class);
	
	public ActuatorExecutor(String uri) {
		this.uri = uri + Endpoint.actuator.getPath();
		this.endpoint = Endpoint.actuator;
	}

	@Override
	public String executeEndpoint() {
		return get();
	}
	
	@SuppressWarnings("unchecked")
	public List<EndpointVo> endpoints() {
		final String json = execute();
		if(json == null) {
			return List.of();
		}
		List<EndpointVo> endpoints = new ArrayList<>();
		if(json != null) {
			Map<String, Object> map = JSONUtils.toMap(json);
			Map<String, Map<String, Object>> links = (Map<String, Map<String, Object>>) map.get("_links");
			links.forEach((name, actuator) -> {
				actuator.forEach((key, value) -> {
					if(key.equals("href")) {
						Endpoint endpoint = Endpoint.valueOfHref((String) value);
						if(endpoint == null) {
							LOGGER.info("未配置的端点：{}", value);
						} else {
							EndpointVo endpointVo = new EndpointVo(endpoint.name(), endpoint.getName(), (String) value);
							endpoints.add(endpointVo);
						}
					}
				});
			});
		}
		return endpoints;
	}

}

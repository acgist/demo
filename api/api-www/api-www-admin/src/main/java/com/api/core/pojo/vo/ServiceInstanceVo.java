package com.api.core.pojo.vo;

import java.util.List;

/**
 * vo - 服务实例
 */
public class ServiceInstanceVo {

	private String serviceId;
	private String uri;
	private List<EndpointVo> endpoints;

	public ServiceInstanceVo() {
	}

	public ServiceInstanceVo(String serviceId, String uri) {
		this.serviceId = serviceId;
		this.uri = uri;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public List<EndpointVo> getEndpoints() {
		return endpoints;
	}

	public void setEndpoints(List<EndpointVo> endpoints) {
		this.endpoints = endpoints;
	}

}

package com.api.core.pojo.vo;

import java.util.List;

/**
 * vo - 服务信息
 */
public class ServiceVo {

	private String serviceId;
	List<ServiceInstanceVo> instances;

	public ServiceVo() {
	}

	public ServiceVo(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public List<ServiceInstanceVo> getInstances() {
		return instances;
	}

	public void setInstances(List<ServiceInstanceVo> instances) {
		this.instances = instances;
	}

	public int getInstanceCount() {
		return this.instances.size();
	}

}
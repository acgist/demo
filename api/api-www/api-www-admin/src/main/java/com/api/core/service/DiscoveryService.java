package com.api.core.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import com.api.core.endpoint.impl.ActuatorExecutor;
import com.api.core.pojo.vo.ServiceVo;
import com.api.core.pojo.vo.ServiceInstanceVo;

/**
 * service - 服务
 */
@Service
public class DiscoveryService {

	@Autowired
	private DiscoveryClient discoveryClient;
	
	/**
	 * 查询所有服务
	 */
	public List<ServiceVo> services() {
		List<ServiceVo> services = new ArrayList<>();
		discoveryClient
		.getServices()
		.forEach(serviceId -> {
			ServiceVo service = new ServiceVo(serviceId);
			List<ServiceInstanceVo> instances = instances(serviceId);
			service.setInstances(instances);
			services.add(service);
		});
		return services;
	}
	
	/**
	 * 查询服务所有实例
	 * @param serviceId 服务ID
	 */
	public List<ServiceInstanceVo> instances(String serviceId) {
		List<ServiceInstanceVo> instances = new ArrayList<>();
		discoveryClient
		.getInstances(serviceId)
		.forEach(instance -> {
			ServiceInstanceVo serviceInstanceVo = new ServiceInstanceVo(serviceId, instance.getUri().toString());
			ActuatorExecutor executor = new ActuatorExecutor(instance.getUri().toString());
			serviceInstanceVo.setEndpoints(executor.endpoints());
			instances.add(serviceInstanceVo);
		});
		return instances;
	}
	
}

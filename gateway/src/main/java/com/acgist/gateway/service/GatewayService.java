package com.acgist.gateway.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.acgist.gateway.request.GatewayRequest;
import com.acgist.gateway.response.GatewayResponse;

/**
 * 网关记录表
 */
@Service
public class GatewayService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayService.class);
	
	public void save(GatewayRequest request) {
		LOGGER.info("保存网关记录：{}", request);
	}
	
	public void update(GatewayResponse response) {
		LOGGER.info("更新网关记录：{}", response);
	}
	
}

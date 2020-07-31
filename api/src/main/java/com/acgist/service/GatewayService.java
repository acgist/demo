package com.acgist.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.acgist.api.request.APIRequest;
import com.acgist.api.response.APIResponse;

/**
 * 网关记录表
 */
@Service
public class GatewayService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayService.class);
	
	public void save(APIRequest request) {
		LOGGER.info("保存网关记录：{}", request);
	}
	
	public void update(APIResponse response) {
		LOGGER.info("更新网关记录：{}", response);
	}
	
}

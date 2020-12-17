package com.acgist.gateway.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.acgist.gateway.request.GatewayRequest;

/**
 * 网关记录表
 */
@Service
public class GatewayService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayService.class);

	public static final String URL_GATEWAY = "/gateway";
	
	public static final String GATEWAY = "gateway";
	public static final String GATEWAY_CODE = "code";
	public static final String GATEWAY_MESSAGE = "message";
	public static final String GATEWAY_QUERY_ID = "queryId";
	public static final String GATEWAY_SIGNATURE = "signature";
	public static final String GATEWAY_NOTICE_URL = "noticeURL";
	public static final String GATEWAY_RESPONSE_TIME = "responseTime";
	
	/**
	 * <p>保存网关</p>
	 * 
	 * @param queryId 请求ID
	 * @param request 请求
	 * @param response 响应
	 */
	public void update(String queryId, GatewayRequest request, Map<String, Object> response) {
		LOGGER.info("保存网关：{}", queryId);
	}
	
}

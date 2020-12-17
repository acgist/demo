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
	 * <p>保存请求</p>
	 * 
	 * @param queryId 请求ID
	 * @param request 请求
	 */
	public void save(String queryId, GatewayRequest request) {
		LOGGER.info("保存网关记录：{}", request);
	}
	
	/**
	 * <p>保存响应</p>
	 * 
	 * @param queryId 请求ID
	 * @param response 响应
	 */
	public void update(String queryId, Map<String, Object> response) {
		LOGGER.info("更新网关记录：{}", response);
	}
	
}

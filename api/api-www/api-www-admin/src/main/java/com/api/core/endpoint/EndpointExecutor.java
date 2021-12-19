package com.api.core.endpoint;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * 端点执行<br>
 * 执行端点请求，获取请求结果
 */
public abstract class EndpointExecutor<T> {

	public static final String METHOD_GET = "get"; // GET请求
	public static final String METHOD_POST = "post"; // POST请求
	public static final String HEAD_ACTUATOR = "/actuator"; // 获取端点的地址
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EndpointExecutor.class);
	
	protected String uri;
	protected Endpoint endpoint;

	/**
	 * 端点执行
	 */
	public T execute() {
		return executeEndpoint();
	}

	/**
	 * 具体执行
	 */
	public abstract T executeEndpoint();

	/**
	 * GET请求
	 */
	public String get() {
		RestTemplate rest = new RestTemplate();
		try {
			ResponseEntity<String> result = rest.getForEntity(URI.create(uri), String.class);
			return result.getBody();
		} catch (Exception e) {
			LOGGER.error("服务端点执行异常，端点地址：{}，端点名称：{}", this.uri, this.endpoint.getName(), e);
		}
		return null;
	}

	/**
	 * POST请求
	 */
	public String post(Object request) {
		RestTemplate rest = new RestTemplate();
		try {
			ResponseEntity<String> result = rest.postForEntity(URI.create(uri), request, String.class);
			return result.getBody();
		} catch (Exception e) {
			LOGGER.error("服务端点执行异常，端点地址：{}，端点名称：{}", this.uri, this.endpoint.getName(), e);
		}
		return null;
	}
	
}

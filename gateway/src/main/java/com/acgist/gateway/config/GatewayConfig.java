package com.acgist.gateway.config;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * <p>网关配置</p>
 */
@Component
public class GatewayConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayConfig.class);
	
	@PostConstruct
	public void init() {
		this.apiType();
	}

	private void apiType() {
		final Gateway[] apiTypes = Gateway.values();
		for (Gateway apiType : apiTypes) {
			LOGGER.info(
				"Gateway：{}，名称：{}，请求地址：{}，是否保存记录：{}，请求Class：{}",
				apiType.name(),
				apiType.typeName(),
				apiType.requestURL(),
				apiType.record(),
				apiType.reqeustClass()
			);
		}
	}
	
}

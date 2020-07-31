package com.acgist.config;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.acgist.api.APIType;

/**
 * 对API请求响应进行检查
 */
@Component
public class APIConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(APIConfig.class);
	
	@PostConstruct
	public void init() {
		apiType();
	}

	private void apiType() {
		APIType[] apiTypes = APIType.values();
		for (APIType apiType : apiTypes) {
			LOGGER.info(
				"API名称：{}，类型：{}，请求地址：{}，是否保存记录：{}，请求CLASS：{}",
				apiType.typeName(),
				apiType.name(),
				apiType.requestURL(),
				apiType.record(),
				apiType.reqeustClazz()
			);
		}
	}
	
}

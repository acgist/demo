package com.acgist.diy;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DiyService {

	private static final Logger LOGGER_DIY = LoggerFactory.getLogger(DiyService.class);
	
	@PostConstruct
	public void init() {
		LOGGER_DIY.info("测试：diy");
	}
	
}

package com.acgist;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DiyDataService {

	private static final Logger LOGGER_DIY_DATA = LoggerFactory.getLogger("diy-data");
	
	@PostConstruct
	public void init() {
		LOGGER_DIY_DATA.info("测试：diy_data");
	}
	
}

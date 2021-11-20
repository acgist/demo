package com.acgist.service;

import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import com.alibaba.nacos.common.utils.ThreadUtils;

@DubboService(protocol = "dubbo", retries = 0, timeout = 10000)
public class UserService implements IUserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private Config config;
	
	@Override
	public String name() {
		ThreadUtils.sleep(1000);
		LOGGER.info("服务调用");
		return ObjectUtils.isEmpty(this.config.getName()) ? "null" : this.config.getName();
	}
	
}

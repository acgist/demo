package com.acgist.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DubboService(protocol = "dubbo")
public class OrderService implements IOrderService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
	
	@Override
	public List<String> list() {
		LOGGER.info("服务调用");
		return new ArrayList<String>() {
			private static final long serialVersionUID = 1L;
			{
				this.add("");
			}
		};
	}
	
}

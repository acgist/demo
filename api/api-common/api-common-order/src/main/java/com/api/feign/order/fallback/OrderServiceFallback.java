package com.api.feign.order.fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import com.api.core.config.APIConstURL;
import com.api.core.order.pojo.message.OrderMessage;
import com.api.data.order.pojo.entity.OrderEntity;
import com.api.feign.order.service.OrderService;
import com.api.feign.service.BaseServiceFallback;

/**
 * 服务熔断 - 订单
 */
@Component
@RequestMapping(APIConstURL.URL_FALLBACK_SERVICE)
public class OrderServiceFallback extends BaseServiceFallback implements OrderService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceFallback.class);
	
	@Override
	public OrderMessage order(OrderEntity order) {
		LOGGER.warn("服务调用失败：创建订单，执行退款，订单号：{}", order.getOrderId());
		OrderMessage message = new OrderMessage();
		message.buildFail();
		message.setEntity(order);
		return message;
	}

}

package com.api.core.order.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.core.order.pojo.message.OrderMessage;
import com.api.data.order.pojo.entity.OrderEntity;
import com.api.data.order.repository.OrderRepository;
import com.api.data.service.APIEntityService;

/**
 * service - 订单
 */
@Service
public class OrderServiceImpl implements APIEntityService {

	@Autowired
	private OrderRepository orderRepository;
	
	/**
	 * 创建订单
	 * @param order 订单信息
	 * @return 订单实体
	 */
	public OrderMessage order(OrderEntity order) {
		OrderMessage message = new OrderMessage();
		if(!verifyEntity(order, message)) {
			return message;
		}
		orderRepository.save(order);
		message.buildSuccess();
		message.setEntity(order);
		return message;
	}

}

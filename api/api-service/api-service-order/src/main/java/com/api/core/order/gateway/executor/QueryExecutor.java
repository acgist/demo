package com.api.core.order.gateway.executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.api.core.gateway.APICode;
import com.api.core.gateway.executor.APIExecutor;
import com.api.core.order.gateway.request.QueryRequest;
import com.api.core.order.gateway.response.QueryResponse;
import com.api.data.order.pojo.entity.OrderEntity;
import com.api.data.order.repository.OrderRepository;
import com.api.utils.DateUtils;

/**
 * 请求执行器 - 订单查询
 */
@Component
@Scope("prototype")
public class QueryExecutor extends APIExecutor<QueryRequest, QueryResponse> {

	@Autowired
	private OrderRepository orderRepository;
	
	@Override
	protected void execute() {
		String orderId = request.getOrderId();
		OrderEntity order = orderRepository.findByOrderId(orderId);
		if(order == null) {
			response.buildMessage(APICode.CODE_2002);
			return;
		}
		response.setCreateDate(DateUtils.date(order.getCreateDate()));
		response.buildSuccess();
	}

}

package com.api.core.order.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.core.config.APIConstURL;
import com.api.core.order.config.APIConstOrderURL;
import com.api.core.order.gateway.executor.PayExecutor;
import com.api.core.order.gateway.executor.QueryExecutor;
import com.api.core.order.gateway.request.PayRequest;
import com.api.core.order.gateway.request.QueryRequest;
import com.api.core.order.gateway.response.PayResponse;
import com.api.core.order.gateway.response.QueryResponse;
import com.api.utils.APIUtils;

/**
 * 网关 - 订单
 */
@RestController
@RequestMapping(APIConstURL.URL_GATEWAY)
public class OrderController {

	@Autowired
	private ApplicationContext context;
	
	/**
	 * 订单支付
	 */
	@PostMapping(APIConstOrderURL.URL_ORDER_PAY)
	public PayResponse pay(@RequestBody PayRequest request) {
		return APIUtils.newInstance(context, PayExecutor.class).execute(request);
	}
	
	/**
	 * 订单查询
	 */
	@PostMapping(APIConstOrderURL.URL_ORDER_QUERY)
	public QueryResponse query(@RequestBody QueryRequest request) {
		return APIUtils.newInstance(context, QueryExecutor.class).execute(request);
	}
	
}

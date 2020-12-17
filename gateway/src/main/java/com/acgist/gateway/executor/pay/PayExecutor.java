package com.acgist.gateway.executor.pay;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.acgist.gateway.config.GatewayCode;
import com.acgist.gateway.executor.GatewayExecutor;
import com.acgist.gateway.request.pay.PayRequest;
import com.acgist.gateway.response.pay.PayResponse;

/**
 * 交易
 */
@Component
@Scope("prototype")
public class PayExecutor extends GatewayExecutor<PayRequest, PayResponse> {

	@Override
	public void execute() {
		String orderId = request.getOrderId();
		if("fail".equals(orderId)) {
			response.message(GatewayCode.CODE_9999, "交易失败");
		} else if ("exception".equals(orderId)) {
			throw new RuntimeException();
		} else {
			response.success();
		}
	}

}

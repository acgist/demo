package com.acgist.api.executor.pay;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.acgist.api.ResponseCode;
import com.acgist.api.executor.APIExecutor;
import com.acgist.api.request.pay.PayRequest;
import com.acgist.api.response.pay.PayResponse;

/**
 * 交易
 */
@Component
@Scope("prototype")
public class PayExecutor extends APIExecutor<PayRequest, PayResponse> {

	@Override
	public void execute() {
		String orderId = request.getOrderId();
		if("fail".equals(orderId)) {
			response.message(ResponseCode.CODE_9999, "交易失败");
		} else if ("exception".equals(orderId)) {
			throw new RuntimeException();
		} else {
			response.success();
		}
	}

}

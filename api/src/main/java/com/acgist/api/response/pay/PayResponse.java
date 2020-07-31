package com.acgist.api.response.pay;

import com.acgist.api.response.APIResponse;

/**
 * 交易响应
 */
public class PayResponse extends APIResponse {

	private static final long serialVersionUID = 1L;

	private String orderId; // 订单号

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

}

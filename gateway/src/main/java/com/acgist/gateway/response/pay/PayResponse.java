package com.acgist.gateway.response.pay;

import com.acgist.gateway.response.GatewayResponse;

/**
 * 交易响应
 */
public class PayResponse extends GatewayResponse {

	private static final long serialVersionUID = 1L;

	private String orderId; // 订单号

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

}

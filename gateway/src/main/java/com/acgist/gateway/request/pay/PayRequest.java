package com.acgist.gateway.request.pay;

import javax.validation.constraints.NotBlank;

import com.acgist.gateway.request.GatewayRequest;

/**
 * 交易请求
 */
public class PayRequest extends GatewayRequest {
	
	private static final long serialVersionUID = 1L;

	@NotBlank(message = "交易订单号不能为空")
	private String orderId;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

}

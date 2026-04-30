package com.api.core.order.gateway.request;

import javax.validation.constraints.NotBlank;

import com.api.core.gateway.request.APIRequest;

/**
 * 请求 - 创建订单
 */
public class PayRequest extends APIRequest {
	
	private static final long serialVersionUID = 1L;

	@NotBlank(message = "订单号不能为空")
	private String orderId;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

}

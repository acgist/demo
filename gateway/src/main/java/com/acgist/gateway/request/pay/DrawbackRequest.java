package com.acgist.gateway.request.pay;

import javax.validation.constraints.NotBlank;

import com.acgist.gateway.request.GatewayRequest;

/**
 * <p>交易退款请求</p>
 */
public class DrawbackRequest extends GatewayRequest {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>交易订单编号</p>
	 */
	@NotBlank(message = "交易订单编号不能为空")
	private String orderId;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
}

package com.api.core.order.gateway.response;

import com.api.core.gateway.response.APIResponse;

/**
 * 响应 - 订单查询
 */
public class QueryResponse extends APIResponse {

	private static final long serialVersionUID = 1L;

	private String orderId;
	private String createDate;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

}

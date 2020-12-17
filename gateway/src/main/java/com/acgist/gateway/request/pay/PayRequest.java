package com.acgist.gateway.request.pay;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.acgist.gateway.request.GatewayRequest;

/**
 * <p>交易请求</p>
 */
public class PayRequest extends GatewayRequest {
	
	private static final long serialVersionUID = 1L;

	/**
	 * <p>交易金额</p>
	 */
	@Min(value = 1, message = "交易金额错误")
	private Long amount;
	/**
	 * <p>交易订单编号</p>
	 */
	@NotBlank(message = "交易订单编号不能为空")
	private String orderId;
	/**
	 * <p>通知地址</p>
	 */
	private String noticeURL;

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getNoticeURL() {
		return noticeURL;
	}

	public void setNoticeURL(String noticeURL) {
		this.noticeURL = noticeURL;
	}

}

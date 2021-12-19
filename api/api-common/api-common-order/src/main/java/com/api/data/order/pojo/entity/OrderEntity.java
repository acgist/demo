package com.api.data.order.pojo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.api.core.order.gateway.request.PayRequest;
import com.api.data.pojo.entity.BaseEntity;
import com.api.data.pojo.entity.ValueOfRequest;

/**
 * entity - 订单
 */
@Entity
@Table(name = "tb_order", indexes = {
	@Index(name = "index_order_order_id", columnList = "orderId", unique = true)
})
public class OrderEntity extends BaseEntity implements ValueOfRequest<PayRequest> {

	private static final long serialVersionUID = 1L;

	public static final String PROPERTY_ORDER_ID = "orderId"; // 订单号
	
	/**
	 * 订单号
	 */
	@Size(max = 100, message = "订单号长度不能超过100")
	@NotBlank(message = "订单号不能为空")
	private String orderId;

	@Column(nullable = false, length = 100)
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@Override
	public void valueOfRequest(PayRequest request) {
		this.setOrderId(request.getOrderId());
	}

}

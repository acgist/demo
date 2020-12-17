package com.acgist.gateway.config;

import com.acgist.gateway.executor.GatewayExecutor;
import com.acgist.gateway.executor.pay.DrawbackExecutor;
import com.acgist.gateway.executor.pay.PayExecutor;
import com.acgist.gateway.executor.pay.QueryExecutor;
import com.acgist.gateway.request.GatewayRequest;
import com.acgist.gateway.request.pay.DrawbackRequest;
import com.acgist.gateway.request.pay.PayRequest;
import com.acgist.gateway.request.pay.QueryRequest;

/**
 * <p>网关</p>
 */
public enum Gateway {

	/**
	 * <p>交易</p>
	 */
	PAY("/pay", "交易", PayRequest.class, PayExecutor.class, true),
	/**
	 * <p>交易查询</p>
	 */
	PAY_QUERY("/pay/query", "交易查询", QueryRequest.class, QueryExecutor.class),
	/**
	 * <p>交易退款</p>
	 */
	PAY_DRAWBACK("/pay/drawback", "交易退款", DrawbackRequest.class, DrawbackExecutor.class, true);

	/**
	 * <p>是否记录</p>
	 */
	private final boolean record;
	/**
	 * <p>请求地址</p>
	 */
	private final String gateway;
	/**
	 * <p>接口名称</p>
	 */
	private final String gatewayName;
	/**
	 * <p>请求类型</p>
	 */
	private final Class<GatewayRequest> requestClass;
	/**
	 * <p>请求执行类型</p>
	 */
	private final Class<GatewayExecutor<GatewayRequest>> executorClass;

	private <T extends GatewayRequest, K extends GatewayExecutor<T>> Gateway(String gateway, String gatewayName, Class<T> requestClass, Class<K> executorClass) {
		this(gateway, gatewayName, requestClass, executorClass, false);
	}

	@SuppressWarnings("unchecked")
	private <T extends GatewayRequest, K extends GatewayExecutor<T>> Gateway(String gateway, String gatewayName, Class<T> requestClass, Class<K> executorClass, boolean record) {
		this.record = record;
		this.gateway = gateway;
		this.gatewayName = gatewayName;
		this.requestClass = (Class<GatewayRequest>) requestClass;
		this.executorClass = (Class<GatewayExecutor<GatewayRequest>>) executorClass;
	}

	public static final Gateway of(String value) {
		final Gateway[] gateways = Gateway.values();
		for (Gateway gateway : gateways) {
			if (gateway.gateway.equals(value)) {
				return gateway;
			}
		}
		return null;
	}
	
	public boolean record() {
		return this.record;
	}
	
	public String gatewayName() {
		return this.gatewayName;
	}

	public Class<GatewayRequest> reqeustClass() {
		return this.requestClass;
	}

	public Class<GatewayExecutor<GatewayRequest>> executorClass() {
		return this.executorClass;
	}

}

package com.acgist.gateway.config;

/**
 * <p>网关地址</p>
 */
public interface GatewayURL {

	/**
	 * <p>交易</p>
	 */
	String PAY = "/gateway/api/pay";
	/**
	 * <p>交易查询</p>
	 */
	String PAY_QUERY = "/gateway/api/pay/query";
	/**
	 * <p>交易退款</p>
	 */
	String PAY_DRAWBACK = "/gateway/api/pay/drawback";

}

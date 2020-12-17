package com.acgist.gateway.config;

import javax.servlet.http.HttpServletRequest;

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
	PAY("交易", GatewayURL.PAY, PayRequest.class, true),
	/**
	 * <p>交易查询</p>
	 */
	PAY_QUERY("交易查询", GatewayURL.PAY_QUERY, QueryRequest.class),
	/**
	 * <p>交易退款</p>
	 */
	PAY_DRAWBACK("交易退款", GatewayURL.PAY_DRAWBACK, DrawbackRequest.class, true);

	/**
	 * <p>是否记录</p>
	 */
	private final boolean record;
	/**
	 * <p>接口名称</p>
	 */
	private final String typeName;
	/**
	 * <p>请求地址</p>
	 */
	private final String requestURL;
	/**
	 * <p>请求类型</p>
	 */
	private final Class<GatewayRequest> requestClass;

	private <T extends GatewayRequest> Gateway(String typeName, String requestURL, Class<T> requestClass) {
		this(typeName, requestURL, requestClass, false);
	}

	@SuppressWarnings("unchecked")
	private <T extends GatewayRequest> Gateway(String typeName, String requestURL, Class<T> requestClass, boolean record) {
		this.record = record;
		this.typeName = typeName;
		this.requestURL = requestURL;
		this.requestClass = (Class<GatewayRequest>) requestClass;
	}

	public static final Gateway of(HttpServletRequest request) {
		final String requestURL = request.getServletPath();
		final Gateway[] gateways = Gateway.values();
		for (Gateway gateway : gateways) {
			if (gateway.requestURL.equals(requestURL)) {
				return gateway;
			}
		}
		return null;
	}
	
	public boolean record() {
		return this.record;
	}
	
	public String typeName() {
		return this.typeName;
	}

	public String requestURL() {
		return this.requestURL;
	}

	public Class<GatewayRequest> reqeustClass() {
		return this.requestClass;
	}

}

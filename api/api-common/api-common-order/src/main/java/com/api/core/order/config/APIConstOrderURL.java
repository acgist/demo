package com.api.core.order.config;

import com.api.core.config.APIConstURL;

/**
 * config - URL - 订单
 */
public interface APIConstOrderURL extends APIConstURL {

	String URL_ORDER = "/order"; // 订单
	String URL_ORDER_PAY = URL_ORDER + "/pay"; // 支付
	String URL_ORDER_QUERY = URL_ORDER + "/query"; // 查询
	
	// 服务
	String URL_SERVICE_ORDER = APIConstURL.URL_SERVICE + URL_ORDER; // 创建
	String URL_SERVICE_ORDER_QUERY = APIConstURL.URL_SERVICE + URL_ORDER_QUERY; // 查询
	
	// 接口
	String URL_GATEWAY_ORDER_PAY = APIConstURL.URL_GATEWAY + URL_ORDER_PAY; // 支付
	String URL_GATEWAY_ORDER_QUERY = APIConstURL.URL_GATEWAY + URL_ORDER_QUERY; // 查询
	
}

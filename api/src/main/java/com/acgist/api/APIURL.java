package com.acgist.api;

/**
 * 请求地址
 */
public interface APIURL {

	String PAY = "/gateway/api/pay"; // 交易
	String PAY_QUERY = "/gateway/api/pay/query"; // 交易查询
	String PAY_DRAWBACK = "/gateway/api/pay/drawback"; // 交易退款

}

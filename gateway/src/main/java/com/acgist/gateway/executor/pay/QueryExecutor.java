package com.acgist.gateway.executor.pay;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.acgist.gateway.executor.GatewayExecutor;
import com.acgist.gateway.request.pay.QueryRequest;
import com.acgist.gateway.response.pay.QueryResponse;

/**
 * 交易查询
 */
@Component
@Scope("prototype")
public class QueryExecutor extends GatewayExecutor<QueryRequest, QueryResponse> {

	@Override
	protected void execute() {
		response.success();
	}

}

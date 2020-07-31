package com.acgist.api.executor.pay;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.acgist.api.executor.APIExecutor;
import com.acgist.api.request.pay.QueryRequest;
import com.acgist.api.response.pay.QueryResponse;

/**
 * 交易查询
 */
@Component
@Scope("prototype")
public class QueryExecutor extends APIExecutor<QueryRequest, QueryResponse> {

	@Override
	protected void execute() {
		response.success();
	}

}

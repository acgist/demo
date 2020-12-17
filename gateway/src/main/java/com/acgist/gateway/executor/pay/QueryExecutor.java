package com.acgist.gateway.executor.pay;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.acgist.gateway.executor.GatewayExecutor;
import com.acgist.gateway.request.pay.QueryRequest;

/**
 * <p>交易查询</p>
 */
@Component
@Scope("prototype")
public class QueryExecutor extends GatewayExecutor<QueryRequest> {

	public static final QueryExecutor newInstance(ApplicationContext context) {
		return context.getBean(QueryExecutor.class);
	}
	
	@Override
	protected void execute() {
		this.session.buildSuccess(Map.of("status", "success"));
	}

}

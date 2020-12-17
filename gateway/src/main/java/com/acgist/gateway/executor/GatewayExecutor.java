package com.acgist.gateway.executor;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.acgist.gateway.GatewaySession;
import com.acgist.gateway.request.GatewayRequest;

/**
 * <p>请求执行者</p>
 * 
 * @author acgist
 */
@Component
@Scope("prototype")
public abstract class GatewayExecutor<T extends GatewayRequest> {

	/**
	 * <p>请求</p>
	 */
	protected T request;
	/**
	 * <p>session</p>
	 */
	protected GatewaySession session;

	@Autowired
	private ApplicationContext context;

	/**
	 * <p>执行请求</p>
	 */
	protected abstract void execute();

	/**
	 * <p>执行请求返回响应</p>
	 * 
	 * @return 响应
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> response() {
		final GatewaySession session = GatewaySession.getInstance(this.context);
		this.request = (T) session.getRequest();
		this.session = session;
		this.execute();
		return session.getResponseData();
	}

}

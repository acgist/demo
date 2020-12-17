package com.acgist.gateway.executor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.acgist.gateway.ErrorCodeException;
import com.acgist.gateway.GatewaySession;
import com.acgist.gateway.config.GatewayCode;
import com.acgist.gateway.request.GatewayRequest;
import com.acgist.gateway.response.GatewayResponse;

/**
 * <p>请求执行者</p>
 */
@Component
@Scope("prototype")
public abstract class GatewayExecutor<T extends GatewayRequest, K extends GatewayResponse> {

	/**
	 * <p>请求</p>
	 */
	protected T request;
	/**
	 * <p>响应</p>
	 */
	protected K response;
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
	 * <p>创建响应</p>
	 */
	public Map<String, String> response() {
		this.init();
		this.execute();
		return this.response.response();
	}

	/**
	 * <p>加载信息</p>
	 */
	@SuppressWarnings("unchecked")
	private void init() {
		final GatewaySession session = GatewaySession.getInstance(this.context);
		this.request = (T) session.getGatewayRequest();
		this.session = session;
		this.response = this.buildResponse();
	}

	/**
	 * <p>创建响应</p>
	 */
	@SuppressWarnings("unchecked")
	private K buildResponse() {
		final ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
		final Type[] types = parameterizedType.getActualTypeArguments();
		final Class<K> responseClass = (Class<K>) types[1]; // 获取响应泛型
		K response = null;
		try {
			response = responseClass.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new ErrorCodeException(GatewayCode.CODE_9999, e);
		}
		response.of(this.request);
		if (this.session != null) {
			response.setQueryId(this.session.getQueryId());
			this.session.setResponse(response);
		}
		return response;
	}

}

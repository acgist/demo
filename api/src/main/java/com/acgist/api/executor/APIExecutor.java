package com.acgist.api.executor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.acgist.api.ResponseCode;
import com.acgist.api.SessionComponent;
import com.acgist.api.request.APIRequest;
import com.acgist.api.response.APIResponse;
import com.acgist.modules.exception.ErrorCodeException;

/**
 * 请求执行者
 */
@Component
@Scope("prototype")
public abstract class APIExecutor<T extends APIRequest, K extends APIResponse> {

	private static final Logger LOGGER = LoggerFactory.getLogger(APIExecutor.class);

	protected T request;
	protected K response;
	protected SessionComponent session;

	@Autowired
	private ApplicationContext context;

	/**
	 * 执行器
	 */
	protected abstract void execute();

	/**
	 * 响应
	 */
	public Map<String, String> response() {
		init();
		execute();
		return this.response.response();
	}

	/**
	 * 初始化
	 */
	@SuppressWarnings("unchecked")
	private void init() {
		final SessionComponent session = SessionComponent.getInstance(context);
		this.request = (T) session.getApiRequest();
		this.session = session;
		this.response = buildResponse();
	}

	/**
	 * 默认生成响应
	 */
	private K buildResponse() {
		K response = buildAPIResponse();
		response.valueOfRequest(this.request);
		if (this.session != null) {
			response.setQueryId(this.session.getQueryId());
			this.session.setApiResponse(response);
		}
		return response;
	}

	/**
	 * 生成新的响应
	 * TODO 优化泛型
	 */
	@SuppressWarnings("unchecked")
	private K buildAPIResponse() {
		final ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
		final Type[] types = parameterizedType.getActualTypeArguments();
		final Class<K> clazz = (Class<K>) types[1]; // 获取第二个泛型
		try {
			return clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			LOGGER.error("生成响应异常", e);
		}
		throw new ErrorCodeException(ResponseCode.CODE_9999);
	}

}

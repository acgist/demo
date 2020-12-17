package com.acgist.gateway.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.gateway.GatewaySession;
import com.acgist.gateway.config.GatewayCode;
import com.acgist.gateway.service.UniqueIdService;
import com.acgist.utils.RedirectUtils;

/**
 * 处理过程中拦截：使用session来记录数据，所以如果一个session处理两次请求，后面的请求数据会覆盖掉前一次的请求
 */
@Component
public class ProcessInterceptor implements HandlerInterceptor {

	@Autowired
	private ApplicationContext context;
	@Autowired
	private UniqueIdService uniqueIdService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final GatewaySession session = GatewaySession.getInstance(context);
		final String queryId = uniqueIdService.id();
		if(session.buildProcess(queryId)) {
			return true;
		}
		RedirectUtils.error(GatewayCode.CODE_1001, request, response);
		return false;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		final GatewaySession session = GatewaySession.getInstance(context);
		session.completeProcess(request);
	}

}

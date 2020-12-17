package com.acgist.gateway.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.gateway.GatewaySession;
import com.acgist.gateway.config.Gateway;
import com.acgist.gateway.notice.NoticeService;
import com.acgist.gateway.service.GatewayService;

/**
 * 报文保存
 */
@Component
public class GatewayInterceptor implements HandlerInterceptor {

	@Autowired
	private NoticeService asynService;
	@Autowired
	private ApplicationContext context;
	@Autowired
	private GatewayService gatewayService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final GatewaySession session = GatewaySession.getInstance(context);
		final Gateway apiType = session.getApiType();
		if(apiType.record()) {
			gatewayService.save(session.getGatewayRequest());
		}
		return true;
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		final GatewaySession session = GatewaySession.getInstance(context);
		final Gateway apiType = session.getApiType();
		if(apiType.record()) {
			gatewayService.update(session.getApiResponse());
			asynService.put(session);
		}
	}

}

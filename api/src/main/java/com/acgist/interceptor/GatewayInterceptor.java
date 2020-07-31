package com.acgist.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.api.APIType;
import com.acgist.api.SessionComponent;
import com.acgist.modules.asyn.AsynService;
import com.acgist.service.GatewayService;

/**
 * 报文保存
 */
@Component
public class GatewayInterceptor implements HandlerInterceptor {

	@Autowired
	private AsynService asynService;
	@Autowired
	private ApplicationContext context;
	@Autowired
	private GatewayService gatewayService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final SessionComponent session = SessionComponent.getInstance(context);
		final APIType apiType = session.getApiType();
		if(apiType.record()) {
			gatewayService.save(session.getApiRequest());
		}
		return true;
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		final SessionComponent session = SessionComponent.getInstance(context);
		final APIType apiType = session.getApiType();
		if(apiType.record()) {
			gatewayService.update(session.getApiResponse());
			asynService.put(session);
		}
	}

}

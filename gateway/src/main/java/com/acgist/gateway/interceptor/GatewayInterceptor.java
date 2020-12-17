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
 * <p>报文保存</p>
 */
@Component
public class GatewayInterceptor implements HandlerInterceptor {

	@Autowired
	private ApplicationContext context;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private GatewayService gatewayService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final GatewaySession session = GatewaySession.getInstance(this.context);
		final Gateway gateway = session.getGateway();
		if(gateway.record()) {
			this.gatewayService.save(session.getQueryId(), session.getRequest());
		}
		return true;
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		final GatewaySession session = GatewaySession.getInstance(this.context);
		final Gateway gateway = session.getGateway();
		if(gateway.record()) {
			this.gatewayService.update(session.getQueryId(), session.getResponseData());
			this.noticeService.put(session);
		}
	}

}

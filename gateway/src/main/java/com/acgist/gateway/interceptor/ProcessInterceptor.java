package com.acgist.gateway.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.gateway.GatewaySession;
import com.acgist.gateway.config.Gateway;
import com.acgist.gateway.config.GatewayCode;
import com.acgist.gateway.notice.NoticeService;
import com.acgist.gateway.service.GatewayService;
import com.acgist.gateway.service.UniqueIdService;

/**
 * <p>拦截处理过程中请求</p>
 * 
 * @author acgist
 */
@Component
public class ProcessInterceptor implements HandlerInterceptor {

	@Autowired
	private ApplicationContext context;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private GatewayService gatewayService;
	@Autowired
	private UniqueIdService uniqueIdService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final GatewaySession session = GatewaySession.getInstance(this.context);
		final String queryId = this.uniqueIdService.id();
		if(session.buildProcess(queryId)) {
			return true;
		}
		session.buildFail(GatewayCode.CODE_1001).response(response);
		return false;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		final GatewaySession session = GatewaySession.getInstance(this.context);
		final Gateway gateway = session.getGateway();
		if(gateway != null && gateway.record()) {
			this.gatewayService.update(session.getQueryId(), session.getResponseData());
			this.noticeService.put(session);
		}
		session.completeProcess(request);
	}

}
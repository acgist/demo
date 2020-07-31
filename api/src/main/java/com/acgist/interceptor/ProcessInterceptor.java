package com.acgist.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.api.ResponseCode;
import com.acgist.api.SessionComponent;
import com.acgist.modules.utils.RedirectUtils;
import com.acgist.service.UniqueIdService;

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
		final SessionComponent session = SessionComponent.getInstance(context);
		final String queryId = uniqueIdService.id();
		if(session.buildProcess(queryId)) {
			return true;
		}
		RedirectUtils.error(ResponseCode.CODE_1001, request, response);
		return false;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		final SessionComponent session = SessionComponent.getInstance(context);
		session.completeProcess(request);
	}

}

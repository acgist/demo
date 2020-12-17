package com.acgist.gateway.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.gateway.GatewaySession;
import com.acgist.gateway.config.GatewayCode;
import com.acgist.gateway.request.GatewayRequest;
import com.acgist.utils.RedirectUtils;

/**
 * 数据格式校验
 */
@Component
public class ValidatorInterceptor implements HandlerInterceptor {

	@Autowired
	private ApplicationContext context;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final GatewaySession session = GatewaySession.getInstance(context);
		final GatewayRequest apiRequest = session.getGatewayRequest();
		final String message = apiRequest.validator();
		if(message != null) {
			RedirectUtils.error(GatewayCode.CODE_3000, message, request, response);
			return false;
		}
		return true;
	}
	
}

package com.acgist.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.api.ResponseCode;
import com.acgist.api.SessionComponent;
import com.acgist.api.request.APIRequest;
import com.acgist.modules.utils.RedirectUtils;

/**
 * 数据格式校验
 */
@Component
public class DataVerifyInterceptor implements HandlerInterceptor {

	@Autowired
	private ApplicationContext context;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final SessionComponent session = SessionComponent.getInstance(context);
		final APIRequest apiRequest = session.getApiRequest();
		final String message = apiRequest.verify();
		if(message != null) {
			RedirectUtils.error(ResponseCode.CODE_3000, message, request, response);
			return false;
		}
		return true;
	}
	
}

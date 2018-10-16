package com.acgist.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.bean.vo.MessageBuilder;
import com.acgist.web.module.SessionComponent;

@Component
public class FaceInterceptor implements HandlerInterceptor {

	@Autowired
	private ApplicationContext context;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String uid = request.getParameter("uid");
		if (uid == null || uid.isEmpty()) {
			SessionComponent sessionComponent = SessionComponent.getInstance(context);
			MessageBuilder.fail().failMessageRequired("uid").buildMessage(sessionComponent).forwardAPIError(request, response);
			return false;
		}
		return true;
	}

}

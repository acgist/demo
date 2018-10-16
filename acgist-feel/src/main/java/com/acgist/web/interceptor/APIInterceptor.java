package com.acgist.web.interceptor;

import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.bean.vo.MessageBuilder;
import com.acgist.web.module.SessionComponent;

@Component
public class APIInterceptor implements HandlerInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(APIInterceptor.class);
	
	@Autowired
	private ApplicationContext context;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		SessionComponent sessionComponent = SessionComponent.getInstance(context);
		if(!sessionComponent.newProcess()) {
			LOGGER.error("HTTP链接上次请求未执行完成！");
			MessageBuilder.fail().failMessage("本条HTTP链接上次请求未处理完成！").buildMessage(sessionComponent).forwardAPIError(request, response);
			return false;
		}
		Map<String, String> reqData = request.getParameterMap().entrySet().stream().collect(Collectors.toMap(entry -> {
			return entry.getKey();
		}, entry -> {
			if(entry.getValue() == null) {
				return null;
			}
			if(entry.getValue().length == 0) {
				return null;
			}
			return entry.getValue()[0];
		}));
		sessionComponent.setReqData(reqData);
		return true;
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		SessionComponent sessionComponent = SessionComponent.getInstance(context);
		sessionComponent.complete();
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}

}

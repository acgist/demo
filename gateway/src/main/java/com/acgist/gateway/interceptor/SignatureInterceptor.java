package com.acgist.gateway.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.gateway.GatewaySession;
import com.acgist.gateway.config.GatewayCode;
import com.acgist.gateway.service.SignatureService;
import com.acgist.utils.JSONUtils;
import com.acgist.utils.RedirectUtils;

/**
 * 签名验证，验证请求所有数据，而不是实体数据
 */
@Component
public class SignatureInterceptor implements HandlerInterceptor {

	@Autowired
	private ApplicationContext context;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final GatewaySession session = GatewaySession.getInstance(context);
		final String json = session.getJson();
		if(json == null) {
			RedirectUtils.error(GatewayCode.CODE_3000, "请求数据不能为空", request, response);
			return false;
		}
		final Map<String, String> data = JSONUtils.jsonToMap(json);
		if(SignatureService.verify(data)) {
			return true;
		}
		RedirectUtils.error(GatewayCode.CODE_3001, request, response);
		return false;
	}
	
}

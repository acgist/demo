package com.acgist.gateway.interceptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.gateway.GatewaySession;
import com.acgist.gateway.config.Gateway;
import com.acgist.gateway.request.GatewayRequest;
import com.acgist.utils.JSONUtils;

/**
 * 数据打包到SessionComponent
 */
@Component
public class PackageInterceptor implements HandlerInterceptor {

	@Autowired
	private ApplicationContext context;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final GatewaySession session = GatewaySession.getInstance(context);
		final Gateway apiType = Gateway.of(request);
		session.setApiType(apiType);
		final String json = json(request);
		if(!json.isEmpty()) {
			final GatewayRequest apiRequest = JSONUtils.jsonToJava(json, apiType.reqeustClass());
			session.setJson(json);
			session.setApiRequest(apiRequest);
		}
		return true;
	}

	private String json(HttpServletRequest request) throws IOException {
		String tmp = null;
		final StringBuffer buffer = new StringBuffer();
		final InputStream input = request.getInputStream();
		final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		while((tmp = reader.readLine()) != null) {
			buffer.append(tmp);
		}
		return buffer.toString();
	}
	
}

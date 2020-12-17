package com.acgist.gateway.interceptor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.gateway.GatewaySession;
import com.acgist.gateway.config.Gateway;
import com.acgist.gateway.config.GatewayCode;
import com.acgist.gateway.request.GatewayRequest;
import com.acgist.gateway.service.GatewayService;
import com.acgist.utils.JSONUtils;

/**
 * <p>数据打包到GatewaySession</p>
 * 
 * @author acgist
 */
@Component
public class PackageInterceptor implements HandlerInterceptor {

	@Autowired
	private ApplicationContext context;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final GatewaySession session = GatewaySession.getInstance(this.context);
		final String json = this.json(request);
		if(StringUtils.isEmpty(json)) {
			session.buildFail(GatewayCode.CODE_1002).response(response);
			return false;
		}
		final Map<String, Object> requestData = JSONUtils.toMap(json);
		final Gateway gateway = Gateway.of((String) requestData.get(GatewayService.GATEWAY));
		if(gateway == null) {
			session.buildFail(GatewayCode.CODE_1000).response(response);
			return false;
		}
		final GatewayRequest gatewayRequest = JSONUtils.unserialize(json, gateway.reqeustClass());
		if(gatewayRequest == null) {
			session.buildFail(GatewayCode.CODE_1002).response(response);
			return false;
		}
		session.setGateway(gateway);
		session.setRequest(gatewayRequest);
		session.setRequestData(requestData);
		return true;
	}

	/**
	 * <p>读取JSON数据</p>
	 * 
	 * @param request 请求
	 * 
	 * @return JSON数据
	 * 
	 * @throws IOException IO异常
	 */
	private String json(HttpServletRequest request) throws IOException {
		return StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
	}
	
}

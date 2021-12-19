package com.api.core.filter.pre;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.api.core.filter.BaseZuulFilter;
import com.api.core.gateway.APICode;
import com.api.core.gateway.APIType;
import com.api.core.gateway.SessionComponent;
import com.api.core.gateway.request.APIRequest;
import com.api.core.service.UniqueNumberService;
import com.api.utils.JSONUtils;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * 请求数据打包
 */
@Component
public class PackageRequestFilter extends BaseZuulFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(PackageRequestFilter.class);
	
	@Autowired
	private UniqueNumberService uniqueNumberService;
	
	@Override
	public Object run() throws ZuulException {
		final RequestContext context = context();
        final HttpServletRequest request = context.getRequest();
		final String queryId = uniqueNumberService.buildId();
		final SessionComponent session = SessionComponent.newInstance(queryId, context);
		final APIType apiType = APIType.valueOfRequest(request);
		if(apiType == null) {
			error(APICode.CODE_1000);
			return null;
		}
		session.setApiType(apiType);
		final String requestJSON = requestJSON(request);
		if(requestJSON == null || requestJSON.isEmpty()) {
			error(APICode.CODE_4400, "请求数据不能为空");
			return null;
		}
		final APIRequest apiRequest = JSONUtils.toJava(requestJSON, apiType.reqeustClazz());
		session.setJson(requestJSON);
		session.setRequest(apiRequest);
		return null;
	}
	
	private String requestJSON(HttpServletRequest request) {
		try {
			return streamToJSON(request.getInputStream());
		} catch (IOException e) {
			LOGGER.error("获取请求报文异常", e);
		}
		return null;
	}
	
	@Override
	public int filterOrder() {
		return 100;
	}
	
	@Override
	public String filterType() {
		return FILTER_TYPE_PRE;
	}

}

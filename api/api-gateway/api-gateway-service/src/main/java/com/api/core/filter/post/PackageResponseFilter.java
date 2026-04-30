package com.api.core.filter.post;

import org.springframework.stereotype.Component;

import com.api.core.filter.BaseZuulFilter;
import com.api.core.gateway.APICode;
import com.api.core.gateway.APIType;
import com.api.core.gateway.SessionComponent;
import com.api.core.gateway.response.APIResponse;
import com.api.utils.JSONUtils;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * 响应数据打包
 */
@Component
public class PackageResponseFilter extends BaseZuulFilter {

	@Override
	public Object run() throws ZuulException {
		final RequestContext context = context();
		final SessionComponent session = sessionComponent();
		final APIType apiType = session.getApiType();
		final Class<APIResponse> clazz = apiType == null ? APIResponse.class : apiType.responseClazz();
		String responseJSON = streamToJSON(context.getResponseDataStream());
		if(responseJSON == null) {
			responseJSON = context.getResponseBody();
		}
		APIResponse apiResponse = JSONUtils.toJava(responseJSON, clazz);
		if(apiResponse == null) {
			apiResponse = APIResponse.builder().valueOfRequest(sessionComponent().getRequest()).buildMessage(APICode.CODE_9999);
		}
		if(apiResponse.fail()) {
			apiResponse.valueOfRequest(session.getRequest());
		}
		session.setResponse(apiResponse);
		return null;
	}

	@Override
	public String filterType() {
		return FILTER_TYPE_POST;
	}

	@Override
	public int filterOrder() {
		return 100;
	}

}

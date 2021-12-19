package com.api.core.filter.pre;

import org.springframework.stereotype.Component;

import com.api.core.filter.BaseZuulFilter;
import com.api.core.gateway.APICode;
import com.api.core.gateway.SessionComponent;
import com.api.core.gateway.request.APIRequest;
import com.netflix.zuul.exception.ZuulException;

/**
 * 数据格式校验
 */
@Component
public class DataVerifyFilter extends BaseZuulFilter {

	@Override
	public boolean shouldFilter() {
		return permissions();
	}
	
	@Override
	public Object run() throws ZuulException {
		final SessionComponent session = sessionComponent();
		final APIRequest apiRequest = session.getRequest();
		final String message = apiRequest.verify();
		if(message == null) {
			return null;
		}
		error(APICode.CODE_3000, message);
		return null;
	}

	@Override
	public int filterOrder() {
		return 120;
	}
	
	@Override
	public String filterType() {
		return FILTER_TYPE_PRE;
	}

}

package com.api.core.filter.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.api.core.filter.BaseZuulFilter;
import com.api.core.gateway.SessionComponent;
import com.api.core.gateway.response.APIResponse;
import com.api.core.service.SignService;
import com.netflix.zuul.exception.ZuulException;

/**
 * 签名
 */
@Component
public class SignFilter extends BaseZuulFilter {

	@Autowired
	private SignService signService;
	
	@Override
	public Object run() throws ZuulException {
		final SessionComponent session = sessionComponent();
		final APIResponse apiResponse = session.getResponse();
		signService.sign(apiResponse);
		return null;
	}

	@Override
	public String filterType() {
		return FILTER_TYPE_POST;
	}

	@Override
	public int filterOrder() {
		return 997;
	}

}

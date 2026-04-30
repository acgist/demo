package com.api.core.filter.post;

import org.springframework.stereotype.Component;

import com.api.core.filter.BaseZuulFilter;
import com.netflix.zuul.exception.ZuulException;

/**
 * 资源销毁
 */
@Component
public class DestroyFilter extends BaseZuulFilter {

	@Override
	public Object run() throws ZuulException {
		sessionComponent().destroy(context());
		return null;
	}

	@Override
	public String filterType() {
		return FILTER_TYPE_POST;
	}

	@Override
	public int filterOrder() {
		return 9999;
	}

}

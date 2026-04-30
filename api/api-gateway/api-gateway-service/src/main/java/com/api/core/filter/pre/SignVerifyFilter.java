package com.api.core.filter.pre;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.api.core.filter.BaseZuulFilter;
import com.api.core.gateway.APICode;
import com.api.core.gateway.SessionComponent;
import com.api.core.service.SignService;
import com.api.utils.JSONUtils;
import com.netflix.zuul.exception.ZuulException;

/**
 * 签名验证，验证请求所有数据，而不是实体数据
 */
@Component
public class SignVerifyFilter extends BaseZuulFilter {

	@Autowired
	private SignService signService;
	
	@Override
	public boolean shouldFilter() {
		return permissions();
	}
	
	@Override
	public Object run() throws ZuulException {
		final SessionComponent session = sessionComponent();
		final String json = session.getJson();
		final Map<String, String> data = JSONUtils.toMap(json)
			.entrySet()
			.stream()
			.map(entry -> {
				return Map.entry(entry.getKey(), (String) entry.getValue());
			})
			.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		if(signService.verify(data)) {
			return null;
		}
		error(APICode.CODE_3001);
		return null;
	}

	@Override
	public int filterOrder() {
		return 110;
	}
	
	@Override
	public String filterType() {
		return FILTER_TYPE_PRE;
	}

}

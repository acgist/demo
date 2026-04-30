package com.api.core.filter.post;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.api.core.filter.BaseZuulFilter;
import com.api.core.gateway.APIType;
import com.api.core.gateway.SessionComponent;
import com.api.core.gateway.response.APIResponse;
import com.api.core.stream.GatewayMessageSender;
import com.api.data.asyn.pojo.entity.GatewayEntity;
import com.netflix.zuul.exception.ZuulException;

/**
 * 网关信息记录
 */
@Component
public class GatewayFilter extends BaseZuulFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayFilter.class);
	
	@Autowired
	private GatewayMessageSender sender;
	
	@Override
	public Object run() throws ZuulException {
		final SessionComponent session = sessionComponent();
		GatewayEntity entity = new GatewayEntity();
		String requestJSON = session.getJson();
		APIResponse response = session.getResponse();
		String responseJSON = response.response();
		final APIType apiType = session.getApiType();
		if(apiType == null) {
			LOGGER.warn("未知的请求接口，queryId：{}", session.getQueryId());
		} else if(apiType.record()) {
			entity.setType(apiType.name());
			entity.setQueryId(session.getQueryId());
			entity.setCode(response.getCode());
			entity.setSend(responseJSON);
			entity.setReceive(requestJSON);
			sender.send(entity);
		} else {
			LOGGER.debug("不需要记录的网关报文，queryId：{}", session.getQueryId());
		}
		return null;
	}

	@Override
	public String filterType() {
		return FILTER_TYPE_POST;
	}

	@Override
	public int filterOrder() {
		return 999;
	}

}

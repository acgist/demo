package com.acgist.gateway.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.acgist.gateway.config.GatewayURL;
import com.acgist.gateway.executor.pay.PayExecutor;
import com.acgist.gateway.executor.pay.QueryExecutor;
import com.acgist.utils.BeanUtils;

@RestController
public class PayController {

	@Autowired
	private ApplicationContext context;

	@RequestMapping(value = GatewayURL.PAY, method = RequestMethod.POST)
	public Map<String, String> pay() {
		return BeanUtils.getInstance(this.context, PayExecutor.class).response();
	}

	@RequestMapping(value = GatewayURL.PAY_QUERY, method = RequestMethod.POST)
	public Map<String, String> query() {
		return BeanUtils.getInstance(this.context, QueryExecutor.class).response();
	}

}

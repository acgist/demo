package com.acgist.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.acgist.api.APIURL;
import com.acgist.api.executor.pay.PayExecutor;
import com.acgist.api.executor.pay.QueryExecutor;
import com.acgist.modules.utils.BeanUtils;

/**
 * 交易
 */
@RestController
public class PayController {

	@Autowired
	private ApplicationContext context;

	@RequestMapping(value = APIURL.PAY, method = RequestMethod.POST)
	public Map<String, String> pay() {
		return BeanUtils.getInstance(context, PayExecutor.class).response();
	}

	@RequestMapping(value = APIURL.PAY_QUERY, method = RequestMethod.POST)
	public Map<String, String> query() {
		return BeanUtils.getInstance(context, QueryExecutor.class).response();
	}

}

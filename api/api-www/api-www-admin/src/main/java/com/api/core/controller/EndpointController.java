package com.api.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.api.core.endpoint.impl.BusRefreshExecutor;
import com.api.core.endpoint.impl.ShutdownExecutor;
import com.api.core.pojo.layui.LayuiMessage;

/**
 * controller - 端点
 */
@Controller
@RequestMapping("/endpoint")
public class EndpointController {

	/**
	 * 刷新
	 */
	@ResponseBody
	@PostMapping("/bus/refresh")
	public LayuiMessage busRefresh(String serviceId, String uri) {
		BusRefreshExecutor executor = new BusRefreshExecutor(uri, serviceId);
		executor.execute();
		return LayuiMessage.buildSuccess();
	}

	/**
	 * 关闭
	 */
	@ResponseBody
	@PostMapping("/shutdown")
	public LayuiMessage shutdown(String uri) {
		ShutdownExecutor executor = new ShutdownExecutor(uri);
		executor.execute();
		return LayuiMessage.buildSuccess();
	}
	
}

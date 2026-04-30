package com.api.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.api.core.order.config.APIConstOrderURL;
import com.api.core.order.pojo.message.OrderMessage;
import com.api.core.order.service.IOrderService;
import com.api.data.order.pojo.entity.OrderEntity;

/**
 * controller - 订单
 */
@Controller
@RequestMapping(APIConstOrderURL.URL_ORDER)
public class OrderController {

	@Autowired
	private IOrderService orderService;
	
	@GetMapping
	public String order() {
		return "/order/index";
	}
	
	@PostMapping
	public String order(@Validated OrderEntity order, ModelMap model) {
		OrderMessage message = orderService.order(order);
		model.put("message", message);
		return "/order/index";
	}
	
}

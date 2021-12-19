package com.api.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.api.core.pojo.layui.LayuiTable;
import com.api.data.order.pojo.entity.OrderEntity;
import com.api.data.pojo.select.Filter;
import com.api.data.pojo.select.PageQuery;
import com.api.data.pojo.select.PageResult;
import com.api.data.repository.OrderRepository;

/**
 * controller - 订单
 */
@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderRepository orderRepository;
	
	@GetMapping("/list")
	public String index() {
		return "/order/list";
	}
	
	@ResponseBody
	@PostMapping("/list")
	public LayuiTable index(int page, int limit, String orderId) {
		PageQuery query = new PageQuery(page, limit);
		query.addFilters(Filter.eq(OrderEntity.PROPERTY_ORDER_ID, orderId));
		PageResult<OrderEntity> pageResult = orderRepository.findPage(query);
		return LayuiTable.build(pageResult.getResult(), pageResult.getTotal());
	}
	
	@GetMapping("/view")
	public String view(String id, ModelMap model) {
		model.addAttribute("entity", orderRepository.findOne(id));
		return "/order/view";
	}
	
}

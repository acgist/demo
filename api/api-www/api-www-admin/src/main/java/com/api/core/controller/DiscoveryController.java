package com.api.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.api.core.pojo.layui.LayuiTable;
import com.api.core.service.DiscoveryService;

/**
 * controller - 服务
 */
@Controller
@RequestMapping("/discovery")
public class DiscoveryController {

	@Autowired
	private DiscoveryService discoveryService;
	
	@GetMapping
	public String index() {
		return "/discovery/index";
	}
	
	@PostMapping
	@ResponseBody
	public LayuiTable index(ModelMap model) {
		return LayuiTable.build(discoveryService.services());
	}
	
	@GetMapping("/{serviceId}")
	public String instancesGet(@PathVariable String serviceId, ModelMap model) {
		model.addAttribute("serviceId", serviceId);
		return "/discovery/instances";
	}
	
	@ResponseBody
	@PostMapping("/{serviceId}")
	public LayuiTable instancesPost(@PathVariable String serviceId, ModelMap model) {
		return LayuiTable.build(discoveryService.instances(serviceId));
	}

}

package com.api.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.api.core.pojo.layui.LayuiMessage;
import com.api.core.service.PermissionService;

/**
 * controller - 缓存
 */
@Controller
@RequestMapping("/cache")
public class CacheController {

	@Autowired
	private PermissionService permissionService;
	
	@ResponseBody
	@GetMapping("/permission/roles")
	public LayuiMessage permissionRoles() {
		permissionService.init();
		return LayuiMessage.buildSuccess();
	}
	
}

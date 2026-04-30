package com.api.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.api.core.gateway.APICode;
import com.api.core.pojo.layui.LayuiMessage;
import com.api.core.pojo.vo.PermissionTree;
import com.api.core.service.PermissionService;
import com.api.data.pojo.entity.PermissionEntity;

/**
 * controller - 系统权限
 */
@Controller
@RequestMapping("/permission")
public class PermissionController {

	@Autowired
	private PermissionService permissionService;
	
	@GetMapping("/tree")
	public String tree() {
		return "/permission/tree";
	}
	
	@ResponseBody
	@PostMapping("/tree")
	public List<PermissionTree> tree(ModelMap model) {
		PermissionTree tree = permissionService.tree();
		return tree.getChildren();
	}
	
	@ResponseBody
	@PostMapping("/submit")
	public LayuiMessage submit(@Validated PermissionEntity entity) {
		final String id = entity.getId();
		entity.setId(null);
		entity.setParent(id);
		permissionService.submit(entity);
		return LayuiMessage.buildSuccess();
	}
	
	@ResponseBody
	@PostMapping("/update")
	public LayuiMessage update(@Validated PermissionEntity entity) {
		permissionService.update(entity);
		return LayuiMessage.buildSuccess();
	}
	
	@ResponseBody
	@PostMapping("/delete")
	public LayuiMessage delete(String id) {
		if(permissionService.delete(id)) {
			return LayuiMessage.buildSuccess();
		}
		return LayuiMessage.build(APICode.CODE_9999.getCode(), "不能删除含有子节点的权限");
	}
	
}

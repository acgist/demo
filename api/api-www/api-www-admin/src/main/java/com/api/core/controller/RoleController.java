package com.api.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.api.core.pojo.layui.LayuiMessage;
import com.api.core.pojo.layui.LayuiTable;
import com.api.core.service.PermissionService;
import com.api.core.service.RoleService;
import com.api.data.pojo.entity.RoleEntity;
import com.api.utils.JSONUtils;

/**
 * controller - 系统角色
 */
@Controller
@RequestMapping("/role")
public class RoleController {

	@Autowired
	private RoleService roleService;
	@Autowired
	private PermissionService permissionService;
	
	@GetMapping("/list")
	public String list() {
		return "/role/list";
	}
	
	@ResponseBody
	@PostMapping("/list")
	public LayuiTable list(ModelMap model) {
		return LayuiTable.build(roleService.findAll());
	}
	
	@GetMapping("/submit")
	public String submit() {
		return "/role/entity";
	}
	
	@ResponseBody
	@PostMapping("/submit")
	public LayuiMessage submit(@Validated RoleEntity entity) {
		roleService.submit(entity);
		return LayuiMessage.buildSuccess();
	}
	
	@GetMapping("/update")
	public String update(String id, ModelMap model) {
		model.addAttribute("entity", roleService.find(id));
		return "/role/entity";
	}
	
	@ResponseBody
	@PostMapping("/update")
	public LayuiMessage update(@Validated RoleEntity entity) {
		roleService.update(entity);
		return LayuiMessage.buildSuccess();
	}
	
	@GetMapping("/permission")
	public String permission(String id, ModelMap model) {
		RoleEntity entity = roleService.find(id);
		model.addAttribute("entity", entity);
		model.addAttribute("permissions", JSONUtils.toJSON(permissionService.tree(entity.getPermissions())));
		return "/role/permission";
	}
	
	@ResponseBody
	@PostMapping("/permission")
	public LayuiMessage permission(String rid, String[] pids) {
		roleService.permission(rid, pids);
		permissionService.initPermissionRoles();
		return LayuiMessage.buildSuccess();
	}
	
	@ResponseBody
	@PostMapping("/delete")
	public LayuiMessage delete(String id) {
		roleService.delete(id);
		return LayuiMessage.buildSuccess();
	}
	
}

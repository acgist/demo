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
import com.api.core.security.AdminDetails;
import com.api.core.service.AdminService;
import com.api.core.service.RoleService;
import com.api.data.pojo.entity.AdminEntity;
import com.api.data.pojo.select.Filter;
import com.api.data.pojo.select.PageQuery;
import com.api.data.pojo.select.PageResult;

/**
 * controller - 系统用户
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private RoleService roleService;
	@Autowired
	private AdminService adminService;
	
	@GetMapping("/info")
	public String info(ModelMap model) {
		model.addAttribute("adminDetails", AdminDetails.current());
		return "/admin/info";
	}

	@GetMapping("/list")
	public String list() {
		return "/admin/list";
	}
	
	@ResponseBody
	@PostMapping("/list")
	public LayuiTable list(int page, int limit, String name) {
		PageQuery query = new PageQuery(page, limit);
		query.addFilters(Filter.eq(AdminEntity.PROPERTY_NAME, name));
		PageResult<AdminEntity> pageResult = adminService.findPage(query);
		return LayuiTable.build(pageResult.getResult(), pageResult.getTotal());
	}

	@GetMapping("/submit")
	public String submit() {
		return "/admin/entity";
	}
	
	@ResponseBody
	@PostMapping("/submit")
	public LayuiMessage submit(@Validated AdminEntity entity) {
		adminService.submit(entity);
		return LayuiMessage.buildSuccess();
	}
	
	@GetMapping("/update")
	public String update(String id, ModelMap model) {
		model.addAttribute("entity", adminService.find(id));
		return "/admin/entity";
	}
	
	@ResponseBody
	@PostMapping("/update")
	public LayuiMessage update(@Validated AdminEntity entity) {
		adminService.update(entity);
		return LayuiMessage.buildSuccess();
	}
	
	@GetMapping("/role")
	public String role(String id, ModelMap model) {
		model.addAttribute("roles", roleService.findAll());
		model.addAttribute("entity", adminService.find(id));
		return "/admin/role";
	}
	
	@ResponseBody
	@PostMapping("/role")
	public LayuiMessage role(String aid, String[] rids) {
		adminService.role(aid, rids);
		return LayuiMessage.buildSuccess();
	}
	
	@ResponseBody
	@PostMapping("/delete")
	public LayuiMessage delete(String id) {
		adminService.delete(id);
		return LayuiMessage.buildSuccess();
	}
	
}

package com.api.core.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.api.core.pojo.layui.LayuiMessage;
import com.api.core.pojo.layui.LayuiTable;
import com.api.core.service.UserService;
import com.api.data.pojo.select.Filter;
import com.api.data.pojo.select.PageQuery;
import com.api.data.pojo.select.PageResult;
import com.api.data.user.pojo.entity.UserEntity;

/**
 * controller - 用户
 */
@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/list")
	public String list() {
		return "/user/list";
	}
	
	@ResponseBody
	@PostMapping("/list")
	public LayuiTable list(int page, int limit, String name) {
		PageQuery query = new PageQuery(page, limit);
		query.addFilters(Filter.eq(UserEntity.PROPERTY_NAME, name));
		PageResult<UserEntity> pageResult = userService.findPage(query);
		return LayuiTable.build(pageResult.getResult(), pageResult.getTotal());
	}
	

	@GetMapping("/submit")
	public String submit() {
		return "/user/entity";
	}
	
	@ResponseBody
	@PostMapping("/submit")
	public LayuiMessage submit(@Validated UserEntity entity) {
		userService.submit(entity);
		return LayuiMessage.buildSuccess();
	}
	
	@GetMapping("/update")
	public String update(String id, ModelMap model) {
		model.addAttribute("entity", userService.find(id));
		return "/user/entity";
	}
	
	@ResponseBody
	@PostMapping("/update")
	public LayuiMessage update(@Validated UserEntity entity) {
		userService.update(entity);
		return LayuiMessage.buildSuccess();
	}
	
	@ResponseBody
	@PostMapping("/cert")
	public Map<String, String> cert() {
		return userService.cert();
	}
	
	@GetMapping("/cert/download")
	public void download(String id, HttpServletResponse response) throws IOException {
		UserEntity entity = userService.find(id);
		if(entity == null) {
			return;
		}
		response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
		response.setHeader("Content-Disposition", "attachment;filename=" + entity.getName() + ".txt");
		PrintWriter writer = response.getWriter();
		writer.write("PublicKey（公钥）：");
		writer.write(entity.getPublicKey());
		writer.write("\r");
		writer.write("PrivateKey（私钥）：");
		writer.write(entity.getPrivateKey());
		writer.flush();
		writer.close();
	}
	
	@ResponseBody
	@PostMapping("/delete")
	public LayuiMessage delete(String id) {
		userService.delete(id);
		return LayuiMessage.buildSuccess();
	}
	
}

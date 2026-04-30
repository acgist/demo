package com.api.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.api.core.config.APIConstAdminURL;
import com.api.core.security.AdminDetails;

/**
 * controller - 首页
 */
@Controller
public class IndexController {

	@GetMapping(APIConstAdminURL.URL_ADMIN_INDEX)
	public String index(ModelMap model) {
		model.addAttribute("adminDetails", AdminDetails.current());
		return "/index";
	}

}

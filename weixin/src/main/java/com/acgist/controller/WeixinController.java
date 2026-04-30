package com.acgist.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WeixinController {

	@GetMapping("/weixin")
	public String weixin(HttpServletRequest request, HttpServletResponse response) {
	    String userAgent = request.getHeader("User-Agent");
	    if(userAgent.contains("MQQBrowser")) { // 判断微信浏览器返回以下响应头和状态码
	        response.addHeader("Content-Type", "text/plain; charset=utf-8");
	        response.addHeader("Content-Disposition", "attachment;filename=open.apk");
	        response.setStatus(HttpStatus.PARTIAL_CONTENT.value()); // 设置状态码206
	        return null;
	    }
		return "/weixin.html";
	}
	
}

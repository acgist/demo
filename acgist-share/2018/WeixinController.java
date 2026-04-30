package com.acgist.controller.demo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("DemoWeixinController")
@RequestMapping("/demo/weixin")
public class WeixinController {

	/**
	 * 微信自动跳转默认浏览器
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(HttpServletRequest request, HttpServletResponse response) {
	    String userAgent = request.getHeader("User-Agent");
	    if(userAgent != null && userAgent.contains("MQQBrowser")) { // 判断微信浏览器返回以下响应头和状态码
	        response.addHeader("Content-Type", "text/plain; charset=utf-8");
	        response.addHeader("Content-Disposition", "attachment;filename=open.apk");
	        response.setStatus(HttpStatus.SC_PARTIAL_CONTENT); // 设置状态码206
	        return null;
	    }
		return "redirect:/demo/weixin/view.html";
	}
	
}

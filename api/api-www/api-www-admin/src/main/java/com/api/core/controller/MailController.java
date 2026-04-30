package com.api.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.api.core.pojo.layui.LayuiMessage;
import com.api.core.stream.MailMessageSender;

/**
 * controller - 邮件
 */
@Controller
@RequestMapping("/mail")
public class MailController {
	
	@Autowired
	private MailMessageSender mailMessageSender;
	
	@ResponseBody
	@GetMapping("/test")
	public LayuiMessage test(String to) {
		final String subject = "邮件主题：测试";
		final String content = "邮件内容：测试邮件";
		mailMessageSender.send(to, subject, content);
		return LayuiMessage.buildSuccess();
	}
	
}

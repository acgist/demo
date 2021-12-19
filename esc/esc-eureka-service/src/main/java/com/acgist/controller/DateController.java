package com.acgist.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DateController {

	@Value("${system.name:}")
	private String name;
	
	@RequestMapping(value = "/date", method = RequestMethod.GET)
	public String date() {
		final String name = StringUtils.isEmpty(this.name) ? "当前只有一个服务提供者" : this.name;
		return name + "，当前时间：" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}
	
}

package com.acgist.fund.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acgist.fund.pojo.vo.Layui;
import com.acgist.fund.service.FundService;

@RestController
@RequestMapping("/fund")
public class FundController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FundController.class);

	@Autowired
	private FundService fundService;
	
	/**
	 * <p>列出自选基金</p>
	 */
	@GetMapping("/list")
	public Layui list() {
		return Layui.success(this.fundService.list());
	}

	/**
	 * <p>更新所有基金</p>
	 * <p>删除全部更新编号列表</p>
	 * 
	 * @param code 编号
	 */
	@GetMapping("/update")
	public void update(String code) {
		if(StringUtils.isEmpty(code)) {
			this.fundService.update();
		} else {
			final String[] codes = code.split(",");
			for (String value : codes) {
				this.fundService.update(value);
			}
		}
	}
	
	/**
	 * <p>查看基金</p>
	 * 
	 * @param code 基金编号
	 */
	@GetMapping("/link/{code}")
	public void link(@PathVariable String code, HttpServletResponse response) {
		try {
			response.sendRedirect(this.fundService.link(code));
		} catch (IOException e) {
			LOGGER.error("查看基金异常", e);
		}
	}
	
}

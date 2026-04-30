package com.acgist.rt.alarm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.acgist.admin.data.alarm.entity.Alarm;
import com.acgist.rt.alarm.service.AlarmService;

/**
 * 告警
 * 
 * @author acgist
 */
@RestController
@RequestMapping("/rt/alarm")
public class AlarmController {
	
	@Autowired
	private AlarmService alarmService;

	@GetMapping("/{ruleId}")
	public Page<Alarm> page(@PathVariable Long ruleId, Page<Alarm> page) {
		return this.alarmService.page(ruleId, page);
	}
	
	@GetMapping("/reload")
	public Boolean reload() {
		return this.alarmService.reload();
	}
	
}

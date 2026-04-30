package com.acgist.rt.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.acgist.rt.alarm.service.AlarmService;

/**
 * 告警配置
 * 
 * @author acgist
 */
@Configuration
public class AlarmConfig {

	@Autowired
	private AlarmService alarmService;

	@PostConstruct
	public void init() {
		// 加载告警规则和告警动作
		this.alarmService.reload();
	}
	
}

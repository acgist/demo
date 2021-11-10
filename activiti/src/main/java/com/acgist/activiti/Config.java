package com.acgist.activiti;

import javax.annotation.PostConstruct;

import org.activiti.engine.ProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

	@Autowired
	private ProcessEngineConfiguration processEngineConfiguration;
	
	@PostConstruct
	public void init() {
		// 修改字体解决中文乱码
		this.processEngineConfiguration.setLabelFontName("宋体");
		this.processEngineConfiguration.setActivityFontName("宋体");
		this.processEngineConfiguration.setAnnotationFontName("宋体");
	}

}

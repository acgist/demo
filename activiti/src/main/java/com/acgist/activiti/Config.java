package com.acgist.activiti;

import javax.annotation.PostConstruct;

import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.cfg.IdGenerator;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

	@Autowired
	private ProcessEngineConfiguration processEngineConfiguration;
	
	@PostConstruct
	public void init() {
		this.reset(this.processEngineConfiguration);
	}
	
//	@Bean
//	public ProcessEngineConfiguration processEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration) {
//		this.reset(processEngineConfiguration);
//		return processEngineConfiguration;
//	}
	
	private void reset(ProcessEngineConfiguration processEngineConfiguration) {
		// 修改字体解决中文乱码
		processEngineConfiguration.setLabelFontName("宋体");
		processEngineConfiguration.setActivityFontName("宋体");
		processEngineConfiguration.setAnnotationFontName("宋体");
		// 修改ID暂时没有发现最有解决方案
		// 以下代码不建议使用没有经过测试
		if(processEngineConfiguration instanceof SpringProcessEngineConfiguration) {
			final IdGenerator idGenerator = new IdGenerator() {
				@Override
				public String getNextId() {
					// TODO：自行实现
					return System.nanoTime() + "";
				}
			};
			final SpringProcessEngineConfiguration springProcessEngineConfiguration = (SpringProcessEngineConfiguration) processEngineConfiguration;
			springProcessEngineConfiguration.setIdGenerator(idGenerator);
			springProcessEngineConfiguration.getBpmnDeployer().setIdGenerator(idGenerator);
			springProcessEngineConfiguration.getDbSqlSessionFactory().setIdGenerator(idGenerator);
		}
	}

}

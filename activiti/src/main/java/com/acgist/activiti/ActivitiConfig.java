package com.acgist.activiti;

import java.io.IOException;

import javax.sql.DataSource;

import org.activiti.spring.SpringAsyncExecutor;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ActivitiConfig extends AbstractProcessEngineAutoConfiguration {

	@Autowired
	private DataSource dataSource;
	
//	@Bean
//	public DataSource database() {
//	    return DataSourceBuilder.create()
//	        .url("jdbc:mysql://127.0.0.1:3306/activiti-spring-boot?characterEncoding=UTF-8")
//	        .username("alfresco")
//	        .password("alfresco")
//	        .driverClassName("com.mysql.jdbc.Driver")
//	        .build();
//	}

	@Bean
	public SpringProcessEngineConfiguration springProcessEngineConfiguration(
		PlatformTransactionManager transactionManager, SpringAsyncExecutor springAsyncExecutor
	) throws IOException {
		return baseSpringProcessEngineConfiguration(this.dataSource, transactionManager, springAsyncExecutor);
	}
}
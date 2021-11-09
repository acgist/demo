package com.acgist.activiti;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@ComponentScan({"org.activiti.rest.editor", "com.acgist.activiti"})
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ActivitiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActivitiApplication.class, args);
	}

	@Configurable
	@EnableWebSecurity
	public static class AdminConfig extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
				.csrf().disable()
				.antMatcher("/**").authorizeRequests().anyRequest().permitAll();
		}
	}
	
}

package com.api.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.api.data.repository.BaseExtendRepositoryImpl;

@EntityScan("com.api.data.**.entity")
@EnableBinding
@ComponentScan({"com.api.core", "com.api.data"})
@EnableEurekaClient
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.api.data.**.repository", repositoryBaseClass = BaseExtendRepositoryImpl.class)
@EnableTransactionManagement
public class ApiServiceAsynApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiServiceAsynApplication.class, args);
	}

}

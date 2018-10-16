package com.acgist.main;

import javax.transaction.TransactionManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableCaching
@EnableSwagger2
@SpringBootApplication
@EnableTransactionManagement
@EntityScan("com.acgist.entity")
@ComponentScan(basePackages = { "com.acgist" })
@EnableJpaRepositories("com.acgist.dao")
public class AcgistFeelApplication {

	public static void main(String[] args) throws ClassNotFoundException {
		Class.forName(TransactionManager.class.getName());
		SpringApplication.run(AcgistFeelApplication.class, args);
	}

}

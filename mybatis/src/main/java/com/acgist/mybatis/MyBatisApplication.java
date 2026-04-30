package com.acgist.mybatis;

import java.util.UUID;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@MapperScan("com.acgist")
@ComponentScan("com.acgist")
@SpringBootApplication
public class MyBatisApplication {

	public static void main(String[] args) {
		MDC.clear();
		MDC.put("log-id", UUID.randomUUID().toString());
		SpringApplication.run(MyBatisApplication.class, args);
	}

}

package com.acgist.logback;

import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.acgist")
@SpringBootApplication
public class LogbackApplication {

	public static void main(String[] args) {
		MDC.clear();
		MDC.put("log-id", UUID.randomUUID().toString());
		SpringApplication.run(LogbackApplication.class, args);
	}

}

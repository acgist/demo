package com.acgist.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.acgist" })
public class AcgistConanApplication {

	public static void main(String[] args) {
		SpringApplication.run(AcgistConanApplication.class, args);
	}

}

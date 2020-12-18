package com.acgist.fund;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.acgist.fund")
@SpringBootApplication
public class FundApplication {

	public static void main(String[] args) {
		SpringApplication.run(FundApplication.class, args);
	}

}

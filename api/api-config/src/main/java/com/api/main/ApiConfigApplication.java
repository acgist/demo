package com.api.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class ApiConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiConfigApplication.class, args);
	}
	
}
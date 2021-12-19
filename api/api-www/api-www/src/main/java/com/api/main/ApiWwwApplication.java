package com.api.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableHystrix
@EnableBinding
@ComponentScan({"com.api.core", "com.api.feign"})
@EnableEurekaClient
@EnableFeignClients("com.api.feign")
@SpringBootApplication
@EnableRedisHttpSession
public class ApiWwwApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiWwwApplication.class, args);
	}

}

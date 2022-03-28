package com.acgist.rt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.acgist.rt.config.MessageConsumer;

import lombok.SneakyThrows;

@EntityScan(basePackages = "com.acgist")
@MapperScan(basePackages = "com.acgist.**.mapper")
@EnableRetry
@EnableCaching
@EnableBinding(MessageConsumer.class)
@ComponentScan(basePackages = "com.acgist")
@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaRepositories(basePackages = "com.acgist.**.repository")
@EnableAspectJAutoProxy(exposeProxy = true)
public class RtApplication {

	public static void main(String[] args) {
		SpringApplication.run(RtApplication.class, args);
	}

	@Bean
	@Primary
	@LoadBalanced
	public RestTemplate lbRestTemplate() {
		final RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
			@Override
			@SneakyThrows
			public void handleError(ClientHttpResponse response) {
				super.handleError(response);
			}
		});
		return restTemplate;
	}

}

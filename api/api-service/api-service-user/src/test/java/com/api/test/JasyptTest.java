//package com.api.test;
//
//import org.jasypt.encryption.StringEncryptor;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import com.api.main.ApiServiceUserApplication;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = ApiServiceUserApplication.class)
//public class JasyptTest {
//
//	@Value("${jasypt.encryptor.password:}")
//	private String jasypt;
//	@Value("${spring.rabbitmq.password:}")
//	private String rabbitmq;
//	@Value("${spring.cloud.stream.binders.rabbit.environment.spring.rabbitmq.password:}")
//	private String rabbitmqStream;
//	@Autowired
//	private StringEncryptor stringEncryptor;
//
//	@Test
//	public void encoding() {
//		System.out.println(jasypt);
//		System.out.println(rabbitmq);
//		System.out.println(rabbitmqStream);
//		System.out.println(stringEncryptor.encrypt("guest"));
//	}
//
//}

package com.api.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.api.core.user.pojo.message.AuthoMessage;
import com.api.main.ApiGatewayServiceApplication;
import com.api.ribbon.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiGatewayServiceApplication.class)
public class UserServiceTest {

	@Autowired
	private UserService userService;
	
	@Test
	public void test() {
		AuthoMessage message = userService.autho("test");
		System.out.println(message.getName());
		System.out.println(message.getPubilcKey());
	}
	
}

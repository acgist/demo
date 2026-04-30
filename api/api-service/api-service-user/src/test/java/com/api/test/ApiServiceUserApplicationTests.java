package com.api.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.api.data.user.pojo.entity.UserEntity;
import com.api.data.user.repository.UserRepository;
import com.api.main.ApiServiceUserApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiServiceUserApplication.class)
public class ApiServiceUserApplicationTests {

	@Autowired
	private UserRepository userRepository;
	
	@Test
	public void contextLoads() {
	}

	@Test
	public void findName() {
		UserEntity userEntity = userRepository.findByName("test");
		System.out.println(userEntity.getId());
	}
	
}

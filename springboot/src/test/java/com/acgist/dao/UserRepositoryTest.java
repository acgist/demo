package com.acgist.dao;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import com.acgist.entity.UserEntity;
import com.acgist.springboot.SpringbootApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootApplication.class)
public class UserRepositoryTest {
	
	@Autowired
	private UserRepository userRepository;

	@Test
	public void testJpa() {
		UserEntity userEntity = new UserEntity();
		userEntity.setName("测试");
		this.userRepository.saveAndFlush(userEntity);
		UserEntity search = new UserEntity();
		search.setName("测试");
		this.userRepository.findAll(Example.of(search), PageRequest.of(0, 2)).forEach(user -> {
			System.out.println(user.getId());
			System.out.println(user.getName());
		});
		assertTrue(this.userRepository.count() > 0);
//		assertNotNull(this.userRepository.findByName("测试"));
	}
	
}

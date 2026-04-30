package com.acgist.sharding;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserTest {

	@Autowired
	private UserRepository userRepository;
	
	@Test
	public void testInsert() {
		UserEntity entity = new UserEntity();
		entity.setId(1L);
		entity.setName("acgist");
		this.userRepository.save(entity);
	}
	
	@Test
	public void testUpdate() {
		UserEntity entity = new UserEntity();
		entity.setId(1L);
		entity.setName("update");
		this.userRepository.saveAndFlush(entity);
	}

	@Test
	public void testDelete() {
		UserEntity entity = new UserEntity();
		entity.setId(1L);
		this.userRepository.delete(entity);
	}
	
	@Test
	public void testFindAll() {
		this.userRepository.findAll().forEach(user -> System.out.println(user.getName()));
	}
	
}

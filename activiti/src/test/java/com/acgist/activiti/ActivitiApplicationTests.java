package com.acgist.activiti;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class ActivitiApplicationTests {

	private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiApplicationTests.class);
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	void contextLoads() {
	}

	public static class User {
		private String name;
		private Integer age;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getAge() {
			return age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}
	}
	
	@Test
	void testObjectMapper() throws JsonProcessingException {
		final User user = new User();
		user.setName("acgist");
		final String json = this.objectMapper.writeValueAsString(user);
		final User jsonUser = this.objectMapper.readValue(json, User.class);
		LOGGER.info("数据：{}", json);
		LOGGER.info("数据：{}", jsonUser.getAge());
		LOGGER.info("数据：{}", jsonUser.getName());
		LOGGER.info("数据：{}", this.objectMapper.readTree(json));
		LOGGER.info("数据：{}", this.objectMapper.createObjectNode());
	}

}

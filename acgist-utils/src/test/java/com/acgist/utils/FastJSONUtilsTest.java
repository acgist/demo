package com.acgist.utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.acgist.pojo.User;

public class FastJSONUtilsTest extends Performance {

	@Test
	public void testSerialize() {
		final User user = new User();
		user.setAge(12);
		user.setName("acgist");
		this.log(FastJSONUtils.serialize(user));
		assertNotNull(FastJSONUtils.serialize(user));
	}
	
	@Test
	public void testUnserialize() {
		final String json = "{\"age\":12,\"name\":\"acgist\"}";
		final User user = FastJSONUtils.unserialize(json, User.class);
		this.log(user.getAge());
		this.log(user.getName());
		assertNotNull(user);
	}
	
	@Test
	public void testToMap() {
		final String json = "{\"age\":12,\"name\":\"acgist\"}";
		final Map<String, Object> map = FastJSONUtils.toMap(json);
		this.log(map);
		assertNotNull(map);
	}

	@Test
	public void testToList() {
		final String json = "[1, 2, 3]";
		final List<Integer> list = FastJSONUtils.toList(json);
		this.log(list);
		assertNotNull(list);
	}
	
}

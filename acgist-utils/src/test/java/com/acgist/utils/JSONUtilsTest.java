package com.acgist.utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.acgist.pojo.User;

public class JSONUtilsTest extends Performance {

	@Test
	public void testSerialize() {
		final User user = new User();
		user.setAge(12);
		user.setName("acgist");
		this.log(JSONUtils.serialize(user));
		assertNotNull(FastJSONUtils.serialize(user));
	}
	
	@Test
	public void testUnserialize() {
		final String json = "{\"age\":12,\"name\":\"acgist\"}";
		final User user = JSONUtils.unserialize(json, User.class);
		this.log(user.getAge());
		this.log(user.getName());
		assertNotNull(user);
	}
	
	@Test
	public void testToMap() {
		final String json = "{\"age\":12,\"name\":\"acgist\"}";
		final Map<String, Object> map = JSONUtils.toMap(json);
		this.log(map);
		assertNotNull(map);
	}

	@Test
	public void testToList() {
		final String json = "[1, 2, 3]";
		final List<Integer> list = JSONUtils.toList(json);
		this.log(list);
		assertNotNull(list);
	}
	
	@Test
	public void testCosted() {
		final String json = "[1, 2, 3]";
		this.costed(100000, () -> JSONUtils.toList(json));
//		this.costed(100000, () -> JSONUtils.toList(json, Integer.class));
	}
	
}

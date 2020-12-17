package com.acgist.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.acgist.pojo.User;

public class BeanUtilsTest extends Performance {

	@Test
	public void testNewInstance() {
		final User user = BeanUtils.newInstance(User.class);
		assertNotNull(user);
	}
	
	@Test
	public void testToMap() {
		final User user = new User(12, "acgist");
		final Map<String, Object> map = BeanUtils.toMap(user);
		this.log(map);
		assertEquals(12, map.get("age"));
		assertEquals("acgist", map.get("name"));
	}
	
	@Test
	public void testGetProperties() {
		final String[] properties = BeanUtils.getProperties(User.class);
		this.log(properties);
		assertNotNull(properties);
		final User user = new User(12, "acgist");
		final Object value = BeanUtils.getProperties(user, "age");
		this.log(value);
		assertNotNull(value);
		final Object[] values = BeanUtils.getProperties(user, "age", "name");
		this.log(values);
		assertNotNull(values);
	}
	
	@Test
	public void testSetProperties() {
		final Map<String, Object> data = new HashMap<String, Object>();
		data.put("age", 12);
		data.put("name", "acgist");
		final User user = new User();
		BeanUtils.setProperties(user, data);
		assertEquals(12, user.getAge());
		assertEquals("acgist", user.getName());
	}
	
}

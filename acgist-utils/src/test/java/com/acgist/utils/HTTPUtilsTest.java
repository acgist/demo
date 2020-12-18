package com.acgist.utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class HTTPUtilsTest extends Performance {

	@Test
	public void testGet() {
		final Map<String, Object> params = new HashMap<>();
		params.put("name", "acgist");
		final String body = HTTPUtils.get("https://www.acgist.com", params);
		this.log(body);
		assertNotNull(body);
	}

	@Test
	public void testPost() {
		final String body = HTTPUtils.post("https://www.acgist.com", "{}");
		this.log(body);
		assertNotNull(body);
	}
	
	@Test
	public void testReuse() {
		this.log(HTTPUtils.get("https://www.acgist.com"));
		this.log(HTTPUtils.get("https://www.acgist.com"));
		this.log(HTTPUtils.get("https://www.baidu.com"));
		this.log(HTTPUtils.get("https://www.baidu.com"));
	}
	
}

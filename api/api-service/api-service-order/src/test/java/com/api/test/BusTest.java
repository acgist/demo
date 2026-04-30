package com.api.test;

import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class BusTest {

	@Test
	public void refresh() {
		RestTemplate rest = new RestTemplate();
		ResponseEntity<String> response = rest.postForEntity("http://localhost:32010/actuator/bus-refresh", "{}", String.class);
		System.out.println(response.getBody());
	}
	
}

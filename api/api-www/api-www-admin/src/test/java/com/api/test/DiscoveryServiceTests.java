package com.api.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.api.core.service.DiscoveryService;
import com.api.main.ApiWwwAdminApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiWwwAdminApplication.class)
public class DiscoveryServiceTests {

	@Autowired
	private DiscoveryService discoveryService;
	
	@Test
	public void list() {
		discoveryService.services();
	}

}

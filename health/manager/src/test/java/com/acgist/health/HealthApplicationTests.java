package com.acgist.health;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.acgist.health.HealthApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes = HealthApplication.class)
class HealthApplicationTests {

	@Test
	void contextLoads() throws InterruptedException {
	}

}

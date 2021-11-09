package com.acgist.sharding;

import org.junit.jupiter.api.Test;

public class SnowflakeTest {

	@Test
	public void test() {
		SnowflakeBuilder builder = new SnowflakeBuilder();
		for (int i = 0; i < 9000; i++) {
			System.out.println(builder.buildId());
		}
	}
	
}

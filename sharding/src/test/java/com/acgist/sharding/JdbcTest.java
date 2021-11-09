package com.acgist.sharding;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
public class JdbcTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Test
	public void test() {
		// 不能使用ID
//		final String sql = "insert into tb_order (id, name) values(", ?)";
		final String sql = "insert into tb_order (name) values(?)";
		final Object[] args = new Object[] { "acgist" };
		for (int i = 0; i < 10; i++) {
			this.jdbcTemplate.update(sql, args);
		}
	}

}

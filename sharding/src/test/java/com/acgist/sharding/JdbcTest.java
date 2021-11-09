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
		final String sql = "insert into tb_order (id, name) values(?, ?)";
		Long id = 1L;
		final Object[] args = new Object[] { id, "acgist" };
		this.jdbcTemplate.update(sql, args);
	}

}

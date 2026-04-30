package com.acgist.duckdb;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.acgist.duckdb.dao.mapper.UserMapper;
import com.acgist.duckdb.data.entity.User;
import com.acgist.duckdb.service.UserService;

@SpringBootTest
class DuckdbApplicationTests {
    
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;

	@Test
	void testDao() {
	    this.userMapper.deleteById(1L);
	    this.userMapper.deleteById(2L);
	    this.userMapper.insert(new User(1L, "acgist"));
	    this.userMapper.insert(new User(2L, "acgist"));
	    final List<User> list = this.userMapper.selectAll();
	    System.out.println(list);
	    assertNotNull(list);
	}
	
	@Test
	void testService() {
	    this.userService.rel();
	    this.userService.user();
	}

}

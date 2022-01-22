package com.acgist.mybatis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@SpringBootTest(classes = MyBatisApplication.class)
class MyBatisApplicationTests {

	@Autowired
	private DemoMapper demoMapper;
	
	@Test
	void testList() {
		assertEquals(2, this.demoMapper.list().size());
	}
	
	@Test
	void testPage() {
		final Page<Demo> page = this.demoMapper.page(PageRequest.of(0, 1));
		assertEquals(2, page.getTotalPages());
		assertEquals(1, page.getTotalElements());
	}
	
	@Test
	void testPageList() {
		final Page<Demo> page = this.demoMapper.pageList(PageRequest.of(0, 1));
		assertEquals(2, page.getTotalPages());
		assertEquals(1, page.getNumberOfElements());
	}

}

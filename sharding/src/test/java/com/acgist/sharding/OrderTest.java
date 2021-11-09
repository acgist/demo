package com.acgist.sharding;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
public class OrderTest {

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private SnowflakeBuilder snowflakeBuilder;
	
	@Test
	public void testInsert() {
		OrderEntity entity = new OrderEntity();
		// hibernate自动检测ID不能为空
		entity.setId(this.snowflakeBuilder.buildId());
		entity.setName("acgist");
		this.orderRepository.save(entity);
	}
	
	@Test
	public void testInsertBatch() {
		for (int i = 0; i < 1000; i++) {
			this.testInsert();
		}
	}
	
	@Test
	public void testUpdate() {
		// 注意：如果测试没有实现主备更新失败
		OrderEntity entity = new OrderEntity();
		entity.setId(2L);
		entity.setName("update");
		this.orderRepository.saveAndFlush(entity);
	}
	
	@Test
	public void testDelete() {
		OrderEntity entity = new OrderEntity();
		entity.setId(2L);
		this.orderRepository.delete(entity);
	}
	
	@Test
	public void testFind() {
		this.orderRepository.findById(1L);
	}
	
	@Test
	public void findPage() {
		this.orderRepository.findAll(PageRequest.of(1, 10));
	}
	
	@Test
	public void testFindAll() {
		this.orderRepository.findAll();
	}
	
}

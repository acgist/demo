package com.acgist.sharding;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

	@Modifying
	@Transactional
	// 顺序
//	@Query(value = "insert into tb_order (name) values(?1)", nativeQuery = true)
	// 简单对象
//	@Query(value = "insert into tb_order (name) values(:name)", nativeQuery = true)
	// 复杂对象
	@Query(value = "insert into tb_order (name) values(:#{#entity.name})", nativeQuery = true)
	void insert(OrderEntity entity);
	
}

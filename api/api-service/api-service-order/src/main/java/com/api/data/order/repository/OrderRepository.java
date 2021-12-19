package com.api.data.order.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.api.data.order.pojo.entity.OrderEntity;
import com.api.data.repository.BaseExtendRepository;

/**
 * repository - 订单
 */
@Repository
public interface OrderRepository extends BaseExtendRepository<OrderEntity> {

	/**
	 * 根据订单ID查询订单实体
	 * @param orderId 订单ID
	 * @return 订单实体
	 */
	@Query(value = "SELECT * FROM tb_order model WHERE model.order_id = :orderId LIMIT 1", nativeQuery = true)
	OrderEntity findByOrderId(String orderId);
	
}

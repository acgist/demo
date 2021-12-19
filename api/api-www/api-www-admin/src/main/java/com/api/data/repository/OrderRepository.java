package com.api.data.repository;

import org.springframework.stereotype.Repository;

import com.api.data.order.pojo.entity.OrderEntity;

/**
 * repository - 订单
 */
@Repository
public interface OrderRepository extends BaseExtendRepository<OrderEntity> {

}

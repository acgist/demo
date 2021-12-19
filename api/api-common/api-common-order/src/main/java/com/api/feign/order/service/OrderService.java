package com.api.feign.order.service;

import org.springframework.cloud.openfeign.FeignClient;

import com.api.core.config.APIConstApplication;
import com.api.core.order.service.IOrderService;
import com.api.feign.config.FeignConfig;
import com.api.feign.order.fallback.OrderServiceFallback;

/**
 * 服务调用 - 订单
 */
@FeignClient(name = APIConstApplication.API_SERVICE_ORDER, configuration = FeignConfig.class, fallback = OrderServiceFallback.class)
public interface OrderService extends IOrderService {

}

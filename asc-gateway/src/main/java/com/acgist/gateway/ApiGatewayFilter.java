//package com.acgist.gateway;
//
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.core.Ordered;
//import org.springframework.web.server.ServerWebExchange;
//
//import reactor.core.publisher.Mono;
//
//public class ApiGatewayFilter implements GatewayFilter, Ordered {
//
//	@Bean
//	public RouteLocator customerRouteLocator(RouteLocatorBuilder builder) {
//		return builder.routes()
//			.route("id", route -> route
//			.path("/customer/**")
//			.filters(f -> f.filter(new ApiGatewayFilter())
//			.addResponseHeader("X-Response-test", "acgist"))
//			.uri("lb://customer")
//		).build();
//	}
//
//	@Override
//	public int getOrder() {
//		return 0;
//	}
//
//	@Override
//	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//		return null;
//	}
//
//}

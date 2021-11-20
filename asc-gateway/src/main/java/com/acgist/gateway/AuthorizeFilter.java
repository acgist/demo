//package com.acgist.gateway;
//
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.server.ServerWebExchange;
//
//import reactor.core.publisher.Mono;
//
//public class AuthorizeFilter implements GlobalFilter, Ordered {
//
//	@Override
//	public int getOrder() {
//		return 0;
//	}
//
//	@Override
//	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//		exchange.getRequest().getPath();
//		if(exchange.getRequest().getQueryParams().containsKey("acgist")) {
//			exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
//			return exchange.getResponse().setComplete();
//		}
//		return chain.filter(exchange);
//	}
//
//}

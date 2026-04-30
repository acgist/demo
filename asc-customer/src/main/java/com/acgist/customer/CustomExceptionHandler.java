package com.acgist.customer;

import org.apache.dubbo.rpc.RpcException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;


@RestControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(RpcException.class)
	public String rpcException(RpcException e) {
		return "rpcException：" + e;
	}
	
	@ExceptionHandler(BlockException.class)
	public String blockException(BlockException e) {
		return "blockException：" + e;
	}
	
	@ExceptionHandler(FlowException.class)
	public String flowException(FlowException e) {
		return "flowException：" + e;
	}
	
	@ExceptionHandler(Exception.class)
	public String allException(Exception e) {
		return "allException：" + e;
	}
	
}

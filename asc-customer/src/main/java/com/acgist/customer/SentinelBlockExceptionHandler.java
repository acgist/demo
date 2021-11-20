package com.acgist.customer;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;

@Configuration
public class SentinelBlockExceptionHandler implements BlockExceptionHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
		response.setStatus(429);
		final PrintWriter out = response.getWriter();
		out.print(e == null ? "系统限流" : e.getMessage());
		out.flush();
		out.close();
	}

}
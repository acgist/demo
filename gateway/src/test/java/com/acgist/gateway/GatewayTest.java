package com.acgist.gateway;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.gateway.service.GatewayService;
import com.acgist.gateway.service.SignatureService;
import com.acgist.utils.DateUtils;
import com.acgist.utils.HTTPUtils;
import com.acgist.utils.JSONUtils;

public class GatewayTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayTest.class);

	private static SignatureService service;
	
	@BeforeClass
	public static void init() {
		service = new SignatureService(
			"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC8N09VZ/ARi+dBbxQq84ynf162QF5P71B/0DY54Lo1XSQXzcbCa6nrySbsLsJJ8FmVtBUWmomxQs0Gf37QTjN8cMVp1ziTXMr4zOapV3WYlG4XSf9kxnIgOD1OyjcjPaglBucJNf90Gyswnqc+3RF4X7Andb21SPpO+bHlsF/+tQIDAQAB",
			"MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBALw3T1Vn8BGL50FvFCrzjKd/XrZAXk/vUH/QNjngujVdJBfNxsJrqevJJuwuwknwWZW0FRaaibFCzQZ/ftBOM3xwxWnXOJNcyvjM5qlXdZiUbhdJ/2TGciA4PU7KNyM9qCUG5wk1/3QbKzCepz7dEXhfsCd1vbVI+k75seWwX/61AgMBAAECgYEAnqw6U5QGTaSWVxGf/P3J7ENyw07Ms7LbOIV90ZllIivdi7vM/obpf3/bMm3RGahovc5NoPqzbh8U3Y+Kh24F9EQZxuBmSbC7vdj52sKrhj0Yh+U4Q4latDeLAFE/BAm4MDSLn8yqp4MGRwfEUJTbtaGRqVM4wGiBz5hX6B+Sg0ECQQD3QawAzCvbhlwLzEHzs5zdhso4rZAwKo7vaVXMwSjSFJjSH6AKNofGMq9MI0ggUvuAcYsmB5sHckhJf3hK3ze9AkEAwt8qOSX5la+PSTx9LuNsbPySyyGCSfJJHLiLBI/8EPs+0tZPfazJryhn5x+ls5Pk/8Gh0jVbuKXf4wKUbm72WQJBAI6ZhH7NQfoQqmNvgRXxH+YvR8+aYg81bEwtfvtg548Jq/17mcxqGLI5JxqNOprR4RT55xuexU1tHr2s046y4C0CQQCwrt39iqzMRqzHXgyyIokAF9CQVAVpPj+Dxt5keJe2XdYMGztfWvZR1XS/XTZDp2gNa2ZozPAoblyb2o7xwbGhAkEAgt7Uj3AOMPcqUj/1iQXjn43P1b2LEZqoEL8Ws9NdaasMHtfiyj0nwkr/HL6RwooSnLf+eY5pY6IH4ofvx+wE4A=="
		);
		service.init();
//		final LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
//		context.getLoggerList().forEach(logger -> {
//			logger.setLevel(Level.INFO);
//		});
	}
	
	@Test
	public void testGateway() {
		String orderId = String.valueOf(System.nanoTime());
//		orderId = "fail";
//		orderId = "exception";
		final Map<String, Object> request = new HashMap<>();
		request.put("orderId", orderId);
		request.put("amount", "100");
		request.put("gateway", "/pay");
		request.put("orderId", orderId);
		request.put("requestTime", DateUtils.buildTime());
		request.put("reserved", "保留数据");
		service.signature(request);
		final String json = HTTPUtils.post("http://localhost:8080/gateway", JSONUtils.serialize(request));
		this.response(orderId, json);
	}
	
	@Test
	public void testGatewayCosted() throws InterruptedException {
		final int count = 10000;
		final CountDownLatch down = new CountDownLatch(count);
		final ExecutorService executors = Executors.newFixedThreadPool(100);
		final long begin = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			executors.submit(() -> {
				this.testGateway();
				down.countDown();
			});
		}
		down.await();
		final long end = System.currentTimeMillis();
		executors.shutdown();
		LOGGER.info("执行时间：{}", end - begin);
	}
	
	public void response(String orderId, String json) {
		final Map<String, Object> response = JSONUtils.toMap(json);
		if(service.verify(response)) {
			final String code = (String) response.get(GatewayService.GATEWAY_CODE);
			if(!orderId.equals(response.get("orderId"))) {
				LOGGER.error("订单编号错误：{}-{}", orderId, response.get("orderId"));
			}
			if("0000".equals(code)) {
				LOGGER.debug("请求执行成功：{}", json);
			} else {
				LOGGER.error("请求执行失败：{}", json);
			}
		} else {
			LOGGER.error("验签失败：{}", json);
		}
	}
	
}

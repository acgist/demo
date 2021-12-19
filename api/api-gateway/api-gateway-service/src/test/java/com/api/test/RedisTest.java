package com.api.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.api.main.ApiGatewayServiceApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiGatewayServiceApplication.class)
public class RedisTest {

//	@Autowired
//	private RedisService redisService;
//	@Autowired
//	private CacheManager cacheManager;
	
	@Test
	public void test() {
//		long begin = System.currentTimeMillis();
//		for (int i = 0; i < 100000; i++) {
//			redisService.find(i);
//		}
//		long end = System.currentTimeMillis();
//		System.out.println(end - begin);
		
//		ResultMessage message = new ResultMessage();
//		message.setCode("1234");
//		message.setMessage("test");
//		redisService.set("1234", message, 1000, TimeUnit.SECONDS);
//		message = (ResultMessage) redisService.get("1234");
//		System.out.println(message.getCode());
//		System.out.println(message.getMessage());
//		redisService.del("1234");
		
//		ResultMessage message = new ResultMessage();
//		message.setCode("1234");
//		message.setMessage("test");
//		cacheManager.getCache("test").put("1234", message);
//		message = (ResultMessage) cacheManager.getCache("test").get("1234").get();
//		System.out.println(message.getCode());
//		System.out.println(message.getMessage());
//		cacheManager.getCache("test").evict("1234");
		
//		for (int i = 0; i < 10000; i++) {
//			redisService.time("test", "123");
//			redisService.time("test", String.valueOf(System.currentTimeMillis()));
//		}
//		System.out.println(redisService.time("test", "123"));
//		System.out.println(redisService.time("test", "123"));
	}
	
}

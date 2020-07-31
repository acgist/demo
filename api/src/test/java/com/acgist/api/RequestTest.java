package com.acgist.api;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class RequestTest {

	@Test
	public void thread() throws InterruptedException {
		HttpClient client = HttpClients.createDefault();
		ExecutorService executors = Executors.newFixedThreadPool(20);
		int count = 10;
		CountDownLatch down = new CountDownLatch(count);
		for (int i = 0; i < count; i++) {
			executors.submit(() -> {
				HttpResponse response;
				try {
					response = client.execute(new HttpGet("http://localhost:8080/gateway/api/pay"));
					System.out.println(EntityUtils.toString(response.getEntity()));
				} catch (IOException e) {
				} finally {
					down.countDown();
				}
			});
		}
		down.await();
		System.out.println("完成");
		executors.shutdown();
	}
	
}

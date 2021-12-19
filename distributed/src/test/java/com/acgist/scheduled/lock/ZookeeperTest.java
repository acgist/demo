package com.acgist.scheduled.lock;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ZookeeperTest {

	@Autowired
	private ZookeeperLock zookeeperLock;

	@Test
	public void testLock() {
		this.zookeeperLock.tryLock("acgist", 10);
		this.zookeeperLock.tryLock("acgist", 10);
		this.zookeeperLock.tryLock("acgist", 10);
		this.zookeeperLock.tryLock("acgist", 10);
		this.zookeeperLock.tryLock("acgist", 10);
		this.zookeeperLock.unlock("acgist");
		this.zookeeperLock.unlock("acgist");
		this.zookeeperLock.unlock("acgist");
		this.zookeeperLock.unlock("acgist");
		assertTrue(this.zookeeperLock.tryLock("acgist", 1));
	}
	
	@Test
	public void testUnlock() {
		this.zookeeperLock.unlock("acgist");
	}

	@Test
	public void testThreadLock() throws InterruptedException {
		final AtomicBoolean atomicBoolean = new AtomicBoolean(true);
		this.zookeeperLock.tryLock("acgist", 100);
		final Thread thread = new Thread(() -> atomicBoolean.set(this.zookeeperLock.tryLock("acgist", 10)));
		thread.start();
		thread.join();
		this.zookeeperLock.unlock("acgist");
		assertFalse(atomicBoolean.get());
	}

	@Test
	public void testThread() {
		try {
			if (this.zookeeperLock.tryLock("acgist", 100000, 1000)) {
				System.out.println("加锁成功" + Thread.currentThread().getId());
				try {
					if (this.zookeeperLock.tryLock("acgist", 100000, 1000)) {
						System.out.println("加锁重入成功" + Thread.currentThread().getId());
					} else {
						System.err.println("加锁重入失败");
					}
				} finally {
					this.zookeeperLock.unlock("acgist");
				}
			} else {
				System.err.println("加锁失败");
			}
		} finally {
			this.zookeeperLock.unlock("acgist");
		}
	}

	@Test
	public void testThreads() throws InterruptedException {
		final ExecutorService executor = Executors.newFixedThreadPool(100);
		final int count = 1000;
		final long start = System.currentTimeMillis();
		final CountDownLatch latch = new CountDownLatch(count);
		for (int index = 0; index < count; index++) {
			executor.submit(() -> {
				this.testThread();
				latch.countDown();
			});
		}
		latch.await();
		System.out.println(System.currentTimeMillis() - start);
	}

}

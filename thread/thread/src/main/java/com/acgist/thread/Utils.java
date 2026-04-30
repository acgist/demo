package com.acgist.thread;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Utils {

    public static void main(String[] args) throws InterruptedException {
        // 原子操作
        final AtomicBoolean atomicBoolean = new AtomicBoolean();
//        AtomicLong
//        AtomicInteger
//        AtomicReference<Boolean>
        // 线程安全集合
        final Set<String> set = new CopyOnWriteArraySet<>();
        final List<String> list = new CopyOnWriteArrayList<>();
        final Map<String, String> map = new ConcurrentHashMap<>();
        // 计数器
        final CountDownLatch countDownLatch = new CountDownLatch(2);
        final Thread threadA = new Thread(() -> {
            System.out.println("a");
            countDownLatch.countDown();
        });
        final Thread threadB = new Thread(() -> {
            System.out.println("b");
            countDownLatch.countDown();
        });
        threadA.start();
        threadB.start();
        countDownLatch.await(5, TimeUnit.SECONDS);
        System.out.println("完成");
        // 信号量
        final Semaphore semaphore = new Semaphore(10);
        final ExecutorService pool = Executors.newCachedThreadPool();
        final AtomicInteger index = new AtomicInteger();
        for (int i = 0; i < 11; i++) {
            pool.execute(() -> {
                try {
                    semaphore.acquire();
                    System.out.println("semaphore = " + index.getAndIncrement());
                } catch (InterruptedException e) {
                } finally {
                    semaphore.release();
                }
            });
        }
        pool.awaitTermination(2, TimeUnit.SECONDS);
        System.out.println("完成：" + pool.shutdownNow().size());
        // 栅栏
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        final Thread threadC = new Thread(() -> {
            while(true) {
                try {
                    cyclicBarrier.await();
                    Thread.sleep(100);
                } catch (InterruptedException | BrokenBarrierException e) {
                }
                System.out.println("a".repeat(10));
            }
        });
        final Thread threadD = new Thread(() -> {
            while(true) {
                try {
                    cyclicBarrier.await();
                    Thread.sleep(100);
                } catch (InterruptedException | BrokenBarrierException e) {
                }
                System.out.println("b".repeat(10));
            }
        });
        threadC.start();
        threadD.start();
        threadC.join();
        threadD.join();
    }
    
}

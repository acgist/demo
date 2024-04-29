package com.acgist.thread;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPool {

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        // 常用线程池
        // 缓冲线程池：不限制大小、60秒空闲存活时间
        Executors.newCachedThreadPool();
        // 任务窃取线程池：ForkJoinPool
        Executors.newWorkStealingPool();
        // 固定线程池
        Executors.newFixedThreadPool(10);
        // 定时任务线程池
        Executors.newScheduledThreadPool(4);
        // 单线程池
        Executors.newSingleThreadExecutor();
        // 单定时任务线程池
        Executors.newSingleThreadScheduledExecutor();
        // 自定义线程池：初始4个、最大8个、60秒空闲时间
        new ThreadPoolExecutor(4, 8, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        // 使用时建议设置线程池工厂
        final AtomicInteger index = new AtomicInteger();
        final ExecutorService pool = Executors.newFixedThreadPool(10, (runnable) -> {
            final Thread thread = new Thread(runnable);
            thread.setName("name" + index.get());
            thread.setDaemon(true);
            // 不要调用start方法
            return thread;
        });
        // 直接执行
        pool.execute(() -> {
            System.out.println("execute");
        });
        // 返回数据
        final Future<?> future = pool.submit(() -> {
            System.out.println("future");
            return 1;
        });
        // 没有return时ret == null
        final Object ret = future.get(5, TimeUnit.SECONDS);
        System.out.println(ret);
        // 关闭线程池：不接受新的任务、队列中和执行中的执行完成、空闲线程interrupt
        pool.shutdown();
        // 立即关闭线程池：不接受新的任务、所有线程执行interrupt、清空队列返回没有执行的任务
        pool.shutdownNow();
    }
    
}

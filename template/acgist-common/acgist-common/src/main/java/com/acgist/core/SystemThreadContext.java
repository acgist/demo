package com.acgist.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>系统线程上下文</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public final class SystemThreadContext {

	private static final Logger LOGGER = LoggerFactory.getLogger(SystemThreadContext.class);
	
	/** 系统线程 */
	public static final String ACGIST_THREAD = "Acgist-Thread";
	/** 定时线程 */
	public static final String ACGIST_THREAD_TIMER = ACGIST_THREAD + "-Timer";
	/** HTTP客户端线程 */
	public static final String ACGIST_THREAD_HTTP_CLIENT = ACGIST_THREAD + "-HTTP-Client";
	/** HTTP服务端线程 */
	public static final String ACGIST_THREAD_HTTP_SERVER = ACGIST_THREAD + "-HTTP-Server";
	
	/**
	 * <p>系统线程池</p>
	 */
	private static final ExecutorService EXECUTOR;
	/**
	 * <p>系统定时线程池</p>
	 */
	private static final ScheduledExecutorService EXECUTOR_TIMER;
	
	static {
		LOGGER.info("启动系统线程池");
		EXECUTOR = newExecutor(4, 20, 100, 60L, ACGIST_THREAD);
		EXECUTOR_TIMER = newTimerExecutor(2, ACGIST_THREAD_TIMER);
	}
	
	/**
	 * <p>异步任务</p>
	 * 
	 * @param runnable 任务
	 */
	public static final void submit(Runnable runnable) {
		EXECUTOR.submit(runnable);
	}

	/**
	 * <p>定时任务（不重复执行）</p>
	 * 
	 * @param delay 延迟时间
	 * @param unit 时间单位
	 * @param runnable 任务
	 */
	public static final ScheduledFuture<?> timer(long delay, TimeUnit unit, Runnable runnable) {
		return EXECUTOR_TIMER.schedule(runnable, delay, unit);
	}
	
	/**
	 * <p>定时任务（重复执行）</p>
	 * <p>固定时间（周期不受执行时间影响）</p>
	 * 
	 * @param delay 延迟时间
	 * @param period 周期时间
	 * @param unit 时间单位
	 * @param runnable 任务
	 */
	public static final ScheduledFuture<?> timer(long delay, long period, TimeUnit unit, Runnable runnable) {
		return EXECUTOR_TIMER.scheduleAtFixedRate(runnable, delay, period, unit);
	}
	
	/**
	 * <p>定时任务（重复执行）</p>
	 * <p>固定周期（周期受到执行时间影响）</p>
	 * 
	 * @param delay 延迟时间
	 * @param period 周期时间
	 * @param unit 时间单位
	 * @param runnable 任务
	 */
	public static final ScheduledFuture<?> timerFixedDelay(long delay, long period, TimeUnit unit, Runnable runnable) {
		return EXECUTOR_TIMER.scheduleWithFixedDelay(runnable, delay, period, unit);
	}
	
	/**
	 * <p>创建固定线程池</p>
	 * 
	 * @param corePoolSize 初始线程数量
	 * @param maximumPoolSize 最大线程数量
	 * @param queueSize 等待线程队列长度
	 * @param keepAliveTime 线程空闲时间（秒）
	 * @param name 线程名称
	 */
	public static final ExecutorService newExecutor(int corePoolSize, int maximumPoolSize, int queueSize, long keepAliveTime, String name) {
		return new ThreadPoolExecutor(
			corePoolSize,
			maximumPoolSize,
			keepAliveTime,
			TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>(queueSize),
			SystemThreadContext.newThreadFactory(name)
		);
	}
	
	/**
	 * <dl>
	 * 	<dt>创建缓存线程池</dt>
	 * 	<dd>不限制线程池大小</dd>
	 * 	<dd>初始线程数量：0</dd>
	 * 	<dd>线程存活时间：60S</dd>
	 * </dl>
	 * 
	 * @param name 线程池名称
	 */
	public static final ExecutorService newCacheExecutor(String name) {
		return new ThreadPoolExecutor(
			0,
			Integer.MAX_VALUE,
			60L,
			TimeUnit.SECONDS,
			new SynchronousQueue<Runnable>(),
			SystemThreadContext.newThreadFactory(name)
		);
	}
	
	/**
	 * <p>创建定时线程池</p>
	 * 
	 * @param corePoolSize 初始线程数量
	 * @param name 线程池名称
	 */
	public static final ScheduledExecutorService newTimerExecutor(int corePoolSize, String name) {
		return new ScheduledThreadPoolExecutor(
			corePoolSize,
			SystemThreadContext.newThreadFactory(name)
		);
	}
	
	/**
	 * <p>创建线程池工厂</p>
	 * 
	 * @param poolName 线程池名称
	 */
	private static final ThreadFactory newThreadFactory(String poolName) {
		return new ThreadFactory() {
			@Override
			public Thread newThread(Runnable runnable) {
				final Thread thread = new Thread(runnable);
				thread.setName(poolName);
				thread.setDaemon(true);
				return thread;
			}
		};
	}
	
	/**
	 * <p>关闭系统线程池</p>
	 */
	public static final void shutdown() {
		LOGGER.info("关闭系统线程池");
		shutdown(EXECUTOR);
		shutdown(EXECUTOR_TIMER);
	}
	
	/**
	 * <p>关闭线程池</p>
	 */
	public static final void shutdown(ExecutorService executor) {
		if(executor == null || executor.isShutdown()) {
			return;
		}
		try {
			executor.shutdown();
		} catch (Exception e) {
			LOGGER.error("关闭线程池异常", e);
		}
	}
	
	/**
	 * <p>关闭线程池</p>
	 * <p>注意：立即关闭可能导致部分任务没有执行</p>
	 */
	public static final void shutdownNow(ExecutorService executor) {
		if(executor == null || executor.isShutdown()) {
			return;
		}
		try {
			executor.shutdownNow();
		} catch (Exception e) {
			LOGGER.error("关闭线程池异常", e);
		}
	}
	
	/**
	 * <p>关闭定时线程池</p>
	 * 
	 * @param scheduledFuture 定时线程池
	 * 
	 * @since 1.1.0
	 */
	public static final void shutdown(ScheduledFuture<?> scheduledFuture) {
		if(scheduledFuture == null || scheduledFuture.isCancelled()) {
			return;
		}
		try {
			scheduledFuture.cancel(true);
		} catch (Exception e) {
			LOGGER.error("定时任务取消异常", e);
		}
	}

}

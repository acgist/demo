package com.acgist.gateway.notice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>异步通知消息线程</p>
 */
@Component
public class NoticeThread extends Thread {

	@Autowired
	private NoticeService noticeService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NoticeThread.class);
	private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(100);
	
	@Override
	public void run() {
		while (true) {
			try {
				final NoticeMessage message = noticeService.take();
				if(message == null) {
					continue;
				}
				EXECUTOR.submit(() -> {
					this.notice(message);
				});
			} catch (Exception e) {
				LOGGER.error("异步通知消息线程异常", e);
			}
		}
	}
	
	/**
	 * <p>发送异步通知消息</p>
	 * 
	 * @param message 异步通知消息
	 */
	public void notice(NoticeMessage message) {
		LOGGER.debug("消息通知：{}", message.getData());
	}
	
}
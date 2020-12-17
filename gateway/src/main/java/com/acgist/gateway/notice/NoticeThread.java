package com.acgist.gateway.notice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.acgist.gateway.response.GatewayResponse;

/**
 * 消息线程
 */
@Component
public class NoticeThread extends Thread {

	@Autowired
	private NoticeService asynService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NoticeThread.class);
	private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(100);
	
	@Override
	public void run() {
		while (true) {
			try {
				NoticeMessage asynMessage = asynService.take();
				if(asynMessage == null) {
					continue;
				}
				EXECUTOR.submit(() -> {
					notice(asynMessage);
				});
			} catch (Exception e) {
				LOGGER.error("消息线程异常", e);
			}
		}
	}
	
	/**
	 * 通知内容
	 */
	public void notice(NoticeMessage asynMessage) {
//		APIRequest apiRequest = asynMessage.getApiRequest();
		GatewayResponse apiResponse = asynMessage.getApiResponse();
		LOGGER.debug("消息通知，QueryId：{}", apiResponse.getQueryId());
	}
	
}

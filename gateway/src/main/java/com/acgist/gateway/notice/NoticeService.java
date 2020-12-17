package com.acgist.gateway.notice;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.acgist.gateway.GatewaySession;
import com.acgist.gateway.service.GatewayService;

/**
 * <p>异步通知消息</p>
 */
@Service
public class NoticeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(NoticeService.class);
	
	@Autowired
	private ApplicationContext context;
	
	@PostConstruct
	public void init() {
		LOGGER.info("初始化异步消息处理线程");
		this.context.getBean(NoticeThread.class).start();
	}
	
	/**
	 * <p>异步通知消息队列</p>
	 */
	private static final BlockingQueue<NoticeMessage> MESSAGE_QUEUE = new ArrayBlockingQueue<>(20000);
	
	/**
	 * <p>添加异步通知消息</p>
	 * 
	 * @param session session
	 */
	public void put(GatewaySession session) {
		if(session == null) {
			return;
		}
		if(!MESSAGE_QUEUE.offer(new NoticeMessage(session.getQueryId(), (String) session.getRequest(GatewayService.GATEWAY_NOTICE_URL), session.getResponseData()))) {
			LOGGER.error("添加异步通知消息失败：{}", session.getQueryId());
		}
	}
	
	/**
	 * <p>获取异步通知消息</p>
	 * 
	 * @return 异步通知消息
	 */
	public NoticeMessage take() {
		try {
			return MESSAGE_QUEUE.take();
		} catch (InterruptedException e) {
			LOGGER.error("获取异步通知消息异常", e);
		}
		return null;
	}
	
}

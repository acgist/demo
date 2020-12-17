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
import com.acgist.gateway.request.GatewayRequest;
import com.acgist.gateway.response.GatewayResponse;

/**
 * 异步处理
 */
@Service
public class NoticeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(NoticeService.class);
	
	@Autowired
	private ApplicationContext context;
	
	@PostConstruct
	public void init() {
		LOGGER.info("初始化异步消息处理线程");
		context.getBean(NoticeThread.class).start();
	}
	
	/**
	 * 消息队列
	 */
	private static final BlockingQueue<NoticeMessage> MESSAGE_QUEUE = new ArrayBlockingQueue<>(20000);
	
	/**
	 * 添加信息
	 * @param session 交互内容
	 */
	public void put(GatewaySession session) {
		if(session == null) {
			return;
		}
		this.put(session.getGatewayRequest(), session.getApiResponse());
	}
	
	/**
	 * 添加信息
	 * @param apiRequest 请求内容
	 * @param apiResponse 响应内容
	 */
	public void put(GatewayRequest apiRequest, GatewayResponse apiResponse) {
		if(apiRequest == null || apiResponse == null) {
			return;
		}
		if(!MESSAGE_QUEUE.offer(new NoticeMessage(apiRequest, apiResponse))) {
			LOGGER.error("消息插入失败，QueryId:{}", apiResponse.getQueryId());
		}
	}
	
	/**
	 * 获取消息
	 */
	public NoticeMessage take() {
		try {
			return MESSAGE_QUEUE.take();
		} catch (InterruptedException e) {
			LOGGER.error("消息获取异常", e);
		}
		return null;
	}
	
}

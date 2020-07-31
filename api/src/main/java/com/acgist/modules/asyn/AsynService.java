package com.acgist.modules.asyn;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.acgist.api.SessionComponent;
import com.acgist.api.request.APIRequest;
import com.acgist.api.response.APIResponse;

/**
 * 异步处理
 */
@Service
public class AsynService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AsynService.class);
	
	@Autowired
	private ApplicationContext context;
	
	@PostConstruct
	public void init() {
		LOGGER.info("初始化异步消息处理线程");
		context.getBean(AsynThread.class).start();
	}
	
	/**
	 * 消息队列
	 */
	private static final BlockingQueue<AsynMessage> MESSAGE_QUEUE = new ArrayBlockingQueue<>(20000);
	
	/**
	 * 添加信息
	 * @param session 交互内容
	 */
	public void put(SessionComponent session) {
		if(session == null) {
			return;
		}
		this.put(session.getApiRequest(), session.getApiResponse());
	}
	
	/**
	 * 添加信息
	 * @param apiRequest 请求内容
	 * @param apiResponse 响应内容
	 */
	public void put(APIRequest apiRequest, APIResponse apiResponse) {
		if(apiRequest == null || apiResponse == null) {
			return;
		}
		if(!MESSAGE_QUEUE.offer(new AsynMessage(apiRequest, apiResponse))) {
			LOGGER.error("消息插入失败，QueryId:{}", apiResponse.getQueryId());
		}
	}
	
	/**
	 * 获取消息
	 */
	public AsynMessage take() {
		try {
			return MESSAGE_QUEUE.take();
		} catch (InterruptedException e) {
			LOGGER.error("消息获取异常", e);
		}
		return null;
	}
	
}

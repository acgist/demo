package com.api.core.asyn.stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;

import com.api.core.asyn.config.LogReceiverBinding;
import com.api.core.asyn.pojo.message.LogMessage;
import com.api.data.asyn.pojo.entity.LogEntity;
import com.api.data.asyn.repository.LogRepository;
import com.api.utils.ValidatorUtils;

/**
 * 日志信息队列接收器
 */
@EnableBinding(LogReceiverBinding.class)
public class LogMessageReceiver {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogMessageReceiver.class);
	
	@Autowired
	private LogRepository logRepository;
	
	@StreamListener(LogReceiverBinding.LOG_RECEIVER_STREAM_BINDER)
	public void receive(Message<LogMessage> message) {
		LogMessage logMessage = message.getPayload();
		if(logMessage == null || logMessage.getEntity() == null) {
			LOGGER.warn("日志信息为空");
		} else {
			LogEntity entity = logMessage.getEntity();
			String error = null;
			if((error = ValidatorUtils.verify(entity)) != null) {
				LOGGER.error("日志信息不完整：{}", error);
			} else {
				logRepository.save(entity);
			}
		}
	}

}

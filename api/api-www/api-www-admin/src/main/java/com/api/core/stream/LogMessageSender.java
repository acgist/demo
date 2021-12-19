package com.api.core.stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

import com.api.core.asyn.config.LogSenderBinding;
import com.api.core.asyn.pojo.message.LogMessage;
import com.api.data.asyn.pojo.entity.LogEntity;

/**
 * 日志信息放入消息队列
 */
@EnableBinding(LogSenderBinding.class)
public class LogMessageSender {

	@Autowired
	@Output(LogSenderBinding.LOG_SENDER_STREAM_BINDER)
	private MessageChannel channel;

	public void log(LogEntity log) {
		LogMessage message = new LogMessage();
		message.setEntity(log);
		channel.send(MessageBuilder.withPayload(message).build());
	}

}

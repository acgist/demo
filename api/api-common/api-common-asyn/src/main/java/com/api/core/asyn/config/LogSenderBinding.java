package com.api.core.asyn.config;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * 日志信息发送
 */
public interface LogSenderBinding {

	String LOG_SENDER_STREAM_BINDER = "api_message_log_sender";

	@Output(LOG_SENDER_STREAM_BINDER)
	MessageChannel output();

}

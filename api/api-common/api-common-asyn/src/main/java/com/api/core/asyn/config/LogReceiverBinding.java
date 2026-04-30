package com.api.core.asyn.config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * 日志信息接收
 */
public interface LogReceiverBinding {

	String LOG_RECEIVER_STREAM_BINDER = "api_message_log_receiver";

	@Input(LOG_RECEIVER_STREAM_BINDER)
	SubscribableChannel input();

}

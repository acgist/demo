package com.api.core.asyn.config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * 邮件信息接收
 */
public interface MailReceiverBinding {

	String MAIL_RECEIVER_STREAM_BINDER = "api_message_mail_receiver";

	@Input(MAIL_RECEIVER_STREAM_BINDER)
	SubscribableChannel input();

}

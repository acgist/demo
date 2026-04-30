package com.api.core.asyn.config;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * 邮件信息发送
 */
public interface MailSenderBinding {

	String MAIL_SENDER_STREAM_BINDER = "api_message_mail_sender";

	@Output(MAIL_SENDER_STREAM_BINDER)
	MessageChannel output();

}

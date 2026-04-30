package com.api.core.stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

import com.api.core.asyn.config.MailSenderBinding;
import com.api.core.asyn.pojo.message.MailMessage;

/**
 * 邮件信息放入消息队列
 */
@EnableBinding(MailSenderBinding.class)
public class MailMessageSender {

	@Autowired
	@Output(MailSenderBinding.MAIL_SENDER_STREAM_BINDER)
	private MessageChannel channel;

	public void send(String to, String subject, String content) {
		MailMessage message = new MailMessage(to, subject, content);
		channel.send(MessageBuilder.withPayload(message).build());
	}

}

package com.api.core.asyn.stream;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;

import com.api.core.asyn.config.MailReceiverBinding;
import com.api.core.asyn.pojo.message.MailMessage;
import com.api.core.asyn.service.MailService;

/**
 * 邮件信息队列接收器<br>
 * 使用线程池发送，防止单个邮件发送时间过长导致线程阻塞
 */
@EnableBinding(MailReceiverBinding.class)
public class MailMessageReceiver {

	private static final Logger LOGGER = LoggerFactory.getLogger(MailMessageReceiver.class);
	
	@Autowired
	private MailService mailService;
	
	// 发送线程池
	private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(20);
	
	@StreamListener(MailReceiverBinding.MAIL_RECEIVER_STREAM_BINDER)
	public void receive(Message<MailMessage> message) {
		MailMessage mailMessage = message.getPayload();
		if(mailMessage == null) {
			LOGGER.warn("发送邮件信息为空");
		} else {
			EXECUTOR.submit(() -> {
				mailService.post(mailMessage.getTo(), mailMessage.getSubject(), mailMessage.getContent());
			});
		}
	}

}

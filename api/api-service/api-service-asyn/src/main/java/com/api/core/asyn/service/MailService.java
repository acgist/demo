package com.api.core.asyn.service;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * service - 邮件
 */
@Service
public class MailService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);
	
	@Value("${spring.mail.username:}")
	private String mail;

	@Autowired
	private JavaMailSender mailSender;
	
	/**
	 * 发送邮件
	 * @param to 收件人
	 * @param subject 主题
	 * @param content 内容
	 */
	public void post(String to, String subject, String content) {
		if(StringUtils.isEmpty(to) || StringUtils.isEmpty(subject) || StringUtils.isEmpty(content)) {
			LOGGER.warn("发送邮件内容异常，收件人：{}，主题：{}，内容：{}", to, subject, content);
			return;
		}
		try {
//			SimpleMailMessage message = new SimpleMailMessage();
//			message.setFrom(mail);
//			message.setTo(to);
//			message.setSubject(subject);
//			message.setText(content);
//			mailSender.send(message);
			
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(mail);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content, true);
			mailSender.send(message);
			
//			MimeMessage message = mailSender.createMimeMessage();
//			MimeMessageHelper helper = new MimeMessageHelper(message, true);
//			helper.setFrom(mail);
//			helper.setTo(to);
//			helper.setSubject(subject);
//			helper.setText(content);
//			FileSystemResource file = new FileSystemResource(new File("文件路径"));
//			helper.addAttachment("文件名称", file);
//			mailSender.send(message);
		} catch (Exception e) {
			LOGGER.error("邮件发送异常", e);
		}
	}
	
}

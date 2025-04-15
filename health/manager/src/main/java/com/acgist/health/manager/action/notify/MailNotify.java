package com.acgist.health.manager.action.notify;

import javax.mail.internet.MimeMessage;

import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.acgist.health.manager.action.INotify;
import com.acgist.health.manager.configuration.ManagerProperties.Mail;
import com.acgist.health.manager.configuration.ManagerProperties.Monitor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailNotify implements INotify {


    private final JavaMailSender javaMailSender;
    private final MailProperties mailProperties;

    @Override
    public String type() {
        return INotify.TYPE_MAIL;
    }

    @Override
    public boolean monitorNotify(String content, Monitor monitor) {
        final Mail mail = monitor.getMail();
        final long currentTime = System.currentTimeMillis();
        if(mail == null || mail.getTo() == null || mail.getSendLimit() == null) {
            log.info("邮件通知配置缺失");
            return false;
        }
        if(mail.getLastSendTime() != null && currentTime - mail.getLastSendTime() < mail.getSendLimit()) {
            return true;
        }
        mail.setLastSendTime(currentTime);
        final Thread thread = new Thread(() -> {
            this.sendMail(mail.getTo(), mail.getSubject(), content);
        });
        thread.setDaemon(true);
        thread.start();
        return true;
    }

    private void sendMail(String to, String subject, String content) {
        if(this.mailProperties == null || this.javaMailSender == null) {
            log.info("邮件发送配置缺失");
            return;
        }
		try {
			final MimeMessage message = this.javaMailSender.createMimeMessage();
			final MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(to);
			helper.setFrom(this.mailProperties.getUsername());
			helper.setSubject(subject);
			helper.setText(content, true);
			this.javaMailSender.send(message);
		} catch (Exception e) {
            log.error("邮件发送异常", e);
		}
    }

}

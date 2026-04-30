package com.api.core.asyn.pojo.message;

import com.api.core.pojo.message.BaseMessage;

/**
 * 邮件消息
 */
public class MailMessage extends BaseMessage {

	private static final long serialVersionUID = 1L;

	private String to; // 收件人
	private String subject; // 主题
	private String content; // 内容

	public MailMessage(String to, String subject, String content) {
		this.to = to;
		this.subject = subject;
		this.content = content;
	}

	public String getTo() {
		return to;
	}

	public String getSubject() {
		return subject;
	}

	public String getContent() {
		return content;
	}

}

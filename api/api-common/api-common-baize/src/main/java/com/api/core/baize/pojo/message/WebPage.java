package com.api.core.baize.pojo.message;

import java.io.Serializable;

/**
 * 页面内容
 */
public class WebPage implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String html;
	private String content;

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}

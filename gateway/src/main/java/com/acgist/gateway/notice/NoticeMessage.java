package com.acgist.gateway.notice;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>异步通知</p>
 */
public class NoticeMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>请求ID</p>
	 */
	private String queryId;
	/**
	 * <p>通知地址</p>
	 */
	private String noticeURL;
	/**
	 * <p>通知数据</p>
	 */
	private Map<String, Object> data;

	public NoticeMessage() {
	}

	public NoticeMessage(String queryId, String noticeURL, Map<String, Object> data) {
		this.queryId = queryId;
		this.noticeURL = noticeURL;
		this.data = data;
	}

	public String getQueryId() {
		return queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	public String getNoticeURL() {
		return noticeURL;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setNoticeURL(String noticeURL) {
		this.noticeURL = noticeURL;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

}

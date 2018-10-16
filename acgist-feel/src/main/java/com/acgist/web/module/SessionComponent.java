package com.acgist.web.module;

import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class SessionComponent {

	private static final long MAX_PROCESS_TIME = 30L * 1000;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SessionComponent.class);
	
	private long begin; // 开始时间
	private String queryId; // 请求的唯一标识
	private boolean process = false; // 是否处于处理中
	private Map<String, String> reqData; // 请求数据
	private Map<String, String> resData; // 响应数据

	public static final SessionComponent getInstance(ApplicationContext context) {
		return context.getBean(SessionComponent.class);
	}
	
	public Map<String, String> getReqData() {
		return reqData;
	}

	public void setReqData(Map<String, String> reqData) {
		this.reqData = reqData;
	}

	public Map<String, String> getResData() {
		return resData;
	}

	public void setResData(Map<String, String> resData) {
		this.resData = resData;
	}
	
	public String getQueryId() {
		return queryId;
	}

	public boolean newProcess() {
		if(this.process) {
			return false;
		}
		this.begin = System.currentTimeMillis();
		this.process = true;
		this.queryId = UUID.randomUUID().toString();
		return true;
	}
	
	public void complete() {
		long interval = System.currentTimeMillis() - this.begin;;
		if(interval > MAX_PROCESS_TIME) {
			LOGGER.warn("处理时间超过时间阀值，处理时间：{}，请求数据：{}，响应数据：{}", interval, this.reqData, this.resData);
		}
		this.begin = 0;
		this.process = false;
		this.queryId = null;
	}

}

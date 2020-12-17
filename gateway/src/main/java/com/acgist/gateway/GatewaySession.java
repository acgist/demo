package com.acgist.gateway;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.acgist.gateway.config.Gateway;
import com.acgist.gateway.request.GatewayRequest;
import com.acgist.gateway.response.GatewayResponse;

/**
 * 请求数据
 * 	session：发生异常、重定向、请求转发均不会丢失数据
 * 	request：发生异常、请求转发均不会丢失数据，重定向时会丢失数据
 */
@Component
@Scope("request")
//@Scope("session")
public class GatewaySession implements Serializable {

	private static final long serialVersionUID = 1L;

	private long time; // 请求时间
	private boolean process = false; // 处理中
	private String queryId; // 唯一标识
	private String json; // 原始请求数据
	private Gateway gateway; // 请求类型
	private GatewayRequest request; // 请求数据
	private GatewayResponse response; // 响应数据

	private static final long MAX_PROCESS_TIME = 2 * 60 * 1000L; // 交易请求处理超时时间：两分钟

	public static final GatewaySession getInstance(ApplicationContext context) {
		return context.getBean(GatewaySession.class);
	}

	public String getQueryId() {
		return queryId;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public Gateway getApiType() {
		return gateway;
	}

	public void setApiType(Gateway apiType) {
		this.gateway = apiType;
	}

	public GatewayRequest getGatewayRequest() {
		return request;
	}

	public void setApiRequest(GatewayRequest apiRequest) {
		this.request = apiRequest;
	}

	public GatewayResponse getApiResponse() {
		return response;
	}

	public void setResponse(GatewayResponse apiResponse) {
		this.response = apiResponse;
	}

	/**
	 * 是否处于处理过程中，超过指定时间默认处理完成
	 */
	private boolean inProcess() {
		if (process) {
			return (process = (System.currentTimeMillis() - this.time < MAX_PROCESS_TIME));
		}
		return false;
	}

	/**
	 * 创建新请求
	 */
	private void newProcess(String queryId) {
		this.process = true;
		this.queryId = queryId;
		this.time = System.currentTimeMillis();
	}

	/**
	 * 创建新请求：true-创建成功，false-已有请求在处理
	 */
	public boolean buildProcess(String queryId) {
		synchronized (this) {
			if (inProcess()) {
				return false;
			} else {
				newProcess(queryId);
				return true;
			}
		}
	}

	/**
	 * 完成请求
	 */
	public void completeProcess(HttpServletRequest request) {
		synchronized (this) {
			this.process = false;
		}
	}

}

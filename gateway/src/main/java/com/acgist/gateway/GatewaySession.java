package com.acgist.gateway;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.acgist.gateway.config.Gateway;
import com.acgist.gateway.config.GatewayCode;
import com.acgist.gateway.executor.GatewayExecutor;
import com.acgist.gateway.request.GatewayRequest;
import com.acgist.gateway.service.GatewayService;
import com.acgist.gateway.service.SignatureService;
import com.acgist.utils.DateUtils;
import com.acgist.utils.JSONUtils;
import com.acgist.utils.ValidatorUtils;

/**
 * <p>请求数据</p>
 * <p>session：发生异常、请求转发均不会丢失数据（重定向时不丢失数据）</p>
 * <p>request：发生异常、请求转发均不会丢失数据（重定向时会丢失数据）</p>
 */
@Component
@Scope("request")
public class GatewaySession implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GatewaySession.class);

	@Autowired
	private ApplicationContext context;
	@Autowired
	private SignatureService signatureService;
	
	/**
	 * <p>处理中</p>
	 */
	private boolean process = false;
	/**
	 * <p>唯一请求标识</p>
	 */
	private String queryId;
	/**
	 * <p>请求类型</p>
	 */
	private Gateway gateway;
	/**
	 * <p>请求数据</p>
	 */
	private GatewayRequest request;
	/**
	 * <p>请求数据</p>
	 * <p>禁止修改数据</p>
	 */
	private Map<String, Object> requestData;
	/**
	 * <p>响应数据</p>
	 */
	private final Map<String, Object> responseData = new HashMap<>();

	public static final GatewaySession getInstance(ApplicationContext context) {
		return context.getBean(GatewaySession.class);
	}

	public String getQueryId() {
		return queryId;
	}

	public Gateway getGateway() {
		return this.gateway;
	}

	public GatewayRequest getRequest() {
		return this.request;
	}

	public Map<String, Object> getRequestData() {
		return this.requestData;
	}

	public Map<String, Object> getResponseData() {
		return this.responseData;
	}

	public void setGateway(Gateway gateway) {
		this.gateway = gateway;
	}

	public void setRequest(GatewayRequest request) {
		this.request = request;
	}

	public void setRequestData(Map<String, Object> requestData) {
		this.requestData = requestData;
	}

	/**
	 * <p>判断是否处于请求处理中</p>
	 * <p>防止多次请求数据被覆盖</p>
	 * 
	 * @return 是否处于请求处理中
	 */
	private boolean inProcess() {
		return this.process;
	}

	/**
	 * <p>创建新请求</p>
	 */
	private void newProcess(String queryId) {
		this.process = true;
		this.queryId = queryId;
	}

	/**
	 * <p>创建新请求</p>
	 * 
	 * @param queryId 请求编号
	 * 
	 * @return 是否成功
	 */
	public boolean buildProcess(String queryId) {
		synchronized (this) {
			if (this.inProcess()) {
				return false;
			} else {
				this.newProcess(queryId);
				return true;
			}
		}
	}

	/**
	 * <p>完成请求</p>
	 */
	public void completeProcess(HttpServletRequest request) {
		synchronized (this) {
			this.process = false;
			this.queryId = null;
			this.gateway = null;
			this.requestData = null;
			this.responseData.clear();
		}
	}
	
	/**
	 * <p>数据格式校验</p>
	 * 
	 * @return 错误信息
	 */
	public String validator() {
		return ValidatorUtils.verify(this.request);
	}
	
	/**
	 * <p>创建失败响应</p>
	 * 
	 * @param code 编号
	 * 
	 * @return GatewaySession
	 */
	public GatewaySession buildFail(GatewayCode code) {
		this.buildFail(code, code.getMessage());
		return this;
	}
	
	/**
	 * <p>创建失败响应</p>
	 * 
	 * @param code 编号
	 * @param message 描述
	 * 
	 * @return GatewaySession
	 */
	public GatewaySession buildFail(GatewayCode code, String message) {
		this.responseData.put(GatewayService.GATEWAY_CODE, code.getCode());
		this.responseData.put(GatewayService.GATEWAY_MESSAGE, message == null ? code.getMessage() : message);
		this.buildResponse();
		LOGGER.info("响应失败-请求报文：{}", this.requestData);
		LOGGER.info("响应失败-响应报文：{}", this.responseData);
		return this;
	}

	/**
	 * <p>创建成功响应</p>
	 * 
	 * @return GatewaySession
	 */
	public GatewaySession buildSuccess() {
		return this.buildSuccess(null);
	}
	
	/**
	 * <p>创建成功响应</p>
	 * 
	 * @param response 响应数据
	 * 
	 * @return GatewaySession
	 */
	public GatewaySession buildSuccess(Map<String, Object> response) {
		if(response != null) {
			this.responseData.putAll(response);
		}
		this.responseData.put(GatewayService.GATEWAY_CODE, GatewayCode.CODE_0000.getCode());
		this.responseData.put(GatewayService.GATEWAY_MESSAGE, GatewayCode.CODE_0000.getMessage());
		this.buildResponse();
		return this;
	}

	/**
	 * <p>验证签名</p>
	 * 
	 * @param data 数据
	 * 
	 * @return 是否成功
	 */
	public boolean verifySignature() {
		return this.signatureService.verify(this.requestData);
	}
	
	/**
	 * <p>响应数据</p>
	 * 
	 * @return 响应
	 */
	public Map<String, Object> response() {
		final GatewayExecutor<GatewayRequest> executor = this.context.getBean(this.gateway.executorClass());
		return executor.response();
	}
	
	/**
	 * <p>响应数据</p>
	 * 
	 * @param response 响应
	 */
	public void response(HttpServletResponse response) {
		try {
			response.setContentType("application/json;charset=utf-8");
//			response.getWriter().write(JSONUtils.serialize(this.responseData));
			response.getOutputStream().write(JSONUtils.serialize(this.responseData).getBytes());
		} catch (IOException e) {
			LOGGER.error("写出响应失败", e);
		}
	}
	
	/**
	 * <p>获取请求JSON</p>
	 * 
	 * @return 请求JSON
	 */
	public String getRequestJSON() {
		return JSONUtils.serialize(this.requestData);
	}
	
	/**
	 * <p>获取请求内容</p>
	 * 
	 * @param key key
	 * 
	 * @return 请求内容
	 */
	public Object getRequest(String key) {
		return this.requestData.get(key);
	}
	
	/**
	 * <p>获取响应JSON</p>
	 * 
	 * @return 响应JSON
	 */
	public String getResponseJSON() {
		return JSONUtils.serialize(this.responseData);
	}
	
	/**
	 * <p>设置响应内容</p>
	 * 
	 * @param key key
	 * @param value value
	 * 
	 * @return GatewaySession
	 */
	public GatewaySession putResponse(String key, Object value) {
		this.responseData.put(key, value);
		return this;
	}
	
	/**
	 * <p>获取响应内容</p>
	 * 
	 * @param key key
	 * 
	 * @return 响应内容
	 */
	public Object getResponse(String key) {
		return this.responseData.get(key);
	}
	
	/**
	 * <p>签名响应数据</p>
	 */
	private void buildResponse() {
		this.convertRequestToResponse();
		this.responseData.put(GatewayService.GATEWAY_QUERY_ID, this.queryId);
		this.responseData.put(GatewayService.GATEWAY_RESPONSE_TIME, DateUtils.buildTime());
		this.signatureService.signature(this.responseData);
	}
	
	/**
	 * <p>数据转换</p>
	 */
	private void convertRequestToResponse() {
		if(this.requestData != null) {
			this.requestData.forEach((key, value) -> this.responseData.computeIfAbsent(key, x -> value));
		}
	}
	
}

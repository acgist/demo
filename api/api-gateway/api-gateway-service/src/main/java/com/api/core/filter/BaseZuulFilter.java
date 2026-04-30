package com.api.core.filter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.api.core.gateway.APICode;
import com.api.core.gateway.SessionComponent;
import com.api.core.gateway.response.APIResponse;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * 过滤器基类<br>
 * POST过滤器顺序要小于1000，否者不能设置返回的responseBody，参考：SendResponseFilter<br>
 *  异常处理需要禁用SendErrorFilter：移除异常
 */
public abstract class BaseZuulFilter extends ZuulFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(BaseZuulFilter.class);
	
	// 过滤器类型
	protected static final String FILTER_TYPE_PRE = "pre";
	protected static final String FILTER_TYPE_POST = "post";
	protected static final String FILTER_TYPE_ROUTE = "route";
	protected static final String FILTER_TYPE_ERROR = "error";
	
	/**
	 * 是否执行
	 */
	@Override
	public boolean shouldFilter() {
		return true;
	}
	
	/**
	 * 是否通过验证
	 */
	protected boolean permissions() {
		return context().sendZuulResponse();
	}
	
	/**
	 * 获取RequestContext
	 */
	protected RequestContext context() {
		return RequestContext.getCurrentContext();
	}
	
	/**
	 * 获取SessionComponent
	 */
	protected SessionComponent sessionComponent() {
		return SessionComponent.getInstance(context());
	}

	/**
	 * 请求
	 */
	protected HttpServletRequest request() {
		return context().getRequest();
	}
	
	/**
	 * 响应
	 */
	protected HttpServletResponse response() {
		return context().getResponse();
	}
	
	/**
	 * 失败，默认状态码：200
	 */
	protected void error(APICode apiCode) {
		this.error(apiCode, apiCode.getMessage());
	}
	
	/**
	 * 失败，默认状态码：200
	 */
	protected void error(APICode apiCode, String message) {
		this.error(HttpStatus.OK.value(), apiCode, message);
	}
	
	/**
	 * 失败
	 */
	protected void error(int statusCode, APICode apiCode) {
		this.error(statusCode, apiCode, apiCode.getMessage());
	}
	
	/**
	 * 失败：设置返回内容编码
	 */
	protected void error(int status, APICode code, String message) {
		final RequestContext context = context();
		context.setSendZuulResponse(false);
		context.setResponseStatusCode(status);
		context.getResponse().setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		responseBody(APIResponse.builder().valueOfRequest(sessionComponent().getRequest()).buildMessage(code, message));
	}
	
	/**
	 * 设置返回内容
	 */
	protected void responseBody(APIResponse response) {
		final RequestContext context = context();
		context.setResponseBody(response.response());
	}
	
	/**
	 * stream转化为JSON字符串
	 */
	protected String streamToJSON(InputStream input) {
		if(input == null) {
			return null;
		}
		final StringBuffer builder = new StringBuffer();
		try {
			String tmp = null;
			final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			while((tmp = reader.readLine()) != null) {
				builder.append(tmp);
			}
		} catch (Exception e) {
			LOGGER.error("获取JSON报文异常", e);
		}
		return builder.toString();
	}
	
}

package com.api.core.fallback;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import com.api.core.config.APIConstApplication;
import com.api.core.gateway.API;
import com.api.core.gateway.APICode;
import com.api.core.gateway.SessionComponent;
import com.api.core.gateway.response.APIResponse;
import com.netflix.zuul.context.RequestContext;

/**
 * 熔断器 - 订单服务
 */
@Component
public class OrderFallbackProvider implements FallbackProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderFallbackProvider.class);
	
	@Override
	public String getRoute() {
		return APIConstApplication.API_SERVICE_ORDER;
	}

	@Override
	public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
		LOGGER.warn("服务不可用，服务名称：{}", getRoute());
		return new ClientHttpResponse() {
			@Override
			public HttpHeaders getHeaders() {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
				return headers;
			}
			@Override
			public InputStream getBody() throws IOException {
				RequestContext context = RequestContext.getCurrentContext();
				SessionComponent session = SessionComponent.getInstance(context);
				return new ByteArrayInputStream(APIResponse.builder().valueOfRequest(session.getRequest()).buildMessage(APICode.CODE_1002).response().getBytes(API.DEFAULT_CHARSET));
			}
			@Override
			public HttpStatus getStatusCode() throws IOException {
				return HttpStatus.SERVICE_UNAVAILABLE;
			}
			@Override
			public int getRawStatusCode() throws IOException {
				return HttpStatus.SERVICE_UNAVAILABLE.value();
			}
			@Override
			public String getStatusText() throws IOException {
				return HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase();
			}
			@Override
			public void close() {
			}
		};
	}

}

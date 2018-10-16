package com.acgist.html;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpRequest.BodyPublisher;
import jdk.incubator.http.HttpRequest.Builder;
import jdk.incubator.http.HttpResponse;

/**
 * <pre>
 * HTTP工具 - JAVA jdk.incubator.httpclient 模块
 * bug：用户自定义的User-Agent不能覆盖掉系统User-Agent
 * </pre>
 */
public class HTTPFoodie {

	private static final Logger LOGGER = LoggerFactory.getLogger(HTTPFoodie.class);
	private static final Map<String, String> HEADERS = new HashMap<String, String>();
	
	static {
		HEADERS.put("User-Agent", "Mozilla/5.0 (Windows NT 10; Win64; x64) AppleWebKit/6666 (KHTML, like Gecko) Chrome/66.66.66 Safari/6666"); // 浏览器
	}

	private static final HTTPFoodie INSTANCE = new HTTPFoodie();
	
	public static final HTTPFoodie instance() {
		return INSTANCE;
	}
	
	public String get(String uri) {
		if(uri == null) {
			return null;
		}
		Builder builder = newBuilder(uri);
		HttpRequest request = builder.GET().build();
		return execute(request);
	}
	
	public String post(String uri) {
		return this.post(uri, null);
	}

	public String post(String uri, Map<String, Object> data) {
		if(uri == null) {
			return null;
		}
		Builder builder = newBuilder(uri);
		HttpRequest request = builder.POST(body(data)).build();
		return execute(request);
	}
	
	private Builder newBuilder(String uri) {
		Builder builder = HttpRequest.newBuilder()
			.uri(URI.create(uri));
		LOGGER.info("请求地址：{}", uri);
		HEADERS.forEach((key, value) -> {
			builder.header(key, value);
		});
//		builder.header("Content-Type", "application/x-www-form-urlencoded");
		builder.timeout(Duration.ofSeconds(20));
		return builder;
	}
	
	private BodyPublisher body(Map<String, Object> data) {
		if(data == null || data.size() == 0) {
			return HttpRequest.BodyPublisher.noBody();
		}
		StringBuffer formData = new StringBuffer();
		data.forEach((key, value) -> {
			formData.append(key).append("=").append(value).append("&");
		});
		formData.setLength(formData.length() - 1);
		return HttpRequest.BodyPublisher.fromString(formData.toString());
	}
	
	private String execute(HttpRequest request) {
		HttpClient client = HttpClient.newHttpClient();
		try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandler.asString());
			if(response.statusCode() != 200) {
				LOGGER.warn("HTTP请求状态码错误，状态码：{}，请求地址：{}", response.statusCode(), request.uri());
			}
			return response.body();
		} catch (IOException | InterruptedException e) {
			LOGGER.error("HTTP请求异常", e);
		}
		return null;
	}
	
}

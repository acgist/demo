package com.acgist.ding.view.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import com.acgist.ding.Config;
import com.acgist.ding.DingView;
import com.acgist.ding.Exceptions;
import com.acgist.ding.Internationalization;
import com.acgist.ding.MapUtils;
import com.acgist.ding.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * HTTP
 * 
 * @author acgist
 */
public class HttpView extends DingView {
	
	/**
	 * 用户标识
	 */
	private static final String USER_AGENT = "Mozilla/5.0 (compatible; Ding/" + Config.VERSION + "; +https://gitee.com/acgist/ding/issues)";

	/**
	 * 请求地址
	 */
	private Text url;
	/**
	 * 请求头部
	 */
	private Text header;
	/**
	 * 请求主体
	 */
	private Text body;
	/**
	 * 主体类型
	 */
	private Combo type;
	/**
	 * 请求方法
	 */
	private Combo method;
	/**
	 * 请求编码
	 */
	private Combo charset;
	/**
	 * 响应状态
	 */
	private Text status;
	/**
	 * 请求响应
	 */
	private Text response;
	
	/**
	 * HTTP连接
	 */
	private HttpURLConnection httpURLConnection;
	/**
	 * GSON
	 */
	private final Gson gson = new Gson();
	
	static {
		final SSLContext sslContext = buildSSLContext();
		if(sslContext != null) {
			HttpsURLConnection.setDefaultHostnameVerifier(SnailHostnameVerifier.INSTANCE);
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
		}
	}
	
	@Override
	public void createPartControl(Composite composite) {
		this.layout(composite, SWT.VERTICAL);
		// 地址模块
		final Composite urlGroup = this.buildFullComposite(composite, SWT.VERTICAL);
		this.url = this.buildMultiText(urlGroup, Internationalization.Message.HTTP_URL);
		// 配置模块
		final Composite sourceGroup = this.buildFullComposite(composite, SWT.HORIZONTAL);
		this.header = this.buildMultiText(sourceGroup, Internationalization.Message.HTTP_HEADER);
		this.body = this.buildMultiText(sourceGroup, Internationalization.Message.HTTP_BODY);
		// 操作模块
		final Group operationGroup = this.buildGroup(composite, Internationalization.Label.OPERATION, 0, 0, SWT.VERTICAL);
		// 配置模块
		final Composite configGroup = this.buildRowComposite(operationGroup, SWT.HORIZONTAL);
		this.type = this.buildSelect(configGroup, Internationalization.Label.HTTP_TYPE, 0, "JSON", "TEXT", "FORM");
		this.method = this.buildSelect(configGroup, Internationalization.Label.HTTP_METHOD, 0, "GET", "POST", "PUT", "DELETE");
		this.charset = this.buildSelect(configGroup, Internationalization.Label.HTTP_CHARSET, 0, "UTF-8", "GBK", "ISO-8859-1");
		this.status = this.buildText(configGroup, Internationalization.Label.HTTP_STATUS);
		this.status.setEditable(false);
		// 按钮模块
		final Composite buttonGroup = this.buildRowComposite(operationGroup, SWT.HORIZONTAL);
		this.buildButton(buttonGroup, Internationalization.Button.URL_ENCODE, this.urlEncodeConsumer);
		this.buildButton(buttonGroup, Internationalization.Button.URL_DECODE, this.urlDecodeConsumer);
		this.buildButton(buttonGroup, Internationalization.Button.HTTP_SUBMIT, this.submitConsumer);
		this.buildButton(buttonGroup, Internationalization.Button.RESET, this.resetConsumer);
		// 响应模块
		final Composite responseGroup = this.buildFullComposite(composite, SWT.HORIZONTAL);
		this.response = this.buildMultiText(responseGroup, Internationalization.Message.HTTP_RESPONSE);
	}
	
	/**
	 * URL编码
	 */
	private Consumer<MouseEvent> urlEncodeConsumer = event -> {
		this.consumer(this.body, bodyValue -> {
			this.body.setText(URLEncoder.encode(bodyValue, StandardCharsets.UTF_8));
		});
	};
	
	/**
	 * URL解码
	 */
	private Consumer<MouseEvent> urlDecodeConsumer = event -> {
		this.consumer(this.body, bodyValue -> {
			this.body.setText(URLDecoder.decode(bodyValue, StandardCharsets.UTF_8));
		});
	};
	
	/**
	 * 提交数据
	 */
	private Consumer<MouseEvent> submitConsumer = event -> {
		this.consumer(this.url, urlValue -> {
			try {
				this.httpURLConnection = this.buildHttpURLConnection(urlValue, 5000, 5000);
				this.buildDefaultHeader();
				this.buildHeader();
				this.buildType();
				this.request();
				this.responseStatus();
				this.responseHeader();
				this.responseBody();
			} catch (IOException e) {
				Exceptions.error(this.getClass(), e);
			}
		});
	};
	
	/**
	 * 重置
	 */
	private Consumer<MouseEvent> resetConsumer = event -> {
		this.url.setText(Config.EMPTY);
		this.body.setText(Config.EMPTY);
		this.header.setText(Config.EMPTY);
		this.status.setText(Config.EMPTY);
		this.response.setText(Config.EMPTY);
	};
	
	/**
	 * 创建请求
	 * 
	 * @param urlValue 请求地址
	 * @param connectTimeout 连接时间
	 * @param receiveTimeout 读取时间
	 * 
	 * @return 请求
	 * 
	 * @throws IOException IO异常
	 */
	private HttpURLConnection buildHttpURLConnection(String urlValue, int connectTimeout, int receiveTimeout) throws IOException {
		final var requestUrl = new URL(this.buildUrl(urlValue));
		final var connection = (HttpURLConnection) requestUrl.openConnection();
		connection.setDoInput(true);
		connection.setUseCaches(false);
		connection.setReadTimeout(receiveTimeout);
		connection.setConnectTimeout(connectTimeout);
		connection.setInstanceFollowRedirects(true);
		return connection;
	}
	
	/**
	 * 设置默认请求头部
	 */
	private void buildDefaultHeader() {
		this.header("Accept", "*/*");
		this.header("User-Agent", USER_AGENT);
	}
	
	/**
	 * 设置用户请求头部
	 */
	private void buildHeader() {
		final String header = this.header.getText();
		Map<String, String> headers = this.json(header);
		if(MapUtils.isEmpty(headers)) {
			headers = this.line(header);
		}
		headers.forEach(this::header);
	}
	
	/**
	 * 设置请求类型头部
	 */
	private void buildType() {
		final String type = this.type.getText();
		switch (type) {
		case "JSON" -> this.header("Content-Type", "application/json");
		case "TEXT" -> this.header("Content-Type", "application/text");
		case "FORM" -> this.header("Content-Type", "application/x-www-form-urlencoded;charset=" + this.charset.getText());
		}
	}
	
	/**
	 * 设置请求主体
	 * 
	 * @return 请求主体
	 */
	private String buildBody() {
		final String body = this.body.getText();
		final String type = this.type.getText();
		switch (type) {
		case "JSON":
			return body;
		case "TEXT":
			return body;
		case "FORM":
			final Map<String, String> map = this.json(body);
			if(MapUtils.isEmpty(map)) {
				return body;
			} else {
				return MapUtils.toUrl(map);
			}
		};
		return null;
	}
	
	/**
	 * 发送请求
	 * 
	 * @throws IOException IO异常
	 */
	private void request() throws IOException {
		final String body = this.buildBody();
		// 数据输出：包含主体、POST请求
		final boolean writeBody = Strings.isNotEmpty(body) && this.post();
		this.httpURLConnection.setRequestMethod(this.method.getText());
		if(writeBody) {
			this.httpURLConnection.setDoOutput(true);
		} else {
			this.httpURLConnection.setDoOutput(false);
		}
		this.httpURLConnection.connect();
		if(writeBody) {
			try (OutputStream output = this.httpURLConnection.getOutputStream()) {
				output.write(body.getBytes(this.charset.getText()));
			}
		}
	}
	
	/**
	 * 响应状态
	 * 
	 * @throws IOException IO异常
	 */
	private void responseStatus() throws IOException {
		this.status.setText(String.valueOf(this.httpURLConnection.getResponseCode()));
	}
	
	/**
	 * 响应头部
	 */
	private void responseHeader() {
		final var headers = this.httpURLConnection.getHeaderFields();
		final StringBuilder builder = new StringBuilder();
		headers.forEach((key, value) -> {
			if(key == null) {
				builder.append(String.join(",", value));
			} else if(value == null) {
				builder.append(key).append(": ");
			} else {
				builder.append(key).append(": ").append(String.join(",", value));
			}
			builder.append(Config.NEW_LINE);
		});
		this.response.setText(builder.toString());
	}
	
	/**
	 * 响应主体
	 * 
	 * @throws IOException IO异常
	 */
	private void responseBody() throws IOException {
		int length;
		try (InputStream input = this.httpURLConnection.getInputStream()) {
			final var bytes = new byte[1024];
			final var builder = new StringBuilder();
			builder.append(Config.NEW_LINE);
			while((length = input.read(bytes)) >= 0) {
				builder.append(new String(bytes, 0, length));
			}
			this.response.append(builder.toString());
		}
	}
	
	/**
	 * 设置请求头部
	 * 
	 * @param key 名称
	 * @param value 属性
	 */
	private void header(String key, String value) {
		this.httpURLConnection.setRequestProperty(key, value);
	}
	
	/**
	 * 读取JSON数据
	 * 
	 * @param value JSON数据
	 * 
	 * @return 数据
	 */
	private Map<String, String> json(String value) {
		boolean json = false;
		if(Strings.isEmpty(value)) {
			json = false;
		} else if(value.startsWith("{") && value.endsWith("}")) {
			// 支持MAP类型
			json = true;
		}
		if(!json) {
			return Map.of();
		}
		return this.gson.fromJson(value, new TypeToken<Map<String, String>>() {}.getType());
	}
	
	/**
	 * 读取行数据
	 * 
	 * @param value 行数据
	 * 
	 * @return 数据
	 */
	private Map<String, String> line(String value) {
		int index;
		final Map<String, String> map = new HashMap<>();
		final String[] lines = Strings.readLine(value);
		for (String line : lines) {
			index = line.indexOf(':');
			if(index > 0) {
				map.put(line.substring(0, index).trim(), line.substring(index + 1).trim());
			}
		}
		return map;
	}
	
	/**
	 * 创建请求地址
	 * 
	 * @param urlValue 请求地址
	 * 
	 * @return 请求地址
	 */
	private String buildUrl(String urlValue) {
		// 不是POST请求参数直接拼接
		if(!this.post()) {
			final String body = this.buildBody();
			if(urlValue.contains("?")) {
				urlValue = urlValue + "&" + body;
			} else {
				urlValue = urlValue + "?" + body;
			}
		}
		return urlValue;
	}
	
	/**
	 * 判断是否POST请求
	 * 
	 * @return 是否POST请求
	 */
	private boolean post() {
		return "POST".equals(this.method.getText());
	}
	
	/**
	 * 创建SSLContext
	 * 
	 * @return SSLContext
	 */
	private static final SSLContext buildSSLContext() {
		try {
			// SSL协议：SSL、SSLv2、SSLv3、TLS、TLSv1、TLSv1.1、TLSv1.2、TLSv1.3
			final SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
			sslContext.init(null, new X509TrustManager[] { SnailTrustManager.INSTANCE }, new SecureRandom());
			return sslContext;
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			Exceptions.error(HttpView.class, e);
		}
		try {
			return SSLContext.getDefault();
		} catch (NoSuchAlgorithmException e) {
			Exceptions.error(HttpView.class, e);
		}
		return null;
	}
	
	/**
	 * 域名验证
	 * 
	 * @author acgist
	 */
	public static class SnailHostnameVerifier implements HostnameVerifier {

		private static final SnailHostnameVerifier INSTANCE = new SnailHostnameVerifier();
		
		private SnailHostnameVerifier() {
		}
		
		@Override
		public boolean verify(String requestHost, SSLSession remoteSslSession) {
			return requestHost.equalsIgnoreCase(remoteSslSession.getPeerHost());
		}
		
	}
	
	/**
	 * 证书管理
	 * 
	 * @author acgist
	 */
	public static class SnailTrustManager implements X509TrustManager {

		private static final SnailTrustManager INSTANCE = new SnailTrustManager();

		private SnailTrustManager() {
		}
		
		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[0];
		}

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			if(chain == null) {
				throw new CertificateException(Internationalization.get(Internationalization.Message.CERTIFICATE_ERROR));
			}
		}
		
		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			if(chain == null) {
				throw new CertificateException(Internationalization.get(Internationalization.Message.CERTIFICATE_ERROR));
			}
		}
		
	}
	
}

package com.acgist.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.ProtocolException;
import org.apache.http.client.CircularRedirectException;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.RedirectLocations;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTP工具
 * 	复用注意：
 * 		连接池中的连接断开后，再次请求会发生什么问题？
 * 		高并发情况下请求没有响应，是否会出现同一个连接第二次请求情况？
 * org.apache.http.conn.ConnectionPoolTimeoutException: Timeout waiting for connection from pool
 * 		如果连接池过小，同时大量的请求生成时，会导致这个异常。
 * 服务器处理时间较长时复用效果并不是非常明显：
 * 		例如：每次请求服务处理时间为一秒，这时复用效果不明显。
 */
public class HTTPUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(HTTPUtils.class);
	
	private static CloseableHttpClient reuseClient; // 复用TCP连接，不能关闭response和client，工具自动管理，不同的域名会使用不同的TCP连接
	private static PoolingHttpClientConnectionManager manager; // 复用连接管理

	private static final boolean USE_REUSE_CLIENT = true; // 是否复用：true：复用；false：不复用
	
	private static final String HTTP_URL = "http://"; // HTTP请求
	private static final String HTTPS_URL = "https://"; // HTTPS请求
	private static final int DEFAULT_TIMEOUT = 30 * 1000; // 超时时间
	private static final String DEFAULT_CHARSET = "UTF-8"; // 默认编码
	private static final Map<String, String> BROWSER_HEADERS = new HashMap<String, String>(); // 浏览器参数
	
	static {
		BROWSER_HEADERS.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.89 Safari/537.36"); // 浏览器
	}

	/**
	 * 执行get请求
	 * 
	 * @param url 请求地址
	 * @return 请求返回内容
	 */
	public static final String get(String url) {
		return get(url, null, null, DEFAULT_TIMEOUT);
	}

	/**
	 * 执行get请求
	 * 
	 * @param url 请求地址
	 * @param timeout 超时时间
	 * @return 请求返回内容
	 */
	public static final String get(String url, int timeout) {
		return get(url, null, null, timeout);
	}

	/**
	 * 执行get请求
	 * 
	 * @param url 请求地址
	 * @param data 请求参数
	 * @return 请求返回内容
	 */
	public static final String get(String url, Map<String, Object> data) {
		return get(url, data, null, DEFAULT_TIMEOUT);
	}

	/**
	 * 执行get请求
	 * 
	 * @param url 请求
	 * @param data 请求参数
	 * @param headers 请求头
	 * @param timeout 超时时间
	 * @return 请求返回内容
	 */
	public static final String get(String url, Map<String, Object> data, Map<String, String> headers, int timeout) {
		url = buildURL(url, data);
		if (StringUtils.isEmpty(url)) {
			throw new IllegalArgumentException("错误的请求连接：" + url);
		}
		final HttpGet get = new HttpGet(url);
		get.setConfig(buildRequestConfig(timeout));
		final CloseableHttpClient client = buildClient(url);
		addHeaders(get, headers);
		return invoke(client, get);
	}
	
	/**
	 * 执行post请求
	 * 
	 * @param url 请求地址
	 * @return 请求返回内容
	 */
	public static final String post(String url) {
		return post(url, null, null, DEFAULT_TIMEOUT);
	}

	/**
	 * 执行post请求
	 * 
	 * @param url 请求地址
	 * @param data 请求参数
	 * @return 请求返回内容
	 */
	public static final String post(String url, Map<String, Object> data) {
		return post(url, data, null, DEFAULT_TIMEOUT);
	}

	/**
	 * 执行post请求
	 * 
	 * @param url 请求地址
	 * @param data 请求参数
	 * @param timeout 超时时间
	 * @return 请求返回内容
	 */
	public static final String post(String url, Map<String, Object> data, int timeout) {
		return post(url, data, null, timeout);
	}

	/**
	 * 执行post请求
	 * 
	 * @param url 请求地址
	 * @param data 请求参数
	 * @param headers 请求头
	 * @param timeout 超时时间
	 * @return 请求返回内容
	 */
	public static final String post(String url, Map<String, Object> data, Map<String, String> headers, int timeout) {
		url = buildURL(url, null);
		if (StringUtils.isEmpty(url)) {
			throw new IllegalArgumentException("错误的请求连接：" + url);
		}
		final HttpPost post = new HttpPost(url);
		post.setConfig(buildRequestConfig(timeout));
		final CloseableHttpClient client = buildClient(url);
		addHeaders(post, headers);
		buildPostParams(post, data);
		return invoke(client, post);
	}
	
	/**
	 * 提交JSON数据
	 */
	public static final String postJSON(String url, String json) {
		return postJSON(url, json, null, DEFAULT_TIMEOUT);
	}
	
	/**
	 * 提交JSON数据
	 */
	public static final String postJSON(String url, String json, Map<String, String> headers, int timeout) {
		url = buildURL(url, null);
		if (StringUtils.isEmpty(url)) {
			throw new IllegalArgumentException("错误的请求连接：" + url);
		}
		final HttpPost post = new HttpPost(url);
		post.setConfig(buildRequestConfig(timeout));
		final CloseableHttpClient client = buildClient(url);
		addHeaders(post, headers);
		buildPostParams(post, json);
		return invoke(client, post);
	}

	/**
	 * 执行请求
	 * 
	 * @param client client
	 * @param request 请求
	 * @return 请求返回内容
	 */
	private static final String invoke(CloseableHttpClient client, HttpUriRequest request) {
		String content = null;
		CloseableHttpResponse response = null;
		try {
			response = client.execute(request);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				LOGGER.error("HTTP返回异常，错误状态代码：{}", statusCode);
			}
			content = EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
			EntityUtils.consume(response.getEntity());
		} catch (IOException e) {
			LOGGER.error("HTTP请求异常", e);
		} catch (Exception e) {
			LOGGER.error("HTTP请求异常", e);
		} finally {
			close(client, response);
		}
		return content;
	}

	/**
	 * 添加请求头
	 * 
	 * @param request 请求
	 * @param headers 请求头
	 */
	private static final void addHeaders(HttpUriRequest request, Map<String, String> headers) {
		// 默认请求头
		Set<Map.Entry<String, String>> entrys = BROWSER_HEADERS.entrySet();
		for (Map.Entry<String, String> entry : entrys) {
			request.addHeader(entry.getKey(), entry.getValue());
		}
		// 自定义请求头
		if (headers != null) {
			entrys = headers.entrySet();
			for (Map.Entry<String, String> entry : entrys) {
				request.addHeader(entry.getKey(), entry.getValue());
			}
		}
	}
	
	/**
	 * 根据参数生成请求地址
	 * 
	 * @param url 原始请求地址
	 * @param data 请求参数
	 * @return 根据参赛生成的url
	 */
	private static final String buildURL(final String url, Map<String, Object> data) {
		if (StringUtils.isEmpty(url)) {
			return url;
		}
		final StringBuffer urlBuilder = new StringBuffer();
		// 设置请求协议
		if (!(url.startsWith(HTTPS_URL) || url.startsWith(HTTP_URL))) {
			urlBuilder.append(HTTPS_URL);
		}
		urlBuilder.append(url);
		// 设置请求参数
		if (MapUtils.isNotEmpty(data)) {
			final String queryString = buildQueryString(data);
			if (url.indexOf("?") == -1) {
				urlBuilder.append("?").append(queryString);
			} else if (url.endsWith("&")) {
				urlBuilder.append(queryString);
			} else if (url.endsWith("?")) {
				urlBuilder.append(queryString);
			} else {
				urlBuilder.append("&").append(queryString);
			}
		}
		return urlBuilder.toString();
	}
	
	/**
	 * 根据URL创建合适的client
	 * 
	 * @param url 请求地址
	 * @return client
	 */
	private static final CloseableHttpClient buildClient(final String url) {
		if (USE_REUSE_CLIENT) { // 复用client
			if (reuseClient == null) {
				synchronized (HTTPUtils.class) { // 不使用double check
					if(reuseClient != null) {
						return reuseClient;
					}
					final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
						.register("http", PlainConnectionSocketFactory.getSocketFactory())
//						.register("https", SSLConnectionSocketFactory.getSocketFactory())
						.register("https", buildSSLConnSocketFactory())
						.build();
					manager = new PoolingHttpClientConnectionManager(registry);
					// TODO 优化连接数量
					manager.setMaxTotal(2000); // 整个连接池的最大数量
					manager.setDefaultMaxPerRoute(1000); // 单个服务器的最大连接数量
					reuseClient = HttpClients.custom()
						.setRedirectStrategy(DefaultRedirectStrategy.getInstance())
						.setRetryHandler(new DefaultHttpRequestRetryHandler(0, false)) // 禁止重试
						.setConnectionManager(manager)
						.build();
				}
			}
			return reuseClient;
		} else { // 不复用
			final boolean https = url.startsWith(HTTPS_URL); // https连接
			if (https) {
				return HttpClients.custom()
					.setRedirectStrategy(DefaultRedirectStrategy.getInstance())
					.setSSLSocketFactory(buildSSLConnSocketFactory())
					.setRetryHandler(new DefaultHttpRequestRetryHandler(0, false)) // 禁止重试
					.build();
			}
			// http连接
			return HttpClients.custom()
				.setRedirectStrategy(DefaultRedirectStrategy.getInstance())
				.setRetryHandler(new DefaultHttpRequestRetryHandler(0, false)) // 禁止重试
				.build();
		}
	}

	/**
	 * 设置请求配置信息
	 */
	private static final RequestConfig buildRequestConfig(int timeout) {
		return RequestConfig.custom()
			.setConnectionRequestTimeout(timeout)
			.setConnectTimeout(timeout)
			.setSocketTimeout(timeout)
			.build();
	}

	/**
	 * 生成参数集合
	 * 
	 * @param data 数据
	 * @return 参数集合
	 */
	private static final List<NameValuePair> buildParams(Map<String, Object> data) {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		if (data == null) {
			return list;
		}
		Set<Map.Entry<String, Object>> entrys = data.entrySet();
		for (Map.Entry<String, Object> entry : entrys) {
			list.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
		}
		return list;
	}

	/**
	 * 设置请求参数
	 */
	private static final void buildPostParams(HttpPost post, Map<String, Object> data) {
		try {
			post.setEntity(new UrlEncodedFormEntity(buildParams(data), DEFAULT_CHARSET));
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("设置请求参数异常：{}", data, e);
		}
	}
	
	/**
	 * 设置请求参数
	 */
	private static final void buildPostParams(HttpPost post, String json) {
		if(json != null) {
			post.setEntity(new StringEntity(json, DEFAULT_CHARSET));
		}
	}
	
	/**
	 * 生成请求参数字符串
	 * 
	 * @param data 数据
	 * @return 参数字符串
	 */
	private static final String buildQueryString(Map<String, Object> data) {
		try {
			return EntityUtils.toString(new UrlEncodedFormEntity(buildParams(data), DEFAULT_CHARSET));
		} catch (ParseException | IOException e) {
			LOGGER.error("生成请求参数字符串异常：{}", data, e);
		}
		return null;
	}

	/**
	 * SSL工厂
	 */
	private static final SSLConnectionSocketFactory buildSSLConnSocketFactory() {
//		// 低版本
//		SSLContext sslContext = null;
//		SSLConnectionSocketFactory sslFactory = null;
//		try {
//			sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
//				// 信任所有证书
//				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//					return true;
//				}
//			}).build();
//		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
//			Logger.error(HTTPUtils.class, "创建SSL工程错误", e);
//		}
//		sslFactory = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {
//			// 证书和域名验证
//			@Override
//			public boolean verify(String host, SSLSession session) {
//				return true;
//			}
//			@Override
//			public void verify(String host, SSLSocket ssl) throws IOException {
//			}
//			@Override
//			public void verify(String host, X509Certificate cert) throws SSLException {
//			}
//			@Override
//			public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
//			}
//		});
//		return sslFactory;
		// 高版本
		SSLContext sslContext = null;
		SSLConnectionSocketFactory sslFactory = null;
		try {
			sslContext = SSLContextBuilder.create().loadTrustMaterial(null, new TrustStrategy() {
//			支持TLS
//			sslContext = SSLContextBuilder.create().useProtocol("TLSv1.2").loadTrustMaterial(null, new TrustStrategy() {
				// 信任所有证书
				@Override
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();
			sslFactory = new SSLConnectionSocketFactory(sslContext, new HostnameVerifier() {
//			支持TLS
//			sslFactory = new SSLConnectionSocketFactory(sslContext, new String[] {"TLSv1.2"}, null, new HostnameVerifier() {
				// 证书和域名验证
				@Override
				public boolean verify(String host, SSLSession session) {
					return true;
				}
			});
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
			LOGGER.error("创建SSL工程错误", e);
		}
		return sslFactory;
	}

	/**
	 * 关闭资源
	 * 
	 * @param client 客户端
	 * @param response 请求响应
	 */
	private static final void close(CloseableHttpClient client, CloseableHttpResponse response) {
		if (USE_REUSE_CLIENT) { // 复用TCP连接时直接返回不关闭
			return;
		}
		if (response != null) {
			try {
				response.close();
			} catch (IOException e) {
				response = null;
				LOGGER.error("关闭连接错误", e);
			}
		}
		if (client != null) {
			try {
				client.close();
			} catch (IOException e) {
				client = null;
				LOGGER.error("关闭连接错误", e);
			}
		}
	}

	/**
	 * 工具关闭
	 */
	public static final void shutdown() {
		if (reuseClient != null) {
			try {
				reuseClient.close();
			} catch (IOException e) {
				LOGGER.error("关闭连接错误", e);
			}
		}
		if (manager != null) {
			manager.close();
		}
		// 设置为null，如果继续使用时会重新创建新的client和manager
		reuseClient = null;
		manager = null;
	}

}

/**
 * 请求异常
 */
class RequestException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RequestException(String content) {
		super(content);
	}

}

/**
 * 重定向策略 307重定向只支持POST，303和302重定向支持所有协议
 */
class DefaultRedirectStrategy implements RedirectStrategy {

	private DefaultRedirectStrategy() {
	}

	private static final DefaultRedirectStrategy INSTANCE = new DefaultRedirectStrategy();

	public static final DefaultRedirectStrategy getInstance() {
		return INSTANCE;
	}

	@Override
	public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
		final String method = request.getRequestLine().getMethod();
		final int statusCode = response.getStatusLine().getStatusCode();
		switch (statusCode) {
			case HttpStatus.SC_TEMPORARY_REDIRECT:
				return HttpPost.METHOD_NAME.equalsIgnoreCase(method);
			case HttpStatus.SC_MOVED_TEMPORARILY:
			case HttpStatus.SC_SEE_OTHER:
				return true;
			default:
				return false;
		}
	}

	public URI getLocationURI(final HttpRequest request, final HttpResponse response, final HttpContext context) throws ProtocolException {
		final HttpClientContext clientContext = HttpClientContext.adapt(context);
		final Header locationHeader = response.getFirstHeader("location");
		if (locationHeader == null) {
			throw new ProtocolException("未返回重定向location首部");
		}
		final String location = locationHeader.getValue();
		final RequestConfig config = clientContext.getRequestConfig();
		URI uri = createLocationURI(location);
		try {
			if (!uri.isAbsolute()) {
				if (!config.isRelativeRedirectsAllowed()) {
					throw new ProtocolException("不允许重定向：" + uri);
				}
				final HttpHost target = clientContext.getTargetHost();
				final URI requestURI = new URI(request.getRequestLine().getUri());
				final URI absoluteRequestURI = URIUtils.rewriteURI(requestURI, target, false);
				uri = URIUtils.resolve(absoluteRequestURI, uri);
			}
		} catch (final URISyntaxException ex) {
			throw new ProtocolException(ex.getMessage(), ex);
		}
		RedirectLocations redirectLocations = (RedirectLocations) clientContext.getAttribute(HttpClientContext.REDIRECT_LOCATIONS);
		if (redirectLocations == null) {
			redirectLocations = new RedirectLocations();
			context.setAttribute(HttpClientContext.REDIRECT_LOCATIONS, redirectLocations);
		}
		if (!config.isCircularRedirectsAllowed()) {
			if (redirectLocations.contains(uri)) {
				throw new CircularRedirectException("无限循环重定向");
			}
		}
		redirectLocations.add(uri);
		return uri;
	}

	protected URI createLocationURI(final String location) throws ProtocolException {
		try {
			final URIBuilder builder = new URIBuilder(new URI(location).normalize());
			final String host = builder.getHost();
			if (host != null) {
				builder.setHost(host.toLowerCase(Locale.ROOT));
			}
			final String path = builder.getPath();
			if (TextUtils.isEmpty(path)) {
				builder.setPath("/");
			}
			return builder.build();
		} catch (final URISyntaxException ex) {
			throw new ProtocolException("非法的location：" + location, ex);
		}
	}

	@Override
	public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
		final URI uri = getLocationURI(request, response, context);
		final String method = request.getRequestLine().getMethod();
		if (HttpGet.METHOD_NAME.equalsIgnoreCase(method)) { // 忽略参数
			return new HttpGet(uri);
		} else {
			final int status = response.getStatusLine().getStatusCode();
			if (status == HttpStatus.SC_TEMPORARY_REDIRECT) {
				return RequestBuilder.copy(request).setUri(uri).build();
			} else {
				return new HttpGet(uri); // 忽略参数
			}
		}
	}

}

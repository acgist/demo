package com.acgist.html;

import java.io.IOException;
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
import org.apache.http.client.methods.HttpRequestBase;
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
import org.apache.http.impl.client.CloseableHttpClient;
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
 * HTTP工具 - HTTPClient 实现
 */
public class HTTPClientFoodie {

	private static CloseableHttpClient reuseClient; // 复用TCP连接，不能关闭response和client，工具自动管理，不同的域名会使用不同的TCP连接
	private static PoolingHttpClientConnectionManager manager; // 复用连接管理

	private static final boolean USE_REUSE_CLIENT = true; // 是否复用：true：复用；false：不复用
	private static final String HTTP_URL = "http://"; // HTTP请求
	private static final String HTTPS_URL = "https://"; // HTTPS请求
	private static final String DEFAULT_CHARSET = "UTF-8"; // 默认编码
	private static final String URL_REGEX = "(http://.*)|(https://.*)"; // URL正则表达式
	private static final Map<String, String> BROWSER_HEADERS = new HashMap<String, String>(); // 浏览器参数
	private static final int DEFAULT_TIMEOUT = 10 * 1000; // 超时时间
	private static final Logger LOGGER = LoggerFactory.getLogger(HTTPClientFoodie.class);
	
	static {
		BROWSER_HEADERS.put("User-Agent", "Mozilla/5.0 (Windows NT 10; Win64; x64) AppleWebKit/6666 (KHTML, like Gecko) Chrome/66.66.66 Safari/6666"); // 浏览器
	}

	/**
	 * 发起get请求
	 * 
	 * @param url url
	 * @return 请求返回内容
	 */
	public static final String get(String url) {
		return get(url, null, null, DEFAULT_TIMEOUT);
	}

	/**
	 * 发起get请求
	 * 
	 * @param url url
	 * @param timeout 超时时间
	 * @return 请求返回内容
	 */
	public static final String get(String url, int timeout) {
		return get(url, null, null, timeout);
	}

	/**
	 * 发起get请求
	 * 
	 * @param url url
	 * @param data 参数
	 * @return 请求返回内容
	 */
	public static final String get(String url, Map<String, Object> data) {
		return get(url, data, null, DEFAULT_TIMEOUT);
	}

	/**
	 * 发起get请求
	 * 
	 * @param url url
	 * @param data 参数
	 * @param headers 请求头
	 * @param timeout 超时时间
	 * @return 请求返回内容
	 */
	public static final String get(String url, Map<String, Object> data, Map<String, String> headers, int timeout) {
		String content = null;
		url = generateUrl(url, data);
		if (url == null) {
			return content;
		}
		HttpGet get = new HttpGet(url);
		get.setConfig(requestConfig(timeout));
		CloseableHttpClient client = generateClient(url);
		addHeaders(get, headers);
		try {
			content = invoke(client, get);
		} catch (Exception e) {
			LOGGER.error("HTTP GET请求错误，请求地址：" + url + "，请求参数：" + data, e);
		} finally {
			close(client, null);
		}
		return content;
	}

	/**
	 * 发起post请求
	 * 
	 * @param url url
	 * @return 请求返回内容
	 */
	public static final String post(String url) {
		return post(url, null, null, DEFAULT_TIMEOUT);
	}

	/**
	 * 发起post请求
	 * 
	 * @param url url
	 * @param data 参数
	 * @return 请求返回内容
	 */
	public static final String post(String url, Map<String, Object> data) {
		return post(url, data, null, DEFAULT_TIMEOUT);
	}

	/**
	 * 发起post请求
	 * 
	 * @param url url
	 * @param data 参数
	 * @param timeout 超时时间
	 * @return 请求返回内容
	 */
	public static final String post(String url, Map<String, Object> data, int timeout) {
		return post(url, data, null, timeout);
	}

	/**
	 * 发起post请求
	 * 
	 * @param url url
	 * @param data 参数
	 * @param headers 请求头
	 * @param timeout 超时时间
	 * @return 请求返回内容
	 */
	public static final String post(String url, Map<String, Object> data, Map<String, String> headers, int timeout) {
		String content = null;
		url = generateUrl(url, null);
		if (url == null) {
			return content;
		}
		HttpPost post = new HttpPost(url);
		post.setConfig(requestConfig(timeout));
		CloseableHttpClient client = generateClient(url);
		addHeaders(post, headers);
		try {
			post.setEntity(new UrlEncodedFormEntity(generateParams(data), DEFAULT_CHARSET));
			content = invoke(client, post);
		} catch (Exception e) {
			LOGGER.error("HTTP POST请求错误，请求地址：" + url + "，请求参数：" + data, e);
		} finally {
			close(client, null);
		}
		return content;
	}

	/**
	 * 设置请求信息
	 */
	private static final RequestConfig requestConfig(int timeout) {
		return RequestConfig.custom().setConnectTimeout(timeout).setConnectionRequestTimeout(timeout)
			.setSocketTimeout(timeout).build();
	}

	/**
	 * 执行请求
	 * 
	 * @param client client
	 * @param request 请求
	 * @return 返回内容
	 */
	private static final String invoke(CloseableHttpClient client, HttpUriRequest request) {
		String content = null;
		CloseableHttpResponse response = null;
		try {
			response = client.execute(request);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				content = EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
			} else {
				content = EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
				LOGGER.error("HTTP返回异常，错误状态代码：{}", statusCode);
			}
		} catch (IOException e) {
			LOGGER.error("HTTP请求异常", e);
		} catch (Exception e) {
			LOGGER.error("HTTP请求异常", e);
		} finally {
			close(null, response);
		}
		return content;
	}

	/**
	 * 根据参赛生成url
	 * 
	 * @param url 原始url
	 * @param data 参数
	 * @return 根据参赛生成的url
	 */
	private static final String generateUrl(String url, Map<String, Object> data) {
		if (url == null) {
			return url;
		}
		if (!url.matches(URL_REGEX)) {
			url = HTTP_URL + url;
		}
		if (data == null) {
			String params = generateParamsString(data);
			if (url.indexOf("?") == -1) {
				url = url + "?" + params;
			} else if (url.endsWith("&")) {
				url = url + params;
			} else if (url.endsWith("?")) {
				url = url + params;
			} else {
				url = url + "&" + params;
			}
		}
		return url;
	}

	/**
	 * 根据URL创建合适的client
	 * 
	 * @param url 请求地址
	 * @return client
	 */
	private static CloseableHttpClient generateClient(String url) {
		if (USE_REUSE_CLIENT) {
			if (reuseClient == null) {
				Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", PlainConnectionSocketFactory.getSocketFactory())
					.register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
				manager = new PoolingHttpClientConnectionManager(registry);
				return (reuseClient = HttpClients.custom().setRedirectStrategy(new AcgistRedirectStrategy())
					.setConnectionManager(manager).build());
			} else {
				return reuseClient;
			}
		} else {
			if (url != null && url.startsWith(HTTPS_URL)) { // https链接
				return HttpClients.custom().setRedirectStrategy(new AcgistRedirectStrategy())
					.setSSLSocketFactory(createSSLConnSocketFactory()).build();
			}
			return HttpClients.custom().setRedirectStrategy(new AcgistRedirectStrategy()).build(); // http链接
		}
	}

	/**
	 * 添加header
	 * 
	 * @param base 请求
	 */
	private static final void addHeaders(HttpRequestBase base, Map<String, String> headers) {
		Set<Map.Entry<String, String>> entrys = BROWSER_HEADERS.entrySet();
		for (Map.Entry<String, String> entry : entrys) {
			base.addHeader(entry.getKey(), entry.getValue());
		}
		if (headers != null) {
			entrys = headers.entrySet();
			for (Map.Entry<String, String> entry : entrys) {
				base.addHeader(entry.getKey(), entry.getValue());
			}
		}
	}

	/**
	 * 生成参数字符串
	 * 
	 * @param data 数据
	 * @return 参数字符串
	 */
	private static final String generateParamsString(Map<String, Object> data) {
		List<NameValuePair> list = generateParams(data);
		try {
			return EntityUtils.toString(new UrlEncodedFormEntity(list, DEFAULT_CHARSET));
		} catch (ParseException | IOException e) {
			LOGGER.error("组装参数错误，参数：" + data, e);
		}
		return null;
	}

	/**
	 * 生成参数集合
	 * 
	 * @param data 数据
	 * @return 参数集合
	 */
	private static final List<NameValuePair> generateParams(Map<String, Object> data) {
		List<NameValuePair> list = new ArrayList<>();
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
	 * SSL工厂
	 */
	private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
		SSLContext sslContext = null;
		SSLConnectionSocketFactory sslFactory = null;
		try {
			sslContext = SSLContextBuilder.create().loadTrustMaterial(null, new TrustStrategy() {
				// 信任所有证书
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();
			sslFactory = new SSLConnectionSocketFactory(sslContext, new HostnameVerifier() {
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
				reuseClient = null;
				LOGGER.error("关闭连接错误", e);
			}
		}
		if (manager != null) {
			manager.close();
		}
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
 * 重定向策略 307重定向只支持POST 303和302重定向支持所有协议
 */
class DefaultRedirectStrategy implements RedirectStrategy {

	private DefaultRedirectStrategy() {
	}

	private static final DefaultRedirectStrategy INSTANCE = new DefaultRedirectStrategy();

	public static final DefaultRedirectStrategy getInstance() {
		return INSTANCE;
	}

	@Override
	public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context)
			throws ProtocolException {
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

	public URI getLocationURI(final HttpRequest request, final HttpResponse response, final HttpContext context)
			throws ProtocolException {
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
		RedirectLocations redirectLocations = (RedirectLocations) clientContext
				.getAttribute(HttpClientContext.REDIRECT_LOCATIONS);
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
	public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context)
			throws ProtocolException {
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

package com.acgist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTP Client
 */
public class HttpClient {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpClient.class);

	private static final BaseX509TrustManager TRUST_MANAGER = new BaseX509TrustManager();
	
	/**
	 * 超时时间
	 */
	public static final int TIMEOUT = 10 * 1000;
	/**
	 * HTTPS协议
	 */
	public static final String HTTPS = "https";
	/**
	 * 默认编码格式
	 */
	public static final String ENCODING = "UTF-8";
	
	/**
	 * 请求地址
	 */
	private URL url;
	
	/**
	 * 请求方法
	 */
	private String method;
	
	/**
	 * 表单请求
	 */
	private boolean form;
	
	/**
	 * 编码格式
	 */
	private String encoding;

	/**
	 * 通信连接超时时间
	 */
	private int connectionTimeout;

	/**
	 * 通信读取超时时间
	 */
	private int readTimeout;

	/**
	 * 响应
	 */
	private String response;

	/**
	 * @param url 请求地址
	 * @param method 请求方法
	 */
	public HttpClient(String url, String method) {
		this(url, method, false, ENCODING, TIMEOUT, TIMEOUT);
	}
	
	/**
	 * @param url 请求地址
	 * @param method 请求方法
	 * @param form 表单请求
	 */
	public HttpClient(String url, String method, boolean form) {
		this(url, method, form, ENCODING, TIMEOUT, TIMEOUT);
	}
	
	/**
	 * @param url 请求地址
	 * @param method 请求方法
	 * @param form 表单请求
	 * @param encoding 编码格式
	 * @param connectionTimeout 连接超时时间
	 * @param readTimeout 读取超时时间
	 */
	public HttpClient(String url, String method, boolean form, String encoding, int connectionTimeout, int readTimeout) {
		try {
			this.url = new URL(url);
			this.method = method.toUpperCase();
			this.form = form;
			this.encoding = encoding;
			this.connectionTimeout = connectionTimeout;
			this.readTimeout = readTimeout;
		} catch (Exception e) {
			LOGGER.error("HTTP工具初始化异常", e);
		}
	}

	/**
	 * 发送请求
	 */
	public int request(Map<String, String> data) throws Exception {
		final HttpURLConnection httpURLConnection = buildConnection();
		try {
			this.request(httpURLConnection, buildQueryString(data));
			this.response = this.response(httpURLConnection);
			return httpURLConnection.getResponseCode();
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 发送请求
	 */
	public int request(String content) throws Exception {
		final HttpURLConnection httpURLConnection = buildConnection();
		try {
			this.request(httpURLConnection, content);
			this.response = this.response(httpURLConnection);
			return httpURLConnection.getResponseCode();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * HTTP Post发送消息
	 */
	private void request(final URLConnection connection, String queryString) throws Exception {
		PrintStream out = null;
		try {
			connection.connect();
			out = new PrintStream(connection.getOutputStream(), false, this.encoding);
			if(queryString != null) {
				out.print(queryString);
			}
			out.flush();
		} catch (Exception e) {
			throw e;
		} finally {
			if (null != out) {
				out.close();
			}
		}
	}
	
	/**
	 * 读取response
	 */
	private String response(final HttpURLConnection connection) throws Exception {
		String line = null;
		InputStream input = null;
		BufferedReader reader = null;
		StringBuilder builder = new StringBuilder();
		try {
			input = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(input, this.encoding));
			while (null != (line = reader.readLine())) {
				builder.append(line);
			}
			return builder.toString();
		} catch (Exception e) {
			throw e;
		} finally {
			if (null != reader) {
				reader.close();
			}
			if (null != input) {
				input.close();
			}
			if (null != connection) {
				connection.disconnect();
			}
		}
	}

	/**
	 * 创建连接
	 */
	private HttpURLConnection buildConnection() throws IOException {
		final HttpURLConnection httpURLConnection = (HttpURLConnection) this.url.openConnection();
		httpURLConnection.setConnectTimeout(this.connectionTimeout); // 连接超时时间
		httpURLConnection.setReadTimeout(this.readTimeout); // 读取结果超时时间
		httpURLConnection.setDoInput(true); // 可读
		httpURLConnection.setDoOutput(true); // 可写
		httpURLConnection.setUseCaches(false); // 取消缓存
		httpURLConnection.setRequestProperty("User-Agent", "acgist"); // 标识
		if(this.form) {
			httpURLConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=" + this.encoding); // 表单请求
		}
		httpURLConnection.setRequestMethod(this.method);
		if (HTTPS.equalsIgnoreCase(this.url.getProtocol())) {
			final HttpsURLConnection httpsURLConnection = (HttpsURLConnection) httpURLConnection;
			httpsURLConnection.setSSLSocketFactory(new BaseHttpSSLSocketFactory());
			httpsURLConnection.setHostnameVerifier(new BaseTrustAnyHostnameVerifier());
			return httpsURLConnection;
		}
		return httpURLConnection;
	}

	/**
	 * 请求参数序列化
	 */
	private String buildQueryString(Map<String, String> querys) throws UnsupportedEncodingException {
		StringBuffer queryString = new StringBuffer();
		if (null != querys && !querys.isEmpty()) {
			for (Entry<String, String> entry : querys.entrySet()) {
				queryString.append(entry.getKey()).append("=").append(encoding(entry.getValue())).append("&");
			}
			return queryString.substring(0, queryString.length() - 1);
		} else {
			return null;
		}
	}
	
	/**
	 * URL编码
	 */
	private String encoding(String value) {
		if(value == null) {
			return "";
		}
		try {
			return URLEncoder.encode(value, this.encoding);
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("URL编码异常：{}-{}", value, this.encoding, e);
		}
		return "";
	}
	
	/**
	 * 获取响应结果
	 */
	public String response() {
		return response;
	}
	
	/**
	 * SSLSocket工厂
	 */
	public static class BaseHttpSSLSocketFactory extends SSLSocketFactory {
		
		private SSLContext getSSLContext() {
			return buildSSLContext();
		}
		
		@Override
		public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
			return getSSLContext().getSocketFactory().createSocket(address, port, localAddress, localPort);
		}

		@Override
		public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
			return getSSLContext().getSocketFactory().createSocket(host, port, localHost, localPort);
		}

		@Override
		public Socket createSocket(InetAddress address, int port) throws IOException {
			return getSSLContext().getSocketFactory().createSocket(address, port);
		}

		@Override
		public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
			return getSSLContext().getSocketFactory().createSocket(host, port);
		}

		@Override
		public String[] getSupportedCipherSuites() {
			return null;
		}

		@Override
		public String[] getDefaultCipherSuites() {
			return null;
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
			return getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
		}

		private SSLContext buildSSLContext() {
			try {
				final SSLContext context = SSLContext.getInstance("TLSv1.2");
				context.init(null, new TrustManager[] { TRUST_MANAGER }, null);
				return context;
			} catch (Exception e) {
				LOGGER.error("创建SSL工厂异常", e);
				return null;
			}
		}
		
	}
	
	/**
	 * 证书管理器
	 */
	public static class BaseX509TrustManager implements X509TrustManager {
		
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
		
		public void checkClientTrusted(X509Certificate[] chain, String authType) {
		}
		
		public void checkServerTrusted(X509Certificate[] chain, String authType) {
		}
		
	}
	
	/**
	 * 解决由于服务器证书问题导致HTTPS无法访问的情况
	 */
	public static class BaseTrustAnyHostnameVerifier implements HostnameVerifier {
		
		public boolean verify(String hostname, SSLSession session) {
			return true; // 直接返回true
		}
		
	}

}

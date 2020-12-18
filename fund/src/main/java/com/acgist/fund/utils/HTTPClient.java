package com.acgist.fund.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse.BodyHandlers;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>HTTP工具</p>
 * 
 * @author acgist
 */
public class HTTPClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(HTTPClient.class);
	
	/**
	 * <p>HTTP客户端信息（User-Agent）</p>
	 */
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/666.666 (KHTML, like Gecko) Chrome/88.8888.88 Safari/666.666";
	
	private static final Executor EXECUTOR = Executors.newCachedThreadPool();
	
	/**
	 * <p>执行GET请求</p>
	 * 
	 * @param url 请求地址
	 * 
	 * @return 响应信息
	 */
	public static final String get(String url) {
		return get(url, null);
	}
	
	/**
	 * <p>执行GET请求</p>
	 * 
	 * @param url 请求地址
	 * @param referer 刷新地址
	 * 
	 * @return 响应信息
	 */
	public static final String get(String url, String referer) {
		final var client = newClient(30);
		final var builder = newBuilder(url, referer, 60);
		try {
			return client.send(builder.GET().build(), BodyHandlers.ofString()).body();
		} catch (IOException | InterruptedException e) {
			LOGGER.error("请求异常", e);
		}
		return null;
	}
	
	/**
	 * <p>新建原生HTTP客户端</p>
	 * <p>设置{@code SSLContext}需要同时设置{@code SSLParameters}</p>
	 * 
	 * @param timeout 超时时间（连接），单位：秒
	 * 
	 * @return 原生HTTP客户端
	 */
	public static final HttpClient newClient(int timeout) {
		return HttpClient
			.newBuilder()
			.executor(EXECUTOR) // 线程池
			.version(Version.HTTP_1_1) // 协议版本
			.followRedirects(Redirect.NORMAL) // 重定向：正常
//			.followRedirects(Redirect.ALWAYS) // 重定向：全部
//			.proxy(ProxySelector.getDefault()) // 代理
//			.sslContext(newSSLContext()) // SSL上下文
			.sslParameters(newSSLParameters()) // SSL参数
//			.authenticator(Authenticator.getDefault()) // 认证
//			.cookieHandler(CookieHandler.getDefault()) // Cookie
			.connectTimeout(Duration.ofSeconds(timeout)) // 超时
			.build();
	}

	/**
	 * <p>新建请求Builder</p>
	 * 
	 * @param url 请求地址
	 * @param referer 刷新地址
	 * @param timeout 超时时间（响应），单位：秒
	 * 
	 * @return 请求Builder
	 */
	public static final Builder newBuilder(String url, String referer, int timeout) {
		final var builder = HttpRequest
			.newBuilder()
			.uri(URI.create(url))
			.version(Version.HTTP_1_1) // HTTP协议使用1.1版本：2.0版本没有普及
			.timeout(Duration.ofSeconds(timeout))
			.header("User-Agent", USER_AGENT);
		if(StringUtils.isNotEmpty(referer)) {
			builder.setHeader("Referer", referer);
		}
		return builder;
	}
	
	/**
	 * <p>新建{@code SSLParameters}</p>
	 * 
	 * @return {@code SSLParameters}
	 */
	public static final SSLParameters newSSLParameters() {
		final var sslParameters = new SSLParameters();
		// SSL加密套件：RSA和ECDSA签名根据证书类型选择（ECDH不推荐使用）
//		sslParameters.setCipherSuites(new String[] {
//			"TLS_AES_128_GCM_SHA256",
//			"TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256",
//			"TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256",
//			"TLS_RSA_WITH_AES_128_CBC_SHA256",
//			"TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256",
//			"TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256"
//		});
		// 不使用TLSv1.3：CPU占用过高
		sslParameters.setProtocols(new String[] {"TLSv1.1", "TLSv1.2"});
		// HTTP协议配置：newBuilder
//		sslParameters.setApplicationProtocols(new String[] {"h2", "http/1.1"});
		return sslParameters;
	}
	
	/**
	 * <p>新建{@code SSLContext}</p>
	 * 
	 * @return {@code SSLContext}
	 */
	public static final SSLContext newSSLContext() {
		SSLContext sslContext = null;
		try {
			// SSL协议：SSL、SSLv2、SSLv3、TLS、TLSv1、TLSv1.1、TLSv1.2、TLSv1.3
			sslContext = SSLContext.getInstance("TLSv1.2");
			sslContext.init(null, ALLOWED_ALL_TRUST_MANAGER, new SecureRandom());
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			LOGGER.error("新建SSLContext异常", e);
			try {
				sslContext = SSLContext.getDefault();
			} catch (NoSuchAlgorithmException ex) {
				LOGGER.error("新建默认SSLContext异常", ex);
			}
		}
		return sslContext;
	}
	
	/**
	 * <p>信任所有证书</p>
	 */
	private static final TrustManager[] ALLOWED_ALL_TRUST_MANAGER = new TrustManager[] {
		new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) {
				// 如果不信任证书抛出异常：CertificateException
			}
			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) {
				// 如果不信任证书抛出异常：CertificateException
			}
			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		}
	};
	
}

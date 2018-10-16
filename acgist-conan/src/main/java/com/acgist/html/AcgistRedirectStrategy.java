package com.acgist.html;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolException;
import org.apache.http.client.CircularRedirectException;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.RedirectLocations;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.TextUtils;

/**
 * 重定向策略
 * 307重定向请求，使用原请求方法，POST携带POST参数
 * 303/302/301重定向到GET请求，POST请求将不携带POST参数
 */
public class AcgistRedirectStrategy implements RedirectStrategy {

	@Override
	public boolean isRedirected(final HttpRequest request, final HttpResponse response, final HttpContext context) throws ProtocolException {
//		final String method = request.getRequestLine().getMethod();
		final int statusCode = response.getStatusLine().getStatusCode();
		switch (statusCode) {
			case HttpStatus.SC_TEMPORARY_REDIRECT:
			case HttpStatus.SC_MOVED_PERMANENTLY:
			case HttpStatus.SC_MOVED_TEMPORARILY:
			case HttpStatus.SC_SEE_OTHER:
				return true;
			default:
				return false;
		}
	}

	@Override
	public HttpUriRequest getRedirect(final HttpRequest request, final HttpResponse response, final HttpContext context) throws ProtocolException {
		final URI uri = getLocationURI(request, response, context);
//		final String method = request.getRequestLine().getMethod();
		final int status = response.getStatusLine().getStatusCode();
		if (status == HttpStatus.SC_TEMPORARY_REDIRECT) { // 307使用原始request
			return RequestBuilder.copy(request).setUri(uri).build();
		} else { // 其他直接转换为GET请求，参数使用返回的location携带，原请求参数将会丢失
			return new HttpGet(uri);
		}
	}
	
	private URI getLocationURI(final HttpRequest request, final HttpResponse response, final HttpContext context) throws ProtocolException {
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
		} catch (final URISyntaxException e) {
			throw new ProtocolException(e.getMessage(), e);
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

	private URI createLocationURI(final String location) throws ProtocolException {
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
		} catch (final URISyntaxException e) {
			throw new ProtocolException("非法的location：" + location, e);
		}
	}
	
}

package com.acgist.bean;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 搜索引擎枚举
 */
public enum Source {
	
	baidu, // 百度
	haosou; // 360
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Source.class);
	
	private String searchUrl; // 搜索地址
	private String pageQuery; // 分页选择器
	private String linkQuery; // 页面选择器

	public String getSearchUrl() {
		return searchUrl;
	}

	public void setSearchUrl(String searchUrl) {
		this.searchUrl = searchUrl;
	}

	public String getPageQuery() {
		return pageQuery;
	}

	public void setPageQuery(String pageQuery) {
		this.pageQuery = pageQuery;
	}

	public String getLinkQuery() {
		return linkQuery;
	}

	public void setLinkQuery(String linkQuery) {
		this.linkQuery = linkQuery;
	}

	public String searchUrl(List<String> keys) {
		return searchUrl.replace("${word}", buildWord(keys));
	}
	
	private String buildWord(List<String> keys) {
		StringBuffer query = new StringBuffer();
		keys.forEach(key -> {
			query.append("\"").append(key).append("\"").append(" ");
		});
		query.setLength(query.length() - 1);
		return urlEncoding(query.toString());
	}
	
	private String urlEncoding(String value) {
		try {
			return URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("URL编码异常：{}", value, e);
		}
		return value;
	}
	
}

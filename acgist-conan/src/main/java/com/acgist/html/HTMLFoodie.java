package com.acgist.html;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

/**
 * 解析HTML
 */
public class HTMLFoodie {

	private URI uri;
	private String html;
	private Document document;
	
	private HTMLFoodie() {
	}
	
	/**
	 * 根据链接获取HTML内容
	 */
	public static final HTMLFoodie html(String uri) {
		HTMLFoodie foodie = new HTMLFoodie();
		foodie.uri = URI.create(uri);
//		foodie.html = HTTPFoodie.instance().get(uri);
		foodie.html = HTTPClientFoodie.get(uri);
		if(foodie.html == null) {
			foodie.html = "";
		}
		foodie.document = Jsoup.parse(foodie.html);
		return foodie;
	}

	/**
	 * 根据HTML获取文本内容
	 */
	public String text() {
		return Jsoup.clean(this.html, Whitelist.none());
	}

	/**
	 * 根据CSS选择器获取链接
	 */
	public List<String> queryHref(String cssQuery) {
		if(cssQuery == null || cssQuery.isEmpty()) {
			return new ArrayList<>();
		}
		Elements elements = this.document.select(cssQuery);
		if(elements.isEmpty()) {
			return new ArrayList<>();
		}
		return elements.stream().map((element) -> {
			return this.uri.resolve(element.attr("href")).toString();
		}).collect(Collectors.toList());
	}
	
}

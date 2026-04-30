package com.api.core.baize.service.impl;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.core.baize.pojo.message.Classify;
import com.api.core.baize.pojo.message.Sentiment;
import com.api.core.baize.pojo.message.Similarity;
import com.api.core.baize.pojo.message.TokenCount;
import com.api.core.baize.pojo.message.TokenSegment;
import com.api.core.baize.pojo.message.WebPage;
import com.api.core.baize.pojo.query.WebQuery;
import com.api.core.baize.service.IWebPageService;
import com.api.utils.HTTPUtils;

@Service
public class WebPageService implements IWebPageService {

	@Autowired
	private TextService textService;
	
	@Override
	public WebPage webPage(WebQuery query) {
		final WebPage page = new WebPage();
		final String html = HTTPUtils.get(query.uri());
		page.setHtml(html);
		page.setContent(Jsoup.clean(html, Whitelist.none()));
		return page;
	}

	@Override
	public TokenSegment tokenSegment(WebQuery query) {
		WebPage page = webPage(query);
		return textService.tokenSegment(page.getContent());
	}

	@Override
	public TokenCount tokenCount(WebQuery query) {
		WebPage page = webPage(query);
		return textService.tokenCount(page.getContent());
	}

	@Override
	public Sentiment sentiment(WebQuery query) {
		WebPage page = webPage(query);
		return textService.sentiment(page.getContent());
	}

	@Override
	public Classify classify(WebQuery query) {
		WebPage page = webPage(query);
		return textService.classify(page.getContent());
	}

	@Override
	public Similarity similarity(WebQuery source, WebQuery target) {
		WebPage sourcePage = webPage(source);
		WebPage targetPage = webPage(source);
		return textService.similarity(sourcePage.getContent(), targetPage.getContent());
	}

}

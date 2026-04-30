package com.api.core.baize.service.impl;

import org.springframework.stereotype.Service;

import com.api.core.baize.pojo.message.Classify;
import com.api.core.baize.pojo.message.Sentiment;
import com.api.core.baize.pojo.message.Similarity;
import com.api.core.baize.pojo.message.TokenCount;
import com.api.core.baize.pojo.message.TokenSegment;
import com.api.core.baize.service.ITextService;

@Service
public class TextService implements ITextService {

	@Override
	public TokenSegment tokenSegment(String content) {
		return null;
	}

	@Override
	public TokenCount tokenCount(String content) {
		return null;
	}

	@Override
	public Sentiment sentiment(String content) {
		return null;
	}

	@Override
	public Classify classify(String content) {
		return null;
	}

	@Override
	public Similarity similarity(String source, String target) {
		return null;
	}

}

package com.api.core.baize.service.impl;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.core.baize.pojo.message.Classify;
import com.api.core.baize.pojo.message.Sentiment;
import com.api.core.baize.pojo.message.Similarity;
import com.api.core.baize.pojo.message.TokenCount;
import com.api.core.baize.pojo.message.TokenSegment;
import com.api.core.baize.service.IFileService;
import com.api.core.baize.service.ITextService;

@Service
public class FileService implements IFileService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);
	
	@Autowired
	private ITextService textService;
	
	@Override
	public TokenSegment tokenSegment(String filePath) {
		return textService.tokenSegment(loadContent(filePath));
	}

	@Override
	public TokenCount tokenCount(String filePath) {
		return textService.tokenCount(loadContent(filePath));
	}

	@Override
	public Sentiment sentiment(String filePath) {
		return textService.sentiment(loadContent(filePath));
	}

	@Override
	public Classify classify(String filePath) {
		return textService.classify(loadContent(filePath));
	}

	@Override
	public Similarity similarity(String sourcePath, String targetPath) {
		return textService.similarity(loadContent(sourcePath), loadContent(targetPath));
	}

	private String loadContent(String path) {
		try {
			return Files.readString(Paths.get(URI.create(path)));
		} catch (IOException e) {
			LOGGER.error("读取文件异常", e);
		}
		return null;
	}
	
}

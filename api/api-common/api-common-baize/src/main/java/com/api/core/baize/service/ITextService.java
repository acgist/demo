package com.api.core.baize.service;

import com.api.core.baize.pojo.message.Classify;
import com.api.core.baize.pojo.message.Sentiment;
import com.api.core.baize.pojo.message.Similarity;
import com.api.core.baize.pojo.message.TokenCount;
import com.api.core.baize.pojo.message.TokenSegment;

/**
 * 服务 - 文本分析
 */
public interface ITextService extends IBaizeService {

	/**
	 * 分词
	 * @param content 文本内容
	 * @return 分词结果
	 */
	TokenSegment tokenSegment(String content);
	
	/**
	 * 词语统计
	 * @param content 文本内容
	 * @return 统计结果
	 */
	TokenCount tokenCount(String content);
	
	/**
	 * 情感分析
	 * @param content 文本内容
	 * @return 分析结果
	 */
	Sentiment sentiment(String content);
	
	/**
	 * 文章分类
	 * @param content 文本内容
	 * @return 文章分类
	 */
	Classify classify(String content);
	
	/**
	 * 相似度识别
	 * @param source 原始文本
	 * @param target 比较文本
	 * @return 相似度
	 */
	Similarity similarity(String source, String target);
	
}

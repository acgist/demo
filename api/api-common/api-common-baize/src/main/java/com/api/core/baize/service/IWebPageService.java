package com.api.core.baize.service;

import com.api.core.baize.pojo.message.Classify;
import com.api.core.baize.pojo.message.Sentiment;
import com.api.core.baize.pojo.message.Similarity;
import com.api.core.baize.pojo.message.TokenCount;
import com.api.core.baize.pojo.message.TokenSegment;
import com.api.core.baize.pojo.message.WebPage;
import com.api.core.baize.pojo.query.WebQuery;

/**
 * 服务 - 网页分析<br>
 * 单页分析
 */
public interface IWebPageService extends IBaizeService {

	/**
	 * 页面抓取
	 * @param query 页面查询器
	 * @return 页面内容
	 */
	WebPage webPage(WebQuery query);
	
	/**
	 * 分词
	 * @param query 页面查询器
	 * @return 分词结果
	 */
	TokenSegment tokenSegment(WebQuery query);
	
	/**
	 * 词语统计
	 * @param query 页面查询器
	 * @return 统计结果
	 */
	TokenCount tokenCount(WebQuery query);
	
	/**
	 * 情感分析
	 * @param query 页面查询器
	 * @return 分析结果
	 */
	Sentiment sentiment(WebQuery query);
	
	/**
	 * 文章分类
	 * @param query 页面查询器
	 * @return 文章分类
	 */
	Classify classify(WebQuery query);
	
	/**
	 * 相似度识别
	 * @param source 原始页面查询器
	 * @param target 比较页面查询器
	 * @return 相似度
	 */
	Similarity similarity(WebQuery source, WebQuery target);
	
}

package com.api.core.baize.service;

import com.api.core.baize.pojo.message.Classify;
import com.api.core.baize.pojo.message.Sentiment;
import com.api.core.baize.pojo.message.Similarity;
import com.api.core.baize.pojo.message.TokenCount;
import com.api.core.baize.pojo.message.TokenSegment;

/**
 * 服务 - 文件分析<br>
 * 文件、文件夹内容分析
 */
public interface IFileService extends IBaizeService {

	/**
	 * 分词
	 * @param filePath 文件路径
	 * @return 分词结果
	 */
	TokenSegment tokenSegment(String filePath);
	
	/**
	 * 词语统计
	 * @param filePath 文件路径
	 * @return 统计结果
	 */
	TokenCount tokenCount(String filePath);
	
	/**
	 * 情感分析
	 * @param filePath 文件路径
	 * @return 分析结果
	 */
	Sentiment sentiment(String filePath);
	
	/**
	 * 文章分类
	 * @param filePath 文件路径
	 * @return 文章分类
	 */
	Classify classify(String filePath);
	
	/**
	 * 相似度识别
	 * @param sourcePath 原始文件路径
	 * @param targetPath 比较文件路径
	 * @return 相似度
	 */
	Similarity similarity(String sourcePath, String targetPath);
	
}

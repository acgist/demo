package com.api.core.baize.service;

import com.api.core.baize.pojo.message.TokenCount;
import com.api.core.baize.pojo.query.WebQuery;

/**
 * 服务 - 网站分析<br>
 * 网站分析：整个站点分析
 */
public interface IWebSiteService extends IBaizeService {

	/**
	 * 词语统计
	 * @param query 页面查询器
	 * @return 统计结果
	 */
	TokenCount tokenCount(WebQuery query);
	
}

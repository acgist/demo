package com.acgist.config;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.acgist.bean.Source;

/**
 * source-config.properties：搜索引擎配置：搜索配置：分页配置：链接配置
 */
@Configuration
public class SourceConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(SourceConfig.class);
	
	@Bean
	public PropertiesFactoryBean sourceConfigProperties() throws IOException {
		PropertiesFactoryBean properties = new PropertiesFactoryBean();
		properties.setLocation(new ClassPathResource("/config/source-config.properties"));
		properties.afterPropertiesSet();
		init(properties.getObject());
		return properties;
	}
	
	public void init(Properties properties) {
		Source.baidu.setSearchUrl(properties.getProperty("baidu.search.url"));
		Source.baidu.setPageQuery(properties.getProperty("baidu.page.query"));
		Source.baidu.setLinkQuery(properties.getProperty("baidu.link.query"));
		Source.haosou.setSearchUrl(properties.getProperty("haosou.search.url"));
		Source.haosou.setPageQuery(properties.getProperty("haosou.page.query"));
		Source.haosou.setLinkQuery(properties.getProperty("haosou.link.query"));
		for (Source source : Source.values()) {
			if(
				source.getSearchUrl() == null || source.getSearchUrl().isEmpty() ||
				source.getPageQuery() == null || source.getPageQuery().isEmpty() ||
				source.getLinkQuery() == null || source.getLinkQuery().isEmpty()
			) {
				LOGGER.warn("搜索引擎{}没有配置信息，请在{}配置相关信息。", source, "/config/source-config.properties");
			}
		}
	}

}

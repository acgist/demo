package com.api.data.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * config - C3P0数据库连接池
 */
@Configuration
//@PropertySource(value = "classpath:/c3p0.properties")
public class C3P0DataSource {

	private static final Logger LOGGER = LoggerFactory.getLogger(C3P0DataSource.class);
	
	@Primary
	@Bean(name = "dataSource")
	@Qualifier(value = "dataSource")
	@ConfigurationProperties(prefix = "c3p0")
	public DataSource dataSource() {
		LOGGER.info("初始化C3P0连接池");
		return DataSourceBuilder.create().type(com.mchange.v2.c3p0.ComboPooledDataSource.class).build();
	}
	
}
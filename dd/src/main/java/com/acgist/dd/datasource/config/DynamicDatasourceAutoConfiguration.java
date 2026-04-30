package com.acgist.dd.datasource.config;

import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.acgist.dd.datasource.DynamicDatasource;
import com.acgist.dd.datasource.DynamicDatasourceImpl;
import com.acgist.dd.datasource.service.DynamicDatasourceService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

/**
 * 动态数据库配置
 * 
 * @author acgist
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({
    DynamicDatasourceProperties.class
})
public class DynamicDatasourceAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    @ConditionalOnMissingBean
    public HikariConfig hikariConfig() {
        return new HikariConfig();
    }
    
    @Bean
    @Primary
    public DynamicDatasource dynamicDatasource(DataSourceProperties dataSourceProperties) {
        log.info("配置动态数据源");
        final HikariDataSource dataSource = dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
        final DynamicDatasource dynamicDatasource = new DynamicDatasourceImpl();
        final Map<Object, Object> datasources = dynamicDatasource.getDatasources();
        datasources.put(DynamicDatasource.SYSTEM, dataSource);
        dynamicDatasource.setTargetDataSources(datasources);
        dynamicDatasource.setDefaultTargetDataSource(dataSource);
        return dynamicDatasource;
    }
    
    @Bean
    public CommandLineRunner dynamicDatasourceServiceCommandLineRunner(
        HikariConfig hikariConfig,
        DataSourceProperties dataSourceProperties,
        DynamicDatasourceService dynamicDatasourceService,
        DynamicDatasourceProperties dynamicDatasourceProperties
    ) {
        return new CommandLineRunner() {
            @Override
            public void run(String ... args) throws Exception {
            	log.info("加载动态数据源");
                dynamicDatasourceService.loadDatasource(hikariConfig, dataSourceProperties, dynamicDatasourceProperties);
            }
        };
    }

}

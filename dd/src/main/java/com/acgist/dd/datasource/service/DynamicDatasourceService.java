package com.acgist.dd.datasource.service;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import com.acgist.dd.datasource.config.DynamicDatasourceProperties;
import com.zaxxer.hikari.HikariConfig;

/**
 * 动态数据源管理
 * 
 * @author acgist
 */
public interface DynamicDatasourceService {

    /**
     * 创建动态数据源
     * 
     * @param dataId dataId
     * @param config 数据源配置
     */
    void createDatasource(String dataId, HikariConfig config);
    
    /**
     * 删除动态数据源
     * 
     * @param dataId dataId
     */
    void removeDatasource(String dataId);
    
    /**
     * 加载动态数据源
     * 
     * @param hikariConfig                默认连接池
     * @param dataSourceProperties        默认数据源
     * @param dynamicDatasourceProperties 动态数据源
     */
    void loadDatasource(HikariConfig hikariConfig, DataSourceProperties dataSourceProperties, DynamicDatasourceProperties dynamicDatasourceProperties);
    
}

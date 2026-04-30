package com.acgist.dd.datasource.service.impl;

import java.util.Map;
import java.util.Objects;

import javax.sql.DataSource;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Service;

import com.acgist.dd.datasource.DynamicDatasource;
import com.acgist.dd.datasource.config.DynamicDatasourceProperties;
import com.acgist.dd.datasource.service.DynamicDatasourceService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DynamicDatasourceServiceImpl implements DynamicDatasourceService {

    @Autowired
    private DynamicDatasource dynamicDatasource;
    
    @Override
    public void createDatasource(String dataId, HikariConfig config) {
        log.info("添加动态数据源：{} - {}", dataId, config.getJdbcUrl());
        // 配置连接池
        final HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(config.getJdbcUrl());
        dataSource.setUsername(config.getUsername());
        dataSource.setPassword(config.getPassword());
        dataSource.setMaxLifetime(config.getMaxLifetime());
        dataSource.setMinimumIdle(config.getMinimumIdle());
        dataSource.setMaximumPoolSize(config.getMaximumPoolSize());
        dataSource.setDriverClassName(config.getDriverClassName());
        // 加入数据源
        final Map<Object, Object> datasources = this.dynamicDatasource.getDatasources();
        final DataSource old = (DataSource) datasources.put(dataId, dataSource);
        // 判断旧数据源
        if(old != null) {
            log.info("替换动态数据源：{}", dataId);
            if(old instanceof HikariDataSource oldDatasource) {
                oldDatasource.close();
            }
        }
        this.dynamicDatasource.setTargetDataSources(datasources);
        this.dynamicDatasource.afterPropertiesSet();
    }
    
    @Override
    public void removeDatasource(String dataId) {
        log.info("删除动态数据源：{}", dataId);
        final Map<Object, Object> datasources = this.dynamicDatasource.getDatasources();
        final DataSource old = (DataSource) datasources.remove(dataId);
        if(old != null) {
            log.debug("删除动态数据源成功：{}", dataId);
            if(old instanceof HikariDataSource oldDatasource) {
                oldDatasource.close();
            }
            this.dynamicDatasource.setTargetDataSources(datasources);
            this.dynamicDatasource.afterPropertiesSet();
        } else {
            log.debug("删除动态数据源为空：{}", dataId);
        }
    }
    
    @Override
    public void loadDatasource(HikariConfig hikariConfig, DataSourceProperties dataSourceProperties, DynamicDatasourceProperties dynamicDatasourceProperties) {
        if(MapUtils.isEmpty(dynamicDatasourceProperties.getDatasource())) {
            return;
        }
        dynamicDatasourceProperties.getDatasource().forEach((k, v) -> {
            this.loadDatasource(k, v, hikariConfig, dataSourceProperties);
        });
    }
    
    private void loadDatasource(String dataId, HikariConfig config, HikariConfig hikariConfig, DataSourceProperties dataSourceProperties) {
        if(StringUtils.isEmpty(config.getJdbcUrl())) {
            throw new IllegalArgumentException("动态数据源没有配置JDBC URL：" + dataId);
        }
        config.setUsername(Objects.requireNonNullElse(config.getUsername(), dataSourceProperties.getUsername()));
        config.setPassword(Objects.requireNonNullElse(config.getPassword(), dataSourceProperties.getPassword()));
        config.setMinimumIdle(Math.max(config.getMinimumIdle(), hikariConfig.getMinimumIdle()));
        config.setMaxLifetime(Math.max(config.getMaxLifetime(), hikariConfig.getMaxLifetime()));
        config.setMaximumPoolSize(Math.max(config.getMaximumPoolSize(), hikariConfig.getMaximumPoolSize()));
        config.setDriverClassName(Objects.requireNonNullElse(config.getDriverClassName(), hikariConfig.getDriverClassName()));
        this.createDatasource(dataId, config);
    }
    
}

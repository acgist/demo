package com.acgist.dd.datasource.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.zaxxer.hikari.HikariConfig;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "acgist.dynamic")
public class DynamicDatasourceProperties {

    private Map<String, HikariConfig> datasource;
    
}

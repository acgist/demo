package com.acgist.health.monitor.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "health")
public class HealthProperties {

    private Boolean enabled  = Boolean.TRUE;
    private String  security = null;

}

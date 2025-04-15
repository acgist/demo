package com.acgist.health.monitor.configuration;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "monitor")
public class MonitorProperties {

    public static final String TYPE_CPU       = "cpu";
    public static final String TYPE_DISK      = "disk";
    public static final String TYPE_MEMORY    = "memory";
    public static final String TYPE_EXCEPTION = "exception";

    @Getter
    @Setter
    public static class Monitor {

        private String name;
        private String type;
        private Double threshold;
        private List<String> disk;
        private Integer      exceptionTime;
        private List<String> exceptionFrom;
        private List<String> exceptionName;
        private List<String> exceptionMessage;
        private Integer      exceptionDuration;

    }

    private List<Monitor> monitors;

    public Monitor getCpu() {
        return this.monitors.stream().filter(v -> TYPE_CPU.equals(v.getType())).findFirst().orElse(null);
    }

    public Monitor getDisk() {
        return this.monitors.stream().filter(v -> TYPE_DISK.equals(v.getType())).findFirst().orElse(null);
    }

    public Monitor getMemory() {
        return this.monitors.stream().filter(v -> TYPE_MEMORY.equals(v.getType())).findFirst().orElse(null);
    }

    public Monitor getException() {
        return this.monitors.stream().filter(v -> TYPE_EXCEPTION.equals(v.getType())).findFirst().orElse(null);
    }

}

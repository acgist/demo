package com.acgist.health.monitor.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;

import com.acgist.health.monitor.CPUMonitor;
import com.acgist.health.monitor.DiskMonitor;
import com.acgist.health.monitor.ExceptionMonitor;
import com.acgist.health.monitor.MemoryMonitor;
import com.acgist.health.monitor.controller.MonitorController;
import com.acgist.health.monitor.filter.MonitorFilter;
import com.acgist.health.monitor.logger.MonitorLoggerAppender;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "health", name = "enabled", havingValue = "true")
@EnableConfigurationProperties({
    HealthProperties.class,
    MonitorProperties.class
})
public class MonitorAutoConfiguration implements ApplicationListener<ContextRefreshedEvent> {

    @Bean
    public CPUMonitor cpuMonitor() {
        return new CPUMonitor();
    }

    @Bean
    public DiskMonitor diskMonitor() {
        return new DiskMonitor();
    }

    @Bean
    public MemoryMonitor memoryMonitor() {
        return new MemoryMonitor();
    }

    @Bean
    public ExceptionMonitor exceptionMonitor() {
        return new ExceptionMonitor();
    }

    @Bean
    public MonitorController monitorController() {
        return new MonitorController();
    }
    
    @Bean
    public MonitorFilter monitorFilter() {
        return new MonitorFilter();
    }

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        final MonitorProperties monitorProperties = event.getApplicationContext().getBean(MonitorProperties.class);
        if(monitorProperties.getMonitors() == null) {
            return;
        }
        MonitorLoggerAppender.monitor = monitorProperties.getException();
    }
    
}

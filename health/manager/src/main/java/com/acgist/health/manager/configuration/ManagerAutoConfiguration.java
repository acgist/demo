package com.acgist.health.manager.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;

import com.acgist.health.manager.action.IDns;
import com.acgist.health.manager.action.INotify;
import com.acgist.health.manager.monitor.IRemoteMonitor;
import com.acgist.health.manager.monitor.MonitorManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "health", name = "enabled", havingValue = "true")
@EnableConfigurationProperties({
    ManagerProperties.class
})
public class ManagerAutoConfiguration implements ApplicationListener<ContextRefreshedEvent> {
    
    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        event.getApplicationContext().getBeansOfType(IDns.class).forEach((k, v) -> {
            log.info("注册DNS服务：{} - {}", v.type(), k);
            IDns.map.put(v.type(), v);
        });
        event.getApplicationContext().getBeansOfType(INotify.class).forEach((k, v) -> {
            log.info("注册通知服务：{} - {}", v.type(), k);
            INotify.map.put(v.type(), v);
        });
        event.getApplicationContext().getBeansOfType(IRemoteMonitor.class).forEach((k, v) -> {
            log.info("注册监控服务：{} - {}", v.type(), k);
            IRemoteMonitor.map.put(v.type(), v);
        });
        event.getApplicationContext().getBean(MonitorManager.class).selectDns();
    }

}

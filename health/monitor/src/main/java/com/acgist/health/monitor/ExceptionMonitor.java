package com.acgist.health.monitor;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.acgist.health.monitor.configuration.MonitorProperties;
import com.acgist.health.monitor.configuration.MonitorProperties.Monitor;
import com.acgist.health.monitor.logger.MonitorLoggerAppender;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExceptionMonitor implements IMonitor {

    @Autowired
    private MonitorProperties monitorProperties;

    @Override
    public MonitorMessage get() {
        final Monitor exceptionMonitor = this.monitorProperties.getException();
        if(exceptionMonitor == null) {
            return MonitorMessage.success(Map.of());
        }
        MonitorLoggerAppender.doCheck();
        log.debug("异常监控：{}", MonitorLoggerAppender.details);
        return MonitorMessage.of(
            MonitorLoggerAppender.health,
            MonitorLoggerAppender.details
        );
    }

    @Override
    public boolean reset() {
        MonitorLoggerAppender.reset();
        return true;
    }

}

package com.acgist.health.monitor;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.acgist.health.monitor.configuration.MonitorProperties;
import com.acgist.health.monitor.configuration.MonitorProperties.Monitor;

import lombok.extern.slf4j.Slf4j;
import oshi.SystemInfo;

@Slf4j
public class MemoryMonitor implements IMonitor {

    @Autowired
    private MonitorProperties monitorProperties;

    @Override
    public MonitorMessage get() {
        final Monitor memoryMonitor = this.monitorProperties.getMemory();
        if(memoryMonitor == null || memoryMonitor.getThreshold() == null) {
            return MonitorMessage.success(Map.of());
        }
        final var systemInfo = new SystemInfo();
        final var memory     = systemInfo.getHardware().getMemory();
        final long free      = memory.getAvailable();
        final long total     = memory.getTotal();
        log.debug("内存监控：{} - {}", free, total);
        return MonitorMessage.of(
            1.0D * free / total <= memoryMonitor.getThreshold(),
            Map.of(
                "free", free,
                "total", total
            )
        );
    }

}

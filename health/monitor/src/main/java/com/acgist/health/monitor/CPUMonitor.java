package com.acgist.health.monitor;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.acgist.health.monitor.configuration.MonitorProperties;
import com.acgist.health.monitor.configuration.MonitorProperties.Monitor;

import lombok.extern.slf4j.Slf4j;
import oshi.SystemInfo;

@Slf4j
public class CPUMonitor implements IMonitor {

    @Autowired
    private MonitorProperties monitorProperties;

    @Override
    public MonitorMessage get() {
        final Monitor cpuMonitor = this.monitorProperties.getCpu();
        if(cpuMonitor == null || cpuMonitor.getThreshold() == null) {
            return MonitorMessage.success(Map.of());
        }
        final var systemInfo  = new SystemInfo();
        final var processor   = systemInfo.getHardware().getProcessor();
        final var loadAverage = processor.getSystemLoadAverage(3);
        log.debug("CPU负载监控：{}", loadAverage);
        return MonitorMessage.of(
            loadAverage[2] <= cpuMonitor.getThreshold(),
            Map.of(
                "loadAverage", loadAverage
            )
        );
    }

}

package com.acgist.health.monitor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

import com.acgist.health.monitor.configuration.MonitorProperties;
import com.acgist.health.monitor.configuration.MonitorProperties.Monitor;

import lombok.extern.slf4j.Slf4j;
import oshi.SystemInfo;

@Slf4j
public class DiskMonitor implements IMonitor {

    @Autowired
    private MonitorProperties monitorProperties;

    @Override
    public MonitorMessage get() {
        final Monitor diskMonitor = monitorProperties.getDisk();
        if(diskMonitor == null || diskMonitor.getDisk() == null || diskMonitor.getThreshold() == null) {
            return MonitorMessage.success(Map.of());
        }
        final var systemInfo = new SystemInfo();
        final var fileSystem = systemInfo.getOperatingSystem().getFileSystem();
        final var fileStores = fileSystem.getFileStores();
        final Map<String, Object> details  = new HashMap<>();
        if(fileStores == null || fileStores.isEmpty()) {
            return MonitorMessage.of(
                Boolean.FALSE,
                details
            );
        }
        final boolean health = fileStores.stream().allMatch(fileStore -> {
            return diskMonitor.getDisk().stream().allMatch(disk -> {
                final long freeSpace  = fileStore.getFreeSpace();
                final long totalSpace = fileStore.getTotalSpace();
                if(Objects.equals(fileStore.getMount(), disk)) {
                    log.debug("磁盘监控：{} - {} - {}", disk, freeSpace, totalSpace);
                    details.put(disk, Map.of(
                        "free", freeSpace,
                        "total", totalSpace
                    ));
                    return 1.0D * freeSpace / totalSpace <= diskMonitor.getThreshold();
                }
                return true;
            });
        });
        return MonitorMessage.of(
            health,
            details
        );
    }

}

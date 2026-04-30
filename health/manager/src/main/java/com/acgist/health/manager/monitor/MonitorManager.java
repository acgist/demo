package com.acgist.health.manager.monitor;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.acgist.health.manager.action.IDns;
import com.acgist.health.manager.action.INotify;
import com.acgist.health.manager.configuration.ManagerProperties;
import com.acgist.health.manager.configuration.ManagerProperties.Dns;
import com.acgist.health.manager.configuration.ManagerProperties.Monitor;
import com.acgist.health.manager.configuration.ManagerProperties.Server;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MonitorManager {

    private final ManagerProperties managerProperties;
    
    @Scheduled(cron = "${manager.scheduled.reset:0 0 0 * * ?}")
    public void scheduledReset() {
        this.reset();
    }

    @Scheduled(initialDelay = 10000, fixedDelayString = "${manager.scheduled.check:60000}")
    public void scheduledCheck() {
        this.monitor();
    }

    public void selectDns() {
        this.managerProperties.getMonitors().forEach(monitor -> {
            final Dns dns = monitor.getDns();
            if(dns == null) {
                return;
            }
            final IDns dnsService = IDns.getDns(dns.getType());
            if(dnsService == null) {
                log.warn("DNS服务无效：{}", dns.getType());
                return; 
            }
            final String ip = dnsService.detail(dns);
            log.info("当前服务地址：{} - {}", monitor.getName(), ip);
            monitor.setServer(ip);
        });
    }
    
    private void monitor() {
        this.managerProperties.getMonitors().forEach(monitor -> {
            this.monitor(monitor);
        });
    }

    private void monitor(Monitor monitor) {
        if(monitor.getServers() == null) {
            return;
        }
        monitor.getServers().forEach(server -> {
            try {
                this.monitor(server, monitor);
            } catch (Exception e) {
                log.error("监控异常：{} - {}", monitor.getName(), server.getHost(), e);
                this.monitorNotify(String.format("监控发生异常：%s，当前系统名称：%s，服务地址：%s。", e.getMessage(), monitor.getName(), server.getHost()), monitor);
            }
        });
        final Server currentServer = monitor.getServers().stream().filter(value -> Objects.equals(value.getHost(), monitor.getServer())).findFirst().orElse(null);
        if(currentServer == null || !Boolean.TRUE.equals(currentServer.getEnabled())) {
            this.modifyDns(monitor);
        }
    }

    private void monitor(Server server, Monitor monitor) {
        final AtomicBoolean notifyFlag = new AtomicBoolean(Boolean.FALSE);
        final AtomicBoolean healthFlag = new AtomicBoolean(Boolean.TRUE);
        final StringBuffer message = new StringBuffer();
        monitor.getRemoteMonitors().stream().forEach(value -> {
            if(!Boolean.TRUE.equals(value.getEnabled())) {
                return;
            }
            int index = 0;
            final int retry = Objects.requireNonNullElse(monitor.getRetry(), 3);
            while (++index <= retry) {
                try {
                    final IRemoteMonitor remoteMonitor = IRemoteMonitor.getRemoteMonitor(value.getType());
                    if(remoteMonitor == null) {
                        log.warn("监控方式无效：{}", value.getType());
                        return;
                    }
                    final boolean health = remoteMonitor.monitor(server, monitor, value);
                    if(!health) {
                        log.info("监控失败：{} - {} - {}", value.getName(), server.getHost(), monitor.getName());
                        message.append(String.format("监控失败，监控方式：%s，当前系统名称：%s。", value.getName(), monitor.getName())).append("\n");
                        if(Boolean.TRUE.equals(value.getNotify())) {
                            notifyFlag.set(Boolean.TRUE);
                        }
                        if(Boolean.TRUE.equals(value.getHealth())) {
                            healthFlag.set(Boolean.FALSE);
                        }
                    }
                    break;
                } catch (Exception e) {
                    log.error("监控异常：{}", monitor.getName(), e);
                    if(index == retry) {
                        if(Boolean.TRUE.equals(value.getNotify())) {
                            notifyFlag.set(Boolean.TRUE);
                        }
                        if(Boolean.TRUE.equals(value.getHealth())) {
                            healthFlag.set(Boolean.FALSE);
                        }
                        message.append(String.format("监控发生异常：%s，当前系统名称：%s。", e.getMessage(), monitor.getName())).append("\n");
                        break;
                    } else {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException ex) {
                            log.error("休眠异常", ex);
                        }
                    }
                }
            }
        });
        log.info("服务状态：{} - {}", server.getHost(), healthFlag.get());
        server.setEnabled(healthFlag.get());
        if(notifyFlag.get()) {
            this.monitorNotify(message.toString(), monitor);
        }
    }

    private void reset() {
        this.managerProperties.getMonitors().forEach(monitor -> {
            this.modifyDns(monitor);
        });
    }

    private void modifyDns(Monitor monitor) {
        final Dns dns = monitor.getDns();
        if(dns == null) {
            return;
        }
        final IDns dnsService = IDns.getDns(dns.getType());
        final Server server = monitor.getServers().stream()
            .filter(v -> Boolean.TRUE.equals(v.getEnabled()))
            .sorted((a, z) -> Integer.compare(Objects.requireNonNullElse(a.getWeight(), 0), Objects.requireNonNullElse(a.getWeight(), 0)))
            .findFirst()
            .orElse(null);
        if(server == null) {
            this.monitorNotify(String.format("当前系统没有可用服务，当前系统名称：%s。", monitor.getName()), monitor);
            return;
        }
        dnsService.modify(dns, server);
    }

    private void monitorNotify(String content, Monitor monitor) {
        final INotify notify = INotify.getNotify(monitor.getNotify());;
        if(notify == null) {
            log.warn("没有通知方式：{}", monitor.getNotify());
            return;
        }
        notify.monitorNotify(content, monitor);
    }

}

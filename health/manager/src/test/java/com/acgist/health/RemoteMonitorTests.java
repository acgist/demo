package com.acgist.health;

import org.junit.jupiter.api.Test;

import com.acgist.health.manager.configuration.ManagerProperties.Monitor;
import com.acgist.health.manager.configuration.ManagerProperties.RemoteMonitor;
import com.acgist.health.manager.configuration.ManagerProperties.Server;
import com.acgist.health.manager.monitor.impl.CPURemoteMonitor;
import com.acgist.health.manager.monitor.impl.SocketRemoteMonitor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RemoteMonitorTests {

    @Test
    void testCPU() {
        final Server server = new Server();
        final Monitor monitor = new Monitor();
        final RemoteMonitor remoteMonitor = new RemoteMonitor();
        server.setHost("localhost");
        server.setPort(8080);
        monitor.setProtocol("http");
        remoteMonitor.setPath("/monitor/cpu");
        final CPURemoteMonitor cpu = new CPURemoteMonitor();
        final boolean ret = cpu.monitor(server, monitor, remoteMonitor);
        log.info("{}", ret);
    }

    @Test
    void testSocket() {
        final Server server = new Server();
        final Monitor monitor = new Monitor();
        final RemoteMonitor remoteMonitor = new RemoteMonitor();
        server.setHost("www.baidu.com");
        server.setPort(80);
        final SocketRemoteMonitor socket = new SocketRemoteMonitor();
        final boolean ret = socket.monitor(server, monitor, remoteMonitor);
        log.info("{}", ret);
    }

}

package com.acgist.health.manager.monitor.impl;

import java.net.InetSocketAddress;
import java.net.Socket;

import org.springframework.stereotype.Component;

import com.acgist.health.manager.configuration.ManagerProperties.Monitor;
import com.acgist.health.manager.configuration.ManagerProperties.RemoteMonitor;
import com.acgist.health.manager.configuration.ManagerProperties.Server;
import com.acgist.health.manager.monitor.IRemoteMonitor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SocketRemoteMonitor implements IRemoteMonitor {

    @Override
    public String type() {
        return IRemoteMonitor.TYPE_SOCKET;
    }

    @Override
    public boolean monitor(Server server, Monitor monitor, RemoteMonitor remoteMonitor) {
        try (
            final Socket socket = new Socket();
        ) {
            final int timeout = monitor.getTimeout() == null ? 5000 : monitor.getTimeout();
            socket.setSoTimeout(timeout);
            socket.connect(new InetSocketAddress(server.getHost(), server.getPort()), timeout);
            return socket.isConnected();
        } catch (Exception e) {
            log.error("Socket连接异常", e);
        }
        return false;
    }

}

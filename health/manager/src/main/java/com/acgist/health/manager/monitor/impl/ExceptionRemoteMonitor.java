package com.acgist.health.manager.monitor.impl;

import org.springframework.stereotype.Component;

import com.acgist.health.manager.configuration.ManagerProperties.Monitor;
import com.acgist.health.manager.configuration.ManagerProperties.RemoteMonitor;
import com.acgist.health.manager.configuration.ManagerProperties.Server;
import com.acgist.health.manager.monitor.HttpRemoteMonitor;
import com.acgist.health.manager.monitor.IRemoteMonitor;

@Component
public class ExceptionRemoteMonitor extends HttpRemoteMonitor implements IRemoteMonitor {

    @Override
    public String type() {
        return IRemoteMonitor.TYPE_EXCEPTION;
    }

    @Override
    public boolean monitor(Server server, Monitor monitor, RemoteMonitor remoteMonitor) {
        return this.get(server, monitor, remoteMonitor);
    }

}

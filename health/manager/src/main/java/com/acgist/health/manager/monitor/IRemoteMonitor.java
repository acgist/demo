package com.acgist.health.manager.monitor;

import java.util.HashMap;
import java.util.Map;

import com.acgist.health.manager.configuration.ManagerProperties.Monitor;
import com.acgist.health.manager.configuration.ManagerProperties.RemoteMonitor;
import com.acgist.health.manager.configuration.ManagerProperties.Server;

public interface IRemoteMonitor {

    String TYPE_CPU       = "cpu";
    String TYPE_DISK      = "disk";
    String TYPE_REST      = "rest";
    String TYPE_SOCKET    = "socket";
    String TYPE_MEMORY    = "memory";
    String TYPE_EXCEPTION = "exception";

    String type();

    boolean monitor(Server server, Monitor monitor, RemoteMonitor remoteMonitor);

    Map<String, IRemoteMonitor> map = new HashMap<>();

    static IRemoteMonitor getRemoteMonitor(String type) {
        return map.get(type);
    }

}

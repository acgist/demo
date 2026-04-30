package com.acgist.health.manager.configuration;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "manager")
public class ManagerProperties {

    @Getter
    @Setter
    public static class Dns {

        private String type;
        private String accessId;
        private String accessKey;
        private String endpoint;
        private String domain;
        private String domainId;
        private String domainRr;
        private Long   domainTtl;
        private String domainType;

    }

    @Getter
    @Setter
    public static class Mail {

        private String to;
        private String subject;
        private Long   sendLimit;
        private Long   lastSendTime;

    }

    @Getter
    @Setter
    public static class Notify {

    }

    @Getter
    @Setter
    public static class Server {
        
        private String  host;
        private Integer port;
        private Boolean main    = Boolean.FALSE;
        private Integer weight  = 1;
        private Boolean enabled = Boolean.TRUE;

    }

    @Getter
    @Setter
    public static class RemoteMonitor {

        private String  name;
        private String  type;
        private Boolean enabled;
        private String  path;
        private Boolean notify;
        private Boolean health;

    }

    @Getter
    @Setter
    public static class Monitor {

        private String  name;
        private Integer retry;
        private String  notify;
        private Integer timeout;
        private String  protocol;
        private Dns  dns;
        private Mail mail;
        private String       server; // 当前服务地址
        private List<Server> servers;
        private List<RemoteMonitor> remoteMonitors;

    }

    private List<String>  stun;
    private List<Monitor> monitors;

}

package com.acgist.health;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.acgist.health.manager.StunManager;
import com.acgist.health.manager.action.IDns;
import com.acgist.health.manager.action.dns.AliyunDns;
import com.acgist.health.manager.configuration.ManagerProperties;
import com.acgist.health.manager.configuration.ManagerProperties.Dns;
import com.acgist.health.manager.configuration.ManagerProperties.Server;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class DnsTests {

    @Test
    void testDns() {
        final ManagerProperties managerProperties = new ManagerProperties();
        managerProperties.setStun(List.of("stun1.l.google.com:19302"));
        IDns dns = new AliyunDns(new StunManager(managerProperties));
        final Dns dnsConfig = new Dns();
        dnsConfig.setDomain("acgist.com");;
        dnsConfig.setDomainRr("@");
        dnsConfig.setDomainType("A");
        dnsConfig.setAccessId("");
        dnsConfig.setAccessKey("");
        dnsConfig.setEndpoint("alidns.cn-hangzhou.aliyuncs.com");
        final String ip = dns.detail(dnsConfig);
        log.info("{}", ip);
        log.info("{}", dnsConfig.getDomainId());
        final Server server = new Server();
        server.setHost("127.0.0.1");
        final boolean ret = dns.modify(dnsConfig, server);
        log.info("{}", ret);
    }

}

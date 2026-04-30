package com.acgist.health.manager.action.dns;

import org.springframework.stereotype.Component;

import com.acgist.health.manager.action.IDns;
import com.acgist.health.manager.configuration.ManagerProperties.Dns;
import com.acgist.health.manager.configuration.ManagerProperties.Server;

@Component
public class HuaweiDns implements IDns {

    @Override
    public String type() {
        return IDns.TYPE_HUAWEI;
    }

    @Override
    public String detail(Dns dns) {
        throw new UnsupportedOperationException("Unimplemented method 'detail'");
    }

    @Override
    public boolean modify(Dns dns, Server server) {
        throw new UnsupportedOperationException("Unimplemented method 'modify'");
    }

}

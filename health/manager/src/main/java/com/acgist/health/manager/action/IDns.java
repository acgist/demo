package com.acgist.health.manager.action;

import java.util.HashMap;
import java.util.Map;

import com.acgist.health.manager.configuration.ManagerProperties.Dns;
import com.acgist.health.manager.configuration.ManagerProperties.Server;

public interface IDns extends IAction {

    String TYPE_ALIYUN  = "aliyun";
    String TYPE_HUAWEI  = "huawei";
    String TYPE_TENCENT = "tencent";

    String detail(Dns dns);

    boolean modify(Dns dns, Server server);

    Map<String, IDns> map = new HashMap<>();

    static IDns getDns(String type) {
        return map.get(type);
    }

}

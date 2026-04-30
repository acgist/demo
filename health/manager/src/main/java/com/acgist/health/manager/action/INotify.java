package com.acgist.health.manager.action;

import java.util.HashMap;
import java.util.Map;

import com.acgist.health.manager.configuration.ManagerProperties.Monitor;

public interface INotify extends IAction {

    String TYPE_SMS  = "sms";
    String TYPE_MAIL = "mail";

    boolean monitorNotify(String content, Monitor monitor);

    Map<String, INotify> map = new HashMap<>();

    static INotify getNotify(String type) {
        return map.get(type);
    }

}

package com.acgist.health.manager.action.notify;

import org.springframework.stereotype.Component;

import com.acgist.health.manager.action.INotify;
import com.acgist.health.manager.configuration.ManagerProperties.Monitor;

@Component
public class SmsNotify implements INotify {

    @Override
    public String type() {
        return INotify.TYPE_SMS;
    }

    @Override
    public boolean monitorNotify(String content, Monitor monitor) {
        throw new UnsupportedOperationException("Unimplemented method 'monitorNotiry'");
    }

}

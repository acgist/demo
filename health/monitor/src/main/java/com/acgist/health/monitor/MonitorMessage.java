package com.acgist.health.monitor;

import java.io.Serializable;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MonitorMessage implements Serializable {

    private static final long serialVersionUID = -1979618722416588199L;

    private Boolean health;
    private Map<String, Object> details;

    public static final MonitorMessage fail(final Map<String, Object> details) {
        return MonitorMessage.of(Boolean.FALSE, details);
    }
    
    public static final MonitorMessage success(final Map<String, Object> details) {
        return MonitorMessage.of(Boolean.TRUE, details);
    }

    public static final MonitorMessage of(final Boolean health, final Map<String, Object> details) {
        final MonitorMessage monitorMessage = new MonitorMessage();
        monitorMessage.setHealth(health);
        monitorMessage.setDetails(details);
        return monitorMessage;
    }

}

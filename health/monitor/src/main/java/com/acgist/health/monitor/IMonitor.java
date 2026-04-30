package com.acgist.health.monitor;

public interface IMonitor {

    /**
     * @return 监控信息
     */
    MonitorMessage get();

    /**
     * 重置监控信息
     * 
     * @return 是否成功
     */
    default boolean reset() {
        return true;
    }

}

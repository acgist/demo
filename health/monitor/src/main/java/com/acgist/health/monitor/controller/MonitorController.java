package com.acgist.health.monitor.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acgist.health.monitor.CPUMonitor;
import com.acgist.health.monitor.DiskMonitor;
import com.acgist.health.monitor.ExceptionMonitor;
import com.acgist.health.monitor.MemoryMonitor;
import com.acgist.health.monitor.MonitorMessage;
import com.acgist.health.monitor.RestMonitor;

@RequestMapping("/monitor")
public class MonitorController {

    @Autowired
    private CPUMonitor cpuMonitor;
    @Autowired
    private DiskMonitor diskMonitor;
    @Autowired(required = false)
    private RestMonitor restMonitor;
    @Autowired
    private MemoryMonitor memoryMonitor;
    @Autowired
    private ExceptionMonitor exceptionMonitor;

    @GetMapping("/cpu")
    public @ResponseBody MonitorMessage cpu() {
        return this.cpuMonitor.get();
    }

    @GetMapping("/disk")
    public @ResponseBody MonitorMessage disk() {
        return this.diskMonitor.get();
    }

    @GetMapping("/rest")
    public @ResponseBody MonitorMessage rest() {
        return this.restMonitor == null ? MonitorMessage.success(Map.of()) : this.restMonitor.get();
    }

    @GetMapping("/memory")
    public @ResponseBody MonitorMessage memory() {
        return this.memoryMonitor.get();
    }

    @GetMapping("/exception")
    public @ResponseBody MonitorMessage exception() {
        return this.exceptionMonitor.get();
    }

    @GetMapping("/reset")
    public @ResponseBody Boolean reset() {
        return
            this.cpuMonitor.reset()    &
            this.diskMonitor.reset()   &
            this.memoryMonitor.reset() &
            this.exceptionMonitor.reset();
    }

}

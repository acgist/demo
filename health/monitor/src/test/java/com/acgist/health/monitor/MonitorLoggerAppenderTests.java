package com.acgist.health.monitor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.acgist.health.monitor.configuration.MonitorProperties.Monitor;
import com.acgist.health.monitor.logger.MonitorLoggerAppender;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class MonitorLoggerAppenderTests {

    @Test
    void testLogger() throws InterruptedException {
        final Monitor monitor = new Monitor();
        monitor.setExceptionDuration(1000);
        monitor.setExceptionTime(3);
        monitor.setExceptionName(List.of("java.lang.IllegalAccessError"));
        monitor.setExceptionFrom(List.of("com.acgist.health.monitor.MonitorLoggerAppenderTests"));
        monitor.setExceptionMessage(List.of(".*null.*"));
        MonitorLoggerAppender.monitor = monitor;
        log.error("error", new IllegalAccessError("null"));
        log.error("error", new NullPointerException("null"));
        log.error("error", new NullPointerException("null"));
        assertEquals(true, MonitorLoggerAppender.health);
        log.error("error", new NullPointerException("null"));
        assertEquals(false, MonitorLoggerAppender.health);
        log.error("error", new NullPointerException("null"));
        assertEquals(false, MonitorLoggerAppender.health);
        Thread.sleep(2000);
        MonitorLoggerAppender.doCheck();
        assertEquals(true, MonitorLoggerAppender.health);
        log.error("error", new NullPointerException("null"));
        log.error("error", new NullPointerException("null"));
        log.error("error", new NullPointerException("null"));
        assertEquals(true, MonitorLoggerAppender.health);
        log.error("error", new NullPointerException("null"));
        MonitorLoggerAppender.doCheck();
        assertEquals(false, MonitorLoggerAppender.health);
        Thread.sleep(2000);
        log.error("error", new NullPointerException("null"));
        assertEquals(true, MonitorLoggerAppender.health);
        log.error("error", new NullPointerException("null"));
        log.error("error", new NullPointerException("null"));
        assertEquals(true, MonitorLoggerAppender.health);
        log.error("error", new NullPointerException("null"));
        assertEquals(false, MonitorLoggerAppender.health);
        MonitorLoggerAppender.doCheck();
        assertEquals(false, MonitorLoggerAppender.health);
        MonitorLoggerAppender.doCheck();
        assertEquals(false, MonitorLoggerAppender.health);
        log.error("error", new NullPointerException("null"));
        Thread.sleep(500);
        MonitorLoggerAppender.doCheck();
        assertEquals(false, MonitorLoggerAppender.health);
        log.error("error", new NullPointerException("null"));
        Thread.sleep(500);
        MonitorLoggerAppender.doCheck();
        assertEquals(false, MonitorLoggerAppender.health);
        log.error("error", new NullPointerException("null"));
        Thread.sleep(500);
        MonitorLoggerAppender.doCheck();
        assertEquals(false, MonitorLoggerAppender.health);
        log.error("error", new NullPointerException("null"));
        Thread.sleep(500);
        MonitorLoggerAppender.doCheck();
        assertEquals(false, MonitorLoggerAppender.health);
        log.error("error", new NullPointerException("null"));
        Thread.sleep(500);
        MonitorLoggerAppender.doCheck();
        assertEquals(false, MonitorLoggerAppender.health);
        log.error("error", new NullPointerException("null"));
        Thread.sleep(500);
        MonitorLoggerAppender.doCheck();
        assertEquals(false, MonitorLoggerAppender.health);
        log.error("error", new NullPointerException("null"));
        Thread.sleep(500);
        MonitorLoggerAppender.doCheck();
        assertEquals(false, MonitorLoggerAppender.health);
        log.error("error", new NullPointerException("null"));
        Thread.sleep(500);
        MonitorLoggerAppender.doCheck();
        assertEquals(false, MonitorLoggerAppender.health);
        log.error("error", new NullPointerException("null"));
        Thread.sleep(2000);
        MonitorLoggerAppender.doCheck();
        assertEquals(true, MonitorLoggerAppender.health);
    }

}

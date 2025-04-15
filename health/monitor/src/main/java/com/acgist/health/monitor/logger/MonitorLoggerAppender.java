package com.acgist.health.monitor.logger;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import com.acgist.health.monitor.configuration.MonitorProperties;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MonitorLoggerAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    
    private static long exceptionCount     = 0L; // 异常计数
    private static long exceptionCountTime = 0L; // 异常计数时间
    
    public static boolean             health  = true;
    public static Map<String, Object> details = Map.of();
    
    public static MonitorProperties.Monitor monitor;

    @Override
    protected void append(ILoggingEvent event) {
        if(!(event instanceof LoggingEvent)) {
            return;
        }
        final AtomicReference<IThrowableProxy> throwable = new AtomicReference<IThrowableProxy>(((LoggingEvent) event).getThrowableProxy());
        if(throwable.get() == null) {
            return;
        }
        final long currentTime = System.currentTimeMillis();
        if(MonitorLoggerAppender.exceptionCountTime == 0L) {
            MonitorLoggerAppender.exceptionCountTime = currentTime;
        } else if(!MonitorLoggerAppender.health) {
            if(currentTime - MonitorLoggerAppender.exceptionCountTime > Objects.requireNonNullElse(MonitorLoggerAppender.monitor.getExceptionDuration(), 60000)) {
                MonitorLoggerAppender.exceptionCount = 0L;
            }
            MonitorLoggerAppender.exceptionCountTime = currentTime;
        } else if(currentTime - MonitorLoggerAppender.exceptionCountTime > Objects.requireNonNullElse(MonitorLoggerAppender.monitor.getExceptionDuration(), 60000)) {
            MonitorLoggerAppender.exceptionCount     = 0L;
            MonitorLoggerAppender.exceptionCountTime = currentTime;
        } else {
            // -
        }
        if(MonitorLoggerAppender.monitor.getExceptionFrom() != null) {
            final boolean health = MonitorLoggerAppender.health && MonitorLoggerAppender.monitor.getExceptionFrom().stream().noneMatch(value -> {
                return Objects.equals(value, event.getLoggerName());
            });
            if(!health) {
                ++MonitorLoggerAppender.exceptionCount;
                this.doCheck(event);
                return;
            }
        }
        while(throwable.get() != null) {
            if(MonitorLoggerAppender.monitor.getExceptionName() != null) {
                final boolean health = MonitorLoggerAppender.health && MonitorLoggerAppender.monitor.getExceptionName().stream().noneMatch(value -> {
                    return Objects.equals(value, throwable.get().getClassName());
                });
                if(!health) {
                    ++MonitorLoggerAppender.exceptionCount;
                    this.doCheck(event);
                    return;
                }
            }
            if(MonitorLoggerAppender.monitor.getExceptionMessage() != null) {
                final boolean health = MonitorLoggerAppender.health && MonitorLoggerAppender.monitor.getExceptionMessage().stream().noneMatch(value -> {
                    return value != null && throwable.get().getMessage() != null && throwable.get().getMessage().matches(value);
                });
                if(!health) {
                    ++MonitorLoggerAppender.exceptionCount;
                    this.doCheck(event);
                    return;
                }
            }
            throwable.set(throwable.get().getCause());
        }
    }

    public static final void reset() {
        MonitorLoggerAppender.health  = true;
        MonitorLoggerAppender.details = Map.of();
    }

    public static final void doCheck() {
        if(!MonitorLoggerAppender.health) {
            if(System.currentTimeMillis() - MonitorLoggerAppender.exceptionCountTime > Objects.requireNonNullElse(MonitorLoggerAppender.monitor.getExceptionDuration(), 60000)) {
                MonitorLoggerAppender.reset();
            }
        }
    }

    private void doCheck(ILoggingEvent event) {
        if(MonitorLoggerAppender.monitor.getExceptionTime() != null) {
            MonitorLoggerAppender.health = MonitorLoggerAppender.exceptionCount <= MonitorLoggerAppender.monitor.getExceptionTime();
            if(!MonitorLoggerAppender.health) {
                MonitorLoggerAppender.details = Map.of(
                    "message", String.format("系统出现异常：%s，超过次数限制：%d次！", event.getFormattedMessage(), MonitorLoggerAppender.monitor.getExceptionTime())
                );
            }
        }
    }

}

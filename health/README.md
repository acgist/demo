# 容灾系统

## monitor

监控器：检查系统健康状态。

### 日志配置

```
<appender name="monitor" class="com.acgist.health.monitor.logger.MonitorLoggerAppender">
</appender>

<root level="warn">
    <appender-ref ref="monitor" />
</root>
```

## manager

管理器：检查监控器的健康状态。

### 监控

#### CPU监控

#### 磁盘监控

#### 内存监控

#### 网络监控

#### 异常监控

* 规定时间内匹配异常次数超过限制判定为异常
* 同一次异常即使匹配多个规则也只会统计一次
* 处于异常时如果规定时间内没有匹配任何规则即可恢复

### 监控动作

#### DNS切换

#### 邮件通知

## 自动恢复

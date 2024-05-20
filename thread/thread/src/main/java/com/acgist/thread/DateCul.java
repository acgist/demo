package com.acgist.thread;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class DateCul {

    public static void main(String[] args) {
        String line = """
2024-04-30 09:48:00,750 [analyExecutor-768790384277933] DEBUG [c.x.p.s.b.service.TaskAnalyDataService] TaskAnalyDataService.java:148 - 开始数据解析，数据信息：[68, 41, 65, -105],模型信息：{115600229=CacheModelItem(modelItemId=115600229, collectPointItemId=115600230, dataType=10, itemStartOffset=40500, numberOfRegisters=2, handleExpression=)}
2024-04-30 09:48:00,751 [analyExecutor-768790384277933] DEBUG [c.x.p.s.b.service.TaskAnalyDataService] TaskAnalyDataService.java:148 - 开始数据解析，数据信息：[-124, 66, 65, -45, 126, 50, 66, 125, 0, 0, 65, -32, 0, 0, 65, -112, 0, 0, 66, -86, 0, 0, 66, -126],模型信息：{109800028=CacheModelItem(modelItemId=109800028, collectPointItemId=114900028, dataType=10, itemStartOffset=40610, numberOfRegisters=2, handleExpression=), 109800027=CacheModelItem(modelItemId=109800027, collectPointItemId=114900027, dataType=10, itemStartOffset=40608, numberOfRegisters=2, handleExpression=), 109800026=CacheModelItem(modelItemId=109800026, collectPointItemId=114900026, dataType=10, itemStartOffset=40606, numberOfRegisters=2, handleExpression=), 109800025=CacheModelItem(modelItemId=109800025, collectPointItemId=114900025, dataType=10, itemStartOffset=40604, numberOfRegisters=2, handleExpression=), 109800024=CacheModelItem(modelItemId=109800024, collectPointItemId=114900024, dataType=10, itemStartOffset=40602, numberOfRegisters=2, handleExpression=), 109800023=CacheModelItem(modelItemId=109800023, collectPointItemId=114900023, dataType=10, itemStartOffset=40600, numberOfRegisters=2, handleExpression=)}
2024-04-30 09:48:00,761 [analyExecutor-768790384277933] DEBUG [c.x.p.southbound.alarm.trigger.TriggerRuleCounter] TriggerRuleCounter.java:154 - 执行取值SQL：SELECT LAST(ts), FIRST(ts), AVG(`109800023`) FROM (SELECT * FROM pemc.p_114900021  WHERE ts >= '2024-04-30 09:47:00' )
2024-04-30 09:48:00,775 [analyExecutor-768790384277933] DEBUG [c.x.p.southbound.alarm.trigger.TriggerRuleCounter] TriggerRuleCounter.java:154 - 执行取值SQL：SELECT ts, `109800024` FROM pemc.p_114900021  WHERE ts >= '2024-04-30 09:47:59' ORDER BY ts DESC LIMIT 1
2024-04-30 09:48:00,793 [analyExecutor-768790384277933] DEBUG [c.x.p.s.b.service.TaskAnalyDataService] TaskAnalyDataService.java:416 - 合成指标执行: 脚本(26.451461724999994+63.373238)/2,计算结果44.912349862499994
2024-04-30 09:48:00,797 [analyExecutor-768790384277933] DEBUG [c.x.p.s.b.service.TaskAnalyDataService] TaskAnalyDataService.java:385 - 合成指标取值命中缓存（counter）：avg-109800023-114900021
2024-04-30 09:48:00,798 [analyExecutor-768790384277933] DEBUG [c.x.p.s.b.service.TaskAnalyDataService] TaskAnalyDataService.java:385 - 合成指标取值命中缓存（counter）：direct-109800024-114900021
2024-04-30 09:48:00,800 [analyExecutor-768790384277933] DEBUG [c.x.p.s.b.service.TaskAnalyDataService] TaskAnalyDataService.java:416 - 合成指标执行: 脚本(26.451461724999994+63.373238)/2,计算结果44.912349862499994
2024-04-30 09:48:00,814 [analyExecutor-768790384277933] DEBUG [c.x.p.southbound.alarm.trigger.TriggerRuleCounter] TriggerRuleCounter.java:154 - 执行取值SQL：SELECT LAST(ts), FIRST(ts), MAX(`109800023`) FROM (SELECT * FROM pemc.p_114900021  WHERE ts >= '2024-04-30 09:47:00' )
2024-04-30 09:48:00,822 [analyExecutor-768790384277933] DEBUG [c.x.p.s.b.service.TaskAnalyDataService] TaskAnalyDataService.java:416 - 合成指标执行: 脚本26.479633,计算结果26.479633
2024-04-30 09:48:00,828 [analyExecutor-768790384277933] DEBUG [c.x.p.southbound.alarm.trigger.TriggerRuleCounter] TriggerRuleCounter.java:154 - 执行取值SQL：SELECT LAST(ts), FIRST(ts), MIN(`109800023`) FROM (SELECT * FROM pemc.p_114900021  WHERE ts >= '2024-04-30 09:47:00' )
2024-04-30 09:48:00,838 [analyExecutor-768790384277933] DEBUG [c.x.p.s.b.service.TaskAnalyDataService] TaskAnalyDataService.java:416 - 合成指标执行: 脚本26.426228,计算结果26.426228
2024-04-30 09:48:00,841 [analyExecutor-768790384277933] DEBUG [c.x.p.southbound.alarm.trigger.TriggerRuleCounter] TriggerRuleCounter.java:154 - 执行取值SQL：SELECT LAST(ts), FIRST(ts), SUM(`109800023`) FROM (SELECT * FROM pemc.p_114900021   WHERE ts >= '2024-04-30 09:47:59' ORDER BY ts DESC LIMIT 1)
2024-04-30 09:48:00,849 [analyExecutor-768790384277933] DEBUG [c.x.p.s.b.service.TaskAnalyDataService] TaskAnalyDataService.java:416 - 合成指标执行: 脚本26.439579,计算结果26.439579
2024-04-30 09:48:00,854 [analyExecutor-768790384277933] DEBUG [c.x.p.southbound.alarm.trigger.TriggerRuleCounter] TriggerRuleCounter.java:154 - 执行取值SQL：SELECT ts, `109800023` FROM pemc.p_114900021  WHERE ts >= '2024-04-30 09:47:57' ORDER BY ts DESC LIMIT 2
2024-04-30 09:48:00,863 [analyExecutor-768790384277933] DEBUG [c.x.p.s.b.service.TaskAnalyDataService] TaskAnalyDataService.java:416 - 合成指标执行: 脚本-0.016021,计算结果-0.016021
2024-04-30 09:48:00,867 [analyExecutor-768790384277933] DEBUG [c.x.p.southbound.alarm.trigger.TriggerRuleCounter] TriggerRuleCounter.java:154 - 执行取值SQL：SELECT ts, `109800023` FROM pemc.p_114900021  WHERE ts >= '2024-04-30 09:47:57' ORDER BY ts DESC LIMIT 2
2024-04-30 09:48:00,879 [analyExecutor-768790384277933] DEBUG [c.x.p.s.b.service.TaskAnalyDataService] TaskAnalyDataService.java:416 - 合成指标执行: 脚本0.016021,计算结果0.016021
2024-04-30 09:48:00,884 [analyExecutor-768790384277933] WARN  [c.x.p.s.b.service.TaskAnalyDataService] TaskAnalyDataService.java:395 - 合成指标为空：114900021 - {109800023}.nodate(60) - {109800023}.nodate(60)
2024-04-30 09:48:00,887 [analyExecutor-768790384277933] DEBUG [c.x.p.s.b.service.TaskAnalyDataService] TaskAnalyDataService.java:385 - 合成指标取值命中缓存（counter）：avg-109800023-114900021
2024-04-30 09:48:00,888 [analyExecutor-768790384277933] DEBUG [c.x.p.s.b.service.TaskAnalyDataService] TaskAnalyDataService.java:416 - 合成指标执行: 脚本26.451461724999994,计算结果26.451461724999994
2024-04-30 09:48:00,891 [analyExecutor-768790384277933] DEBUG [c.x.p.s.b.service.TaskAnalyDataService] TaskAnalyDataService.java:385 - 合成指标取值命中缓存（counter）：avg-109800023-114900021
2024-04-30 09:48:00,892 [analyExecutor-768790384277933] DEBUG [c.x.p.s.b.service.TaskAnalyDataService] TaskAnalyDataService.java:416 - 合成指标执行: 脚本26.451461724999994,计算结果26.451461724999994
2024-04-30 09:48:00,895 [analyExecutor-768790384277933] DEBUG [c.x.p.s.b.service.TaskAnalyDataService] TaskAnalyDataService.java:203 - 分析耗时：114900021 - 145
        """;
        final AtomicInteger old = new AtomicInteger(0);
        Stream.of(line.split("\n")).map(v -> v.strip()).filter(v -> v.length() > 0)
        .map(v -> {
            final int index = v.indexOf(',');
            final int jndex = v.indexOf(' ', index);
            final int diff  = Integer.parseInt(v.substring(index + 1, jndex));
            Map.Entry<Integer, String> entry = null;
            if(old.get() > 0) {
                entry = Map.entry(diff - old.get(), diff - old.get() + " = " + v.substring(v.indexOf('-', v.indexOf(".java")) + 2));
            }
            old.set(diff);
            return entry;
        })
        .filter(v -> v != null)
        .sorted((a, z) -> a.getKey().compareTo(z.getKey()))
        .forEach(v -> System.out.println(v.getValue()));
    }
    
}

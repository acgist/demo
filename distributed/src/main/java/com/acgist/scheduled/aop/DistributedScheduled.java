package com.acgist.scheduled.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分布式定时任务切点
 * 
 * 定时任务一个周期内只能有一个任务执行，所以不用重试拿锁。
 * 任务完成最好不要立即释放锁，因为时间同步以及执行周期问题导致重复执行。
 * Zookeeper一个节点拿锁成功以后其他节点永远不能在拿到锁。
 * 
 * @author acgist
 */
@Target({
    ElementType.TYPE,
    ElementType.METHOD,
})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DistributedScheduled {

    /**
     * @return 分布式锁键值
     */
    String key();
    
    /**
     * @return 分布式锁名称
     */
    String name() default "";

    /**
     * 定时任务：
     * 1. Zookeeper可以不用设置
     * 2. Redis时长最好大于定时任务间隔
     * 
     * @return 分布式锁锁定时长（单位：毫秒）
     */
    int ttl() default 30000;
    
    /**
     * 定时任务最好不要立即释放（时间不同可能导致重复执行）
     * 
     * @return 执行完成立即释放
     */
    boolean release() default false;

}

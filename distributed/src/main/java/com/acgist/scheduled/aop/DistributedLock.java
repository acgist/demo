package com.acgist.scheduled.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分布式锁切点
 * 
 * 方法执行完成以后应该立即释放锁资源，注解只能保证方法原子性，如果需要实现性能更加优秀的指定资源的原子性建议直接使用代码实现锁定某个资源。
 * 
 * @author acgist
 */
@Target({
    ElementType.TYPE,
    ElementType.METHOD,
})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DistributedLock {

    /**
     * @return 分布式锁键值
     */
    String key();
    
    /**
     * @return 分布式锁名称
     */
    String name() default "";

    /**
     * @return 分布式锁锁定时长（单位：毫秒）
     */
    int ttl() default 30000;
    
    /**
     * @return 分布式锁等待时长（单位：毫秒）
     */
    int duration() default 30000;
    
    /**
     * @return 执行完成立即释放
     */
    boolean release() default true;

}

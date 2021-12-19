package com.acgist.scheduled.aop;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分布式定时任务切点
 * 
 * @author acgist
 */
@Target({ java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DistributedScheduled {

	/**
	 * 分布式锁名称
	 */
	String name();

	/**
	 * 分布式锁时长（单位：秒）
	 * 
	 * 注意：Redis时长必须大于定时任务间隔，Zookeeper可以不用设置。
	 */
	int ttl() default 0;

}

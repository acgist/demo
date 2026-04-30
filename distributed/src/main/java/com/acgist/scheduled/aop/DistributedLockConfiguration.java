package com.acgist.scheduled.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

import com.acgist.scheduled.lock.DistributedLock;

/**
 * 分布式锁逻辑
 * 
 * @author acgist
 */
@Aspect
@Configuration
@ConditionalOnBean(value = DistributedLock.class)
public class DistributedLockConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistributedLockConfiguration.class);

    @Autowired
    private DistributedLock distributedLock;
    
    /**
     * 注解切点
     */
    @Pointcut("@annotation(com.acgist.scheduled.aop.DistributedLock)")
    public void scheduled() {
    }

    /**
     * 环绕执行
     * 
     * @param proceedingJoinPoint 切点
     * 
     * @return 结果
     * 
     * @throws Throwable 异常
     */
    @Around("scheduled()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        final com.acgist.scheduled.aop.DistributedLock distributedLock = this.getAnnotation(proceedingJoinPoint);
        if (distributedLock == null) {
            return null;
        }
        final String key  = distributedLock.key();
        final String name = distributedLock.name();
        try {
            if (this.distributedLock.tryLock(key, distributedLock.duration(), distributedLock.ttl())) {
                LOGGER.debug("分布式锁加锁成功执行：{} - {}", key, name);
                return proceedingJoinPoint.proceed();
            } else {
                LOGGER.warn("分布式锁加锁失败执行：{} - {}", key, name);
            }
        } catch (Throwable e) {
            throw e;
        } finally {
            if(distributedLock.release()) {
                this.distributedLock.unlock(key);
            }
        }
        return null;
    }

    /**
     * @param proceedingJoinPoint 切点
     * 
     * @return 注解
     */
    private com.acgist.scheduled.aop.DistributedLock getAnnotation(ProceedingJoinPoint proceedingJoinPoint) {
        if (proceedingJoinPoint.getSignature() instanceof MethodSignature) {
            final MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
            return methodSignature.getMethod().getAnnotation(com.acgist.scheduled.aop.DistributedLock.class);
        }
        return null;
    }

}

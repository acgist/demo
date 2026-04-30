package com.acgist.scheduled.lock;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 分布式锁Redis实现
 * 
 * @author acgist
 */
@Primary
@Component
@ConditionalOnClass(value = RedisTemplate.class)
@ConditionalOnProperty(value = "${distributed.lock.type}", havingValue = "redis", matchIfMissing = true)
public class RedisLock implements DistributedLock {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean set(String key, String value, int ttl) {
        return this.redisTemplate.opsForValue().setIfAbsent(key, value, Duration.ofMillis(ttl));
    }

    @Override
    public void reset(String key, String value, int ttl) {
        this.redisTemplate.opsForValue().set(key, value, Duration.ofMillis(ttl));
    }

    @Override
    public String get(String key) {
        return this.redisTemplate.opsForValue().get(key);
    }

    @Override
    public void delete(String key) {
        this.redisTemplate.opsForValue().getOperations().delete(key);
    }

}

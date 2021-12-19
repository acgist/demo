# 分布式锁&分布式定时任务

支持两种分布式锁：Redis、Zookeeper

## 使用

Redis和Zookeeper任选一种

#### 配置

```
# Redis
spring.redis.host=localhost
spring.redis.port=6379
# Zookeeper
zookeeper.address=127.0.0.1:2181
zookeeper.timeout=10000
# 配置
# Zookeeper锁根
zookeeper.lock.root=lock
# Zookeeper锁名
zookeeper.lock.keys=acgist,group-a,group-b
```

#### 依赖

```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-aop</artifactId>
</dependency>
<dependency>
	<groupId>org.apache.zookeeper</groupId>
	<artifactId>zookeeper</artifactId>
	<optional>true</optional>
</dependency>
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-redis</artifactId>
	<optional>true</optional>
</dependency>
```

#### 扫描

```
@ComponentScan("com.acgist.scheduled")
```

#### 分布式锁

```
@Autowired
private DistributedLock distributedLock;

try {
	if (this.distributedLock.tryLock("acgist", 10)) {
		// 成功
	} else {
		// 失败
	}
} finally {
	this.distributedLock.unlock("acgist");
}
```

#### 分布式定时任务

```
@Scheduled(cron = "*/5 * * * * ?")
@DistributedScheduled(name = "lockName", ttl = 10)
public void scheduledGroupB() {
	System.out.println("scheduledGroupB");
}
```

> 只需添加注解`DistributedScheduled`

## 注意

* 默认优先使用Redis
* 保证Redis或者Zookeeper都在同一集群
* 使用Zookeeper需要提前配置定时任务名称

> 有时间以后做成starter
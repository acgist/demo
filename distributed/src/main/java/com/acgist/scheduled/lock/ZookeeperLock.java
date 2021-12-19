package com.acgist.scheduled.lock;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

/**
 * 分布式锁Zookeeper实现
 * 
 * @author acgist
 */
@Component
@ConditionalOnClass(value = ZooKeeper.class)
public class ZookeeperLock implements DistributedLock {

	private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperLock.class);

	/**
	 * 序列锁名
	 */
	private static final String LOCK_NAME = "lock-";
	/**
	 * 真实锁名
	 */
	private static final ThreadLocal<Map<String, String>> KEY_LOCAL = new ThreadLocal<Map<String, String>>() {
		@Override
		protected Map<String, String> initialValue() {
			return new ConcurrentHashMap<>();
		}
	};

	@Value("${zookeeper.address}")
	private String address;
	@Value("${zookeeper.timeout}")
	private int timeout;
	@Value("${zookeeper.lock.root}")
	private String root;
	@Value("#{'${zookeeper.lock.keys}'.split(',')}")
	private List<String> keys;

	/**
	 * zookeeper
	 */
	private ZooKeeper zooKeeper;
	/**
	 * 唤醒锁
	 */
	private final ReentrantLock lock = new ReentrantLock();
	/**
	 * 锁条件
	 */
	private final Condition condition = this.lock.newCondition();

	@PostConstruct
	public void init() throws IOException {
		this.zooKeeper = new ZooKeeper(this.address, this.timeout, watchedEvent -> {
			if (Watcher.Event.KeeperState.SyncConnected == watchedEvent.getState()) {
				LOGGER.debug("准备完成：{}-{}", ZookeeperLock.this.address, ZookeeperLock.this.timeout);
				this.buildLockNode();
			}
		});
	}

	@Override
	public boolean set(String key, String value, int ttl) {
		final Map<String, String> map = KEY_LOCAL.get();
		if (map.get(key) != null) {
			// 已经加锁
			return false;
		}
		try {
			final String newKey = this.zooKeeper.create(this.lockPath(key), value.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
			map.put(key, newKey);
		} catch (KeeperException | InterruptedException e) {
			LOGGER.debug("创建节点异常：{}-{}", key, value, e);
		}
		return false;
	}

	@Override
	public String get(String key) {
		try {
			final List<String> list = this.zooKeeper.getChildren(this.rootPath(key), false);
			if (list.isEmpty()) {
				// 正常情况不会出现空值：除非直接解锁
				return null;
			}
			// 排序节点
			Collections.sort(list);
			// 最小节点
			final String lockPath = this.lockPath(key, list.get(0));
			// 当前线程
			final Map<String, String> map = KEY_LOCAL.get();
			if (lockPath.equals(map.get(key))) {
				return VALUE_LOCAL.get();
			}
			// 如果节点已经删除抛出异常：后面直接返回空值递归
			return new String(this.zooKeeper.getData(lockPath, false, null));
		} catch (KeeperException | InterruptedException e) {
			LOGGER.debug("获取节点异常：{}", key, e);
		}
		return null;
	}

	@Override
	public void delete(String key) {
		try {
			final Map<String, String> map = KEY_LOCAL.get();
			this.zooKeeper.delete(map.remove(key), -1);
			if(map.isEmpty()) {
				KEY_LOCAL.remove();
			}
		} catch (KeeperException | InterruptedException e) {
			LOGGER.debug("删除节点异常：{}", key, e);
		}
	}

	@Override
	public int blocking(String key, int duration) {
		final AtomicBoolean await = new AtomicBoolean(true);
		// 观察
		try {
			this.zooKeeper.getChildren(this.rootPath(key), event -> {
				this.lock.lock();
				try {
					await.set(false);
					this.condition.signalAll();
				} finally {
					this.lock.unlock();
				}
			});
		} catch (KeeperException | InterruptedException e) {
			LOGGER.debug("阻塞异常", e);
		}
		// 阻塞
		if(await.get()) {
			this.lock.lock();
			try {
				duration = (int) TimeUnit.NANOSECONDS.toMillis(this.condition.awaitNanos(TimeUnit.MILLISECONDS.toNanos(duration)));
			} catch (InterruptedException e) {
				LOGGER.debug("阻塞异常", e);
			} finally {
				this.lock.unlock();
			}
		}
		return duration;
	}

	/**
	 * 获取锁根路径
	 * 
	 * @return 锁根路径
	 */
	private String rootPath() {
		return "/" + this.root;
	}

	/**
	 * 获取锁根路径
	 * 
	 * @param key 锁名
	 * 
	 * @return 路径
	 */
	private String rootPath(String key) {
		return String.join("/", this.rootPath(), key);
	}

	/**
	 * 获取锁路径
	 * 
	 * @param key 锁名
	 * 
	 * @return 锁路径
	 */
	private String lockPath(String key) {
		return String.join("/", this.rootPath(key), LOCK_NAME);
	}

	/**
	 * 获取锁路径
	 * 
	 * @param key   锁名
	 * @param value 锁值
	 * 
	 * @return 锁路径
	 */
	private String lockPath(String key, String value) {
		return String.join("/", this.rootPath(key), value);
	}

	/**
	 * 创建节点
	 */
	private void buildLockNode() {
		// 根节点
		this.buildLockNode(this.rootPath());
		// 锁节点
		if (this.keys != null) {
			this.keys.forEach(key -> {
				this.buildLockNode(this.rootPath(key));
			});
		}
	}

	/**
	 * 创建节点
	 * 
	 * @param node 节点名称
	 */
	private void buildLockNode(String node) {
		try {
			// 去掉空格
			node = node.trim();
			if (this.zooKeeper.exists(node, false) == null) {
				// 注意临时节点不能有子节点
				this.zooKeeper.create(node, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}
		} catch (KeeperException | InterruptedException e) {
			LOGGER.error("创建节点异常：{}", node, e);
		}
	}

}

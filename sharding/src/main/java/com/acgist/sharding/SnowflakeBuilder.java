package com.acgist.sharding;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SnowflakeBuilder {

	@Value("${system.sn:10}")
	private long sn;
	// 根据并发数量调整
	private int index = 0;
	private int MIN_INDEX = 1000;
	private int MAX_INDEX = MIN_INDEX * 10 - 1;
	private int TIME_SCALE = 1000;
	
	@PostConstruct
	public void init() {
		this.sn = 10000000000L * this.sn;
	}
	
	public long buildId() {
		long id = this.sn + (System.currentTimeMillis() / TIME_SCALE);
		synchronized (this) {
			id = id * MIN_INDEX + this.index;
			if(++this.index > MAX_INDEX) {
				this.index = MIN_INDEX;
			}
		}
		return id;
	}
	
}

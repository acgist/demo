package com.api.core.timer;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务<br>
 * @Async：开启异步支持，不开启此功能，上一次任务没有执行完成将会阻塞下一次任务执行<br>
 * fixedRate：固定时间五秒<br>
 * fixedDelay：执行完成后五秒<br>
 */
@Component
public class APITask {

	@Async
//	@Scheduled(cron = "0/5 * * * * *")
	@Scheduled(fixedRate = 5000)
//	@Scheduled(fixedDelay = 5000)
	public void scheduled() {
		
	}

}

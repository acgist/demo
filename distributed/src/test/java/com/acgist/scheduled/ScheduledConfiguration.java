package com.acgist.scheduled;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import com.acgist.scheduled.aop.DistributedScheduled;

@Configuration
public class ScheduledConfiguration {

    @Scheduled(cron = "*/5 * * * * ?")
    @DistributedScheduled(key = "group-a")
    public void scheduledGroupAA() {
        System.out.println("scheduledGroupA");
    }
    
    @Scheduled(cron = "*/5 * * * * ?")
    @DistributedScheduled(key = "group-b")
    public void scheduledGroupB() {
        System.out.println("scheduledGroupB");
    }
    
}

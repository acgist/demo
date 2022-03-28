package com.acgist.rt.config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * Kafka消费者
 * 
 * @author acgist
 */
@SuppressWarnings("deprecation")
public interface MessageConsumer {

	/**
	 * 告警队列
	 */
	String COLLECTOR_ALARM_INPUT = "collector-alarm-input";
	
	/**
	 * 告警队列
	 */
	@Input(COLLECTOR_ALARM_INPUT)
	SubscribableChannel collectorAlarmInput();
	
}

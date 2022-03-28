package com.acgist.rt.alarm.listener;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.acgist.admin.data.alarm.entity.Alarm;
import com.acgist.boot.utils.JSONUtils;
import com.acgist.collector.data.CollectorEventData;
import com.acgist.rt.alarm.service.AlarmService;
import com.acgist.rt.config.MessageConsumer;

/**
 * 告警监听
 * 
 * @author acgist
 */
@Component
@SuppressWarnings("deprecation")
public class AlarmListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(AlarmListener.class);

	@Autowired
	private AlarmService alarmService;
	
	@StreamListener(MessageConsumer.COLLECTOR_ALARM_INPUT)
	public void collectorBaseMessage(@Header("traceId") String traceId, @Header("emsCode") String emsCode, @Payload String message) {
		LOGGER.info("主动上报，traceId：{}，emsCode：{}，数据：{}", traceId, emsCode, message);
		if (StringUtils.isEmpty(message)) {
			LOGGER.warn("告警数据为空：{}-{}", traceId, emsCode);
			return;
		}
		final CollectorEventData<Alarm> collectorEventData = JSONUtils.toJava(message, new TypeReference<CollectorEventData<Alarm>>() {});
		if (collectorEventData != null && CollectionUtils.isNotEmpty(collectorEventData.getOriginalData())) {
			this.alarmService.pushAlarm(collectorEventData.getOriginalData());
		}
	}

}

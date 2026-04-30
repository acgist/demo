package com.acgist.rt.alarm.action.impl;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.acgist.admin.data.alarm.category.ActionType;
import com.acgist.admin.data.alarm.entity.Alarm;
import com.acgist.rt.alarm.action.AlarmActionExecutor;

/**
 * Kafka执行器
 * 
 * @author acgist
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class KafkaAlarmActionExecutor extends AlarmActionExecutor {

	public KafkaAlarmActionExecutor() {
		super(ActionType.KAFKA);
	}
	
	@Override
	public boolean execute(Alarm alarm) {
		return false;
	}

}

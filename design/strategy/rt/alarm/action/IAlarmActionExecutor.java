package com.acgist.rt.alarm.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.admin.data.alarm.category.ActionType;
import com.acgist.admin.data.alarm.entity.Alarm;
import com.acgist.admin.data.alarm.entity.AlarmAction;

/**
 * 告警动作执行器接口
 * 
 * @author acgist
 */
public interface IAlarmActionExecutor {
	
	static final Logger LOGGER = LoggerFactory.getLogger(IAlarmActionExecutor.class);
	
	/**
	 * @return 告警动作ID
	 */
	Long getActionId();
	
	/**
	 * @return 动作类型
	 */
	ActionType getActionType();
	
	/**
	 * 设置告警动作
	 * 
	 * @param action 动作
	 * 
	 * @return this
	 */
	IAlarmActionExecutor setAction(AlarmAction action);
	
	/**
	 * 执行动作
	 * 
	 * @param alarm 告警
	 * 
	 * @return 是否成功
	 */
	boolean execute(Alarm alarm);
	
	/**
	 * 初始化
	 */
	void init();

	/**
	 * 重新加载
	 */
	void reload();
	
	/**
	 * 销毁
	 */
	void destory();
	
}

package com.acgist.rt.alarm.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.admin.data.alarm.category.ActionType;
import com.acgist.admin.data.alarm.entity.AlarmAction;

/**
 * 告警动作执行器
 * 
 * @author acgist
 */
public abstract class AlarmActionExecutor implements IAlarmActionExecutor {

	private static final Logger LOGGER = LoggerFactory.getLogger(AlarmActionExecutor.class);
	
	/**
	 * 告警动作
	 */
	protected final ActionType actionType;
	/**
	 * 告警动作
	 */
	protected AlarmAction alarmAction;
	/**
	 * 是否需要重新加载
	 */
	private boolean reloadable = true;
	
	protected AlarmActionExecutor(ActionType actionType) {
		this.actionType = actionType;
	}

	@Override
	public Long getActionId() {
		return this.alarmAction == null ? null : this.alarmAction.getActionId();
	}
	
	@Override
	public ActionType getActionType() {
		return this.actionType;
	}
	
	@Override
	public IAlarmActionExecutor setAction(AlarmAction action) {
		if(this.alarmAction == null) {
			this.reloadable = true;
		} else if(this.alarmAction.getUpdatedTime().compareTo(action.getUpdatedTime()) < 0) {
			this.reloadable = true;
		} else {
			this.reloadable = false;
		}
		this.alarmAction = action;
		return this;
	}
	
	@Override
	public void init() {
		LOGGER.info("初始告警动作执行器：{}-{}", this.getActionType(), this.getActionId());
	}
	
	@Override
	public void reload() {
		if(this.actionType.getPassive()) {
			// 被动连接不要重新加载
			return;
		}
		if(this.reloadable) {
			this.destory();
			this.init();
		}
	}
	
	@Override
	public void destory() {
		LOGGER.info("销毁告警动作执行器：{}-{}", this.getActionType(), this.getActionId());
	}
	
}

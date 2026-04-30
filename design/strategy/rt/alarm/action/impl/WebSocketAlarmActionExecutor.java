package com.acgist.rt.alarm.action.impl;

import javax.websocket.Session;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.acgist.admin.data.alarm.category.ActionType;
import com.acgist.admin.data.alarm.entity.Alarm;
import com.acgist.boot.utils.JSONUtils;
import com.acgist.rt.alarm.action.AlarmActionExecutor;

/**
 * WebSocket执行器
 * 
 * @author acgist
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class WebSocketAlarmActionExecutor extends AlarmActionExecutor {
	
	/**
	 * WebSocket Session
	 */
	private Session session;
	
	public WebSocketAlarmActionExecutor() {
		super(ActionType.WEBSOCKET);
	}

	/**
	 * @param session WebSocket Session
	 */
	public void setSession(Session session) {
		this.session = session;
	}
	
	@Override
	public boolean execute(Alarm alarm) {
		this.session.getAsyncRemote().sendText(JSONUtils.toJSON(alarm));
		return true;
	}

}
package com.acgist.rt.alarm.websocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.acgist.admin.data.alarm.category.ActionType;
import com.acgist.rt.alarm.action.IAlarmActionExecutor;
import com.acgist.rt.alarm.action.impl.WebSocketAlarmActionExecutor;
import com.acgist.rt.alarm.service.AlarmService;
import com.acgist.rt.alarm.service.impl.MappingFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * 告警WebSocket
 * 
 * @author acgist
 */
@Slf4j
@Component
@ServerEndpoint("/rt/alarm/websocket/{ruleId}")
public class AlarmWebSocket {

	private static AlarmService alarmService;
	private static MappingFactory mappingFactory;
	
	/**
	 * 保存所有连接
	 */
	private Map<Session, IAlarmActionExecutor> sessions = new ConcurrentHashMap<>();

	@OnOpen
	public void open(@PathParam("ruleId") Long ruleId, Session session) {
		log.info("WebSocket打开：{}-{}", ruleId, session.getId());
		final IAlarmActionExecutor executor = AlarmWebSocket.mappingFactory.buildExecutor(ActionType.WEBSOCKET);
		// 设置session信息
		final WebSocketAlarmActionExecutor websocketExecutor = (WebSocketAlarmActionExecutor) executor;
		websocketExecutor.setSession(session);
		// 保存连接
		this.sessions.put(session, executor);
		AlarmWebSocket.alarmService.putExecutor(ruleId, executor);
	}
	
	@OnMessage
	public void message(String message, Session session) {
		// 直接忽略
		log.info("WebSocket接收消息：{}", message);
	}

	@OnClose
	public void close(@PathParam("ruleId") Long ruleId, Session session) {
		log.info("WebSocket关闭：{}-{}", ruleId, session.getId());
		final IAlarmActionExecutor executor = this.sessions.remove(session);
		AlarmWebSocket.alarmService.removeExecutor(ruleId, executor);
	}

	@OnError
	public void error(@PathParam("ruleId") Long ruleId, Session session, Throwable e) {
		log.error("WebSocket异常：{}-{}", ruleId, session.getId(), e);
		this.close(ruleId, session);
	}

	@Autowired
	public  void setAlarmService(AlarmService alarmService) {
		AlarmWebSocket.alarmService = alarmService;
	}

	@Autowired
	public  void setMappingFactory(MappingFactory mappingFactory) {
		AlarmWebSocket.mappingFactory = mappingFactory;
	}
	
}

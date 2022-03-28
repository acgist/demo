package com.acgist.rt.alarm.service.impl;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.acgist.admin.data.alarm.category.ActionType;
import com.acgist.admin.data.alarm.category.ConditionOperation;
import com.acgist.rt.alarm.action.IAlarmActionExecutor;
import com.acgist.rt.alarm.condition.ICondition;

import lombok.extern.slf4j.Slf4j;

/**
 * 枚举映射工厂
 * 
 * @author acgist
 */
@Slf4j
@Component
public class MappingFactory {

	@Autowired
	private ApplicationContext context;
	
	@PostConstruct
	public void init() {
		log.info("初始化枚举映射");
		this.context.getBeansOfType(IAlarmActionExecutor.class).forEach((name, instance) -> {
			log.info("创建动作类型和执行器映射：{}-{}", instance.getActionType(), name);
			this.executorMapping.put(instance.getActionType(), () -> this.context.getBean(instance.getClass()));
		});
		this.context.getBeansOfType(ICondition.class).forEach((name, instance) -> {
			log.info("创建规则条件和条件执行器映射：{}-{}", instance.getConditionOperation(), name);
			this.conditionMapping.put(instance.getConditionOperation(), instance);
		});
	}

	/**
	 * 动作类型和执行器映射：多例
	 * 
	 * @see #init()
	 */
	private Map<ActionType, Supplier<IAlarmActionExecutor>> executorMapping = new EnumMap<>(ActionType.class) {
		private static final long serialVersionUID = 1L;
		{
//			this.put(ActionType.SMS, SmsAlarmActionExecutor::new);
//			this.put(ActionType.REST, RestAlarmActionExecutor::new);
//			this.put(ActionType.KAFKA, KafkaAlarmActionExecutor::new);
//			this.put(ActionType.DISPATCH, DispatchAlarmActionExecutor::new);
//			this.put(ActionType.WEBSOCKET, WebSocketAlarmActionExecutor::new);
		}
	};
	
	/**
	 * 规则条件和条件执行器映射：单例
	 * 
	 * @see #init()
	 */
	private Map<ConditionOperation, ICondition> conditionMapping = new EnumMap<>(ConditionOperation.class) {
		private static final long serialVersionUID = 1L;
		{
//			this.put(ConditionOperation.EQ, new EqCondition());
//			this.put(ConditionOperation.NE, new NeCondition());
//			this.put(ConditionOperation.LT, new LtCondition());
//			this.put(ConditionOperation.GT, new GtCondition());
//			this.put(ConditionOperation.LE, new LeCondition());
//			this.put(ConditionOperation.GE, new GeCondition());
//			this.put(ConditionOperation.IN, new InCondition());
//			this.put(ConditionOperation.NOT_IN, new NotInCondition());
//			this.put(ConditionOperation.INCLUDE, new IncludeCondition());
//			this.put(ConditionOperation.EXCLUDE, new ExcludeCondition());
		}
	};
	
	/**
	 * 创建执行器
	 * 
	 * @param actionType 执行器类型
	 * 
	 * @return 执行器
	 */
	public IAlarmActionExecutor buildExecutor(ActionType actionType) {
		return this.executorMapping.get(actionType).get();
	}

	/**
	 * 获取条件执行器
	 * 
	 * @param operation 条件操作类型
	 * 
	 * @return 条件执行器
	 */
	public ICondition getCondition(ConditionOperation operation) {
		return this.conditionMapping.get(operation);
	}
	
}


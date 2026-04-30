package com.acgist.rt.alarm.rule;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.acgist.admin.data.alarm.category.ActionType;
import com.acgist.admin.data.alarm.category.RuleLogic;
import com.acgist.admin.data.alarm.entity.Alarm;
import com.acgist.admin.data.alarm.entity.AlarmAction;
import com.acgist.admin.data.alarm.entity.AlarmRule;
import com.acgist.admin.data.alarm.entity.AlarmRuleCondition;
import com.acgist.rt.alarm.action.IAlarmActionExecutor;
import com.acgist.rt.alarm.service.impl.MappingFactory;

/**
 * 告警过滤规则过滤器
 * 
 * @author acgist
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class AlarmRuleFilter implements IAlarmRuleFilter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AlarmRuleFilter.class);

	@Autowired
	private MappingFactory mappingFactory;
	
	/**
	 * 规则
	 */
	private AlarmRule alarmRule;
	/**
	 * 规则条件
	 */
	private List<AlarmRuleCondition> conditions;
	/**
	 * 执行器
	 */
	private final List<IAlarmActionExecutor> executors = new CopyOnWriteArrayList<IAlarmActionExecutor>();
	
	@Override
	public Long getRuleId() {
		return this.alarmRule.getRuleId();
	}

	@Override
	public boolean filter(Alarm alarm) {
		final boolean logicOr = this.alarmRule.getRuleLogic() == RuleLogic.OR;
		if(logicOr) {
			return this.conditions.stream()
				.map(condition -> this.mappingFactory.getCondition(condition.getConditionOperation()).filter(alarm, this.alarmRule, condition))
				.anyMatch(value -> value);
		} else {
			return this.conditions.stream()
				.map(condition -> this.mappingFactory.getCondition(condition.getConditionOperation()).filter(alarm, this.alarmRule, condition))
				.allMatch(value -> value);
		}
	}
	
	@Override
	public Wrapper<Alarm> buildWrapper() {
		final QueryWrapper<Alarm> wrapper = Wrappers.query();
		this.conditions.forEach(condition -> {
			this.mappingFactory.getCondition(condition.getConditionOperation()).buildWrapper(Alarm.class, wrapper, this.alarmRule, condition);
		});
		return wrapper;
	}
	
	@Override
	public boolean pushAlarm(Alarm alarm) {
		this.executors.forEach(executor -> {
			try {
				executor.execute(alarm);
			} catch (Exception e) {
				LOGGER.error("执行告警动作异常：{}-{}-{}", alarm.getAlarmId(), executor.getActionType(), executor.getActionId(), e);
			}
		});
		return true;
	}

	@Override
	public void setRule(AlarmRule alarmRule) {
		this.alarmRule = alarmRule;
	}
	
	@Override
	public void setConditions(List<AlarmRuleCondition> conditions) {
		this.conditions = conditions;
	}
	
	@Override
	public void buildExecutor(List<AlarmAction> list) {
		final Set<Long> set = new HashSet<>();
		list.forEach(alarmAction -> {
			set.add(alarmAction.getActionId());
			final IAlarmActionExecutor executor = this.executors.stream()
				.filter(value -> Objects.equals(value.getActionId(), alarmAction.getActionId()))
				.findFirst().orElse(null);
			if(executor == null) {
				final ActionType actionType = alarmAction.getActionType();
				if(actionType.getPassive()) {
					// 被动动作不要主动创建
					return;
				}
				this.putExecutor(this.mappingFactory.buildExecutor(actionType).setAction(alarmAction));
			} else if(alarmAction != null) {
				executor.setAction(alarmAction).reload();
			}
		});
		// 删除无效执行器
		this.executors.stream()
			.filter(value -> !set.contains(value.getActionId()))
			.forEach(executor -> {
				if(executor.getActionType().getPassive()) {
					// 忽略被动连接
					return;
				}
				this.removeExecutor(executor);
			});
	}
	
	@Override
	public Boolean putExecutor(IAlarmActionExecutor executor) {
		LOGGER.info("添加告警动作执行器：{}-{}", executor.getActionType(), executor.getActionId());
		executor.init();
		return this.executors.add(executor);
	}

	@Override
	public Boolean removeExecutor(IAlarmActionExecutor executor) {
		LOGGER.info("删除告警动作执行器：{}-{}", executor.getActionType(), executor.getActionId());
		executor.destory();
		return this.executors.remove(executor);
	}
	
	@Override
	public void destory() {
		LOGGER.info("销毁告警规则过滤器：{}", this.getRuleId());
		this.executors.forEach(this::removeExecutor);
	}

}

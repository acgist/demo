package com.acgist.rt.alarm.service.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.acgist.admin.data.alarm.entity.Alarm;
import com.acgist.admin.data.alarm.entity.AlarmRule;
import com.acgist.admin.data.alarm.entity.AlarmRuleCondition;
import com.acgist.rt.alarm.action.IAlarmActionExecutor;
import com.acgist.rt.alarm.dao.mapper.AlarmActionMapper;
import com.acgist.rt.alarm.dao.mapper.AlarmMapper;
import com.acgist.rt.alarm.dao.mapper.AlarmRuleConditionMapper;
import com.acgist.rt.alarm.dao.mapper.AlarmRuleMapper;
import com.acgist.rt.alarm.rule.IAlarmRuleFilter;
import com.acgist.rt.alarm.service.AlarmService;

@Service
public class AlarmServiceImpl implements AlarmService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AlarmServiceImpl.class);

	/**
	 * 过滤队列
	 */
	private List<IAlarmRuleFilter> filters = new CopyOnWriteArrayList<>();
	
	@Autowired
	private AlarmMapper alarmMapper;
	@Autowired
	private ApplicationContext context;
	@Autowired
	private AlarmRuleMapper alarmRuleMapper;
	@Autowired
	private AlarmActionMapper alarmActionMapper;
	@Autowired
	private AlarmRuleConditionMapper alarmRuleConditionMapper;
	
	@Override
	public Boolean reload() {
		LOGGER.info("加载告警规则过滤器和动作执行器");
		final Set<Long> set = new HashSet<>();
		this.alarmRuleMapper
			.selectList(Wrappers.lambdaQuery(AlarmRule.class).eq(AlarmRule::getRuleStatus, Boolean.TRUE))
			.forEach(rule -> {
				set.add(rule.getRuleId());
				this.reloadRule(rule);
			});
		// 删除规则过滤器
		final Iterator<IAlarmRuleFilter> iterator = this.filters.iterator();
		while(iterator.hasNext()) {
			final IAlarmRuleFilter filter = iterator.next();
			if(!set.contains(filter.getRuleId())) {
				filter.destory();
				iterator.remove();
			}
		}
		return true;
	}
	
	/**
	 * 加载规则
	 */
	private void reloadRule(AlarmRule alarmRule) {
		final Long ruleId = alarmRule.getRuleId();
		final IAlarmRuleFilter alarmRuleFilter = this.alarmRuleFilter(ruleId);
		// 加载规则
		final List<AlarmRuleCondition> conditions = this.alarmRuleConditionMapper
			.selectList(Wrappers.lambdaQuery(AlarmRuleCondition.class).eq(AlarmRuleCondition::getRuleId, ruleId));
		alarmRuleFilter.setRule(alarmRule);
		alarmRuleFilter.setConditions(conditions);
		alarmRuleFilter.buildExecutor(this.alarmActionMapper.selectByRuleId(ruleId));
	}

	@Override
	public Page<Alarm> page(Long ruleId, Page<Alarm> page) {
		return this.alarmMapper.selectPage(page, this.alarmRuleFilter(ruleId).buildWrapper());
	}
	
	/**
	 * 查询告警规则过滤器
	 * 
	 * @param ruleId 规则ID
	 * 
	 * @return 规则过滤器
	 */
	private IAlarmRuleFilter alarmRuleFilter(Long ruleId) {
		return this.filters.stream()
			.filter(filter -> Objects.equals(ruleId, filter.getRuleId()))
			.findFirst()
			.orElseGet(() -> {
				final IAlarmRuleFilter alarmRuleFilter = this.context.getBean(IAlarmRuleFilter.class);
				this.filters.add(alarmRuleFilter);
				return alarmRuleFilter;
			});
	}
	
	@Override
	public Boolean putExecutor(Long ruleId, IAlarmActionExecutor executor) {
		if(executor == null) {
			return false;
		}
		return this.alarmRuleFilter(ruleId).putExecutor(executor);
	}

	@Override
	public Boolean removeExecutor(Long ruleId, IAlarmActionExecutor executor) {
		if(executor == null) {
			return false;
		}
		return this.alarmRuleFilter(ruleId).removeExecutor(executor);
	}

	@Async
	@Override
	public void pushAlarm(Alarm alarm) {
		this.filters.forEach(filter -> {
			if(filter.filter(alarm)) {
				filter.pushAlarm(alarm);
			} else {
				LOGGER.info("告警匹配失败忽略：{}-{}", alarm.getAlarmId(), filter.getRuleId());
			}
		});
	}
	
	@Async
	@Override
	public void pushAlarm(List<Alarm> list) {
		list.forEach(alarm -> this.pushAlarm(alarm));
	}

}

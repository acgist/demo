package com.acgist.rt.alarm.condition;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.acgist.admin.data.alarm.category.ConditionOperation;
import com.acgist.admin.data.alarm.entity.Alarm;
import com.acgist.admin.data.alarm.entity.AlarmRule;
import com.acgist.admin.data.alarm.entity.AlarmRuleCondition;

/**
 * 条件判断
 * 
 * @author acgist
 */
public interface ICondition {
	
	/**
	 * @return 条件操作类型
	 */
	ConditionOperation getConditionOperation();

	/**
	 * 过滤规则
	 * 
	 * @param alarm 告警
	 * @param rule 规则
	 * @param condition 条件
	 * 
	 * @return 是否匹配
	 */
	boolean filter(Alarm alarm, AlarmRule rule, AlarmRuleCondition condition);
	
	/**
	 * 创建查询条件
	 * 
	 * @param entityClazz 查询类型
	 * @param wrapper 查询条件
	 * @param rule 规则
	 * @param condition 条件
	 */
	<T> void buildWrapper(Class<T> entityClazz, QueryWrapper<T> wrapper, AlarmRule rule, AlarmRuleCondition condition);
	
}

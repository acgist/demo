package com.acgist.rt.alarm.rule;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.acgist.admin.data.alarm.entity.Alarm;
import com.acgist.admin.data.alarm.entity.AlarmAction;
import com.acgist.admin.data.alarm.entity.AlarmRule;
import com.acgist.admin.data.alarm.entity.AlarmRuleCondition;
import com.acgist.rt.alarm.action.IAlarmActionExecutor;

/**
 * 告警过滤规则过滤器
 * 
 * @author acgist
 */
public interface IAlarmRuleFilter {
	
	/**
	 * @return 告警过滤规则ID
	 */
	Long getRuleId();
	
	/**
	 * 告警过滤
	 * 
	 * @param alarm 告警
	 * 
	 * @return 是否过滤：true-通过；false-不通过；
	 */
	boolean filter(Alarm alarm);
	
	/**
	 * 创建查询Wrapper
	 * 
	 * @return Wrapper
	 */
	Wrapper<Alarm> buildWrapper();
	
	/**
	 * 执行动作
	 * 
	 * @param alarm 告警
	 * 
	 * @return 是否成功
	 */
	boolean pushAlarm(Alarm alarm);
	
	/**
	 * 设置规则
	 * 
	 * @param alarmRule 告警规则
	 */
	void setRule(AlarmRule alarmRule);
	
	/**
	 * 设置告警条件
	 * 
	 * @param conditions 告警条件
	 */
	void setConditions(List<AlarmRuleCondition> conditions);
	
	/**
	 * 加载动作执行器
	 * 
	 * @param alarmAction 动作
	 */
	void buildExecutor(List<AlarmAction> list);
	
	/**
	 * 添加动作执行器
	 * 
	 * @param executor 动作执行器
	 * 
	 * @return 是否成功
	 */
	Boolean putExecutor(IAlarmActionExecutor executor);
	
	/**
	 * 删除动作执行器
	 * 
	 * @param executor 动作执行器
	 * 
	 * @return 是否成功
	 */
	Boolean removeExecutor(IAlarmActionExecutor executor);
	
	/**
	 * 销毁
	 */
	void destory();
	
}

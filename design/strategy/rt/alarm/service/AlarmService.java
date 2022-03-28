package com.acgist.rt.alarm.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.acgist.admin.data.alarm.entity.Alarm;
import com.acgist.rt.alarm.action.IAlarmActionExecutor;

/**
 * 告警
 * 
 * @author acgist
 */
public interface AlarmService {

	/**
	 * 加载告警规则和告警动作
	 * 
	 * @return 是否成功
	 */
	Boolean reload();
	
	/**
	 * 分页查询
	 * 
	 * @param ruleId 告警规则ID
	 * @param page 分页信息
	 * 
	 * @return 分页数据
	 */
	Page<Alarm> page(Long ruleId, Page<Alarm> page);
	
	/**
	 * 添加动作执行器
	 * 
	 * @param ruleId 规则ID
	 * @param executor 动作执行器
	 * 
	 * @return 是否成功
	 */
	Boolean putExecutor(Long ruleId, IAlarmActionExecutor executor);
	
	/**
	 * 删除动作执行器
	 * 
	 * @param ruleId 规则ID
	 * @param executor 动作执行器
	 * 
	 * @return 是否成功
	 */
	Boolean removeExecutor(Long ruleId, IAlarmActionExecutor executor);
	
	/**
	 * 推送告警
	 * 
	 * @param alarm 告警
	 */
	void pushAlarm(Alarm alarm);
	
	/**
	 * 推送告警
	 * 
	 * @param list 告警列表
	 */
	void pushAlarm(List<Alarm> list);
	
}

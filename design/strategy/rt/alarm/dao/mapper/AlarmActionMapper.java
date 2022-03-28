package com.acgist.rt.alarm.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.acgist.admin.data.alarm.entity.AlarmAction;
import com.acgist.boot.dao.mapper.BootMapper;

/**
 * 告警动作
 * 
 * @author yusheng
 */
@Mapper
public interface AlarmActionMapper extends BootMapper<AlarmAction> {

	/**
	 * 根据规则ID查询所有动作
	 * 
	 * @param ruleId 规则ID
	 * 
	 * @return 所有动作
	 */
	List<AlarmAction> selectByRuleId(Long ruleId);
	
}

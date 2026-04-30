package com.acgist.rt.alarm.dao.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.acgist.admin.data.alarm.entity.AlarmRuleCondition;
import com.acgist.boot.dao.mapper.BootMapper;

/**
 * 告警过滤规则条件
 * 
 * @author yusheng
 */
@Mapper
public interface AlarmRuleConditionMapper extends BootMapper<AlarmRuleCondition> {

}

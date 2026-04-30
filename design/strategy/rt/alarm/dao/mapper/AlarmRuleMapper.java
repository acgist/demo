package com.acgist.rt.alarm.dao.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.acgist.admin.data.alarm.entity.AlarmRule;
import com.acgist.boot.dao.mapper.BootMapper;

/**
 * 告警过滤规则
 * 
 * @author yusheng
 */
@Mapper
public interface AlarmRuleMapper extends BootMapper<AlarmRule> {

}

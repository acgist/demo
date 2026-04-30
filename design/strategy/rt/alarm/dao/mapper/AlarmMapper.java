package com.acgist.rt.alarm.dao.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.acgist.admin.data.alarm.entity.Alarm;
import com.acgist.boot.dao.mapper.BootMapper;

/**
 * 告警
 * 
 * @author yusheng
 */
@Mapper
public interface AlarmMapper extends BootMapper<Alarm> {

}

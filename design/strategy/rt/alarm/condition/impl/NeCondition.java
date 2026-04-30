package com.acgist.rt.alarm.condition.impl;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.acgist.admin.data.alarm.category.ConditionOperation;
import com.acgist.boot.FilterQuery.Filter;
import com.acgist.rt.alarm.condition.Condition;

/**
 * 不等条件
 * 
 * @author acgist
 */
@Component
public class NeCondition extends Condition {

	public NeCondition() {
		super(ConditionOperation.NE);
	}
	
	@Override
	public boolean filter(String fieldValue, Class<?> fieldClazz, Object fieldValueRt) {
		return !this.equals(fieldValueRt, fieldValue);
	}

	@Override
	protected <T> void buildWrapper(boolean logicOr, Class<T> entityClazz, String field, Class<?> fieldClazz, String fieldValue, QueryWrapper<T> wrapper) {
		Filter.Type.NE.of(field, getObject(fieldClazz, fieldValue)).predicate(entityClazz, logicOr ? wrapper.or() : wrapper);
	}
	
}

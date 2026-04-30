package com.acgist.rt.alarm.condition.impl;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.acgist.admin.data.alarm.category.ConditionOperation;
import com.acgist.boot.FilterQuery.Filter;
import com.acgist.rt.alarm.condition.Condition;

/**
 * 不在列表中条件
 * 
 * @author acgist
 */
@Component
public class NotInCondition extends Condition {

	public NotInCondition() {
		super(ConditionOperation.NOT_IN);
	}
	
	@Override
	public boolean filter(String fieldValue, Class<?> fieldClazz, Object fieldValueRt) {
		return this.getList(fieldClazz, fieldValue).stream()
			.noneMatch(value -> this.equals(fieldValueRt, value));
	}

	@Override
	protected <T> void buildWrapper(boolean logicOr, Class<T> entityClazz, String field, Class<?> fieldClazz, String fieldValue, QueryWrapper<T> wrapper) {
		Filter.Type.NOT_IN.of(field, this.getList(fieldClazz, fieldValue)).predicate(entityClazz, logicOr ? wrapper.or() : wrapper);
	}
	
}

package com.acgist.rt.alarm.condition.impl;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.acgist.admin.data.alarm.category.ConditionOperation;
import com.acgist.boot.FilterQuery.Filter;
import com.acgist.rt.alarm.condition.Condition;

/**
 * 包含文本条件
 * 
 * @author acgist
 */
@Component
public class IncludeCondition extends Condition {

	public IncludeCondition() {
		super(ConditionOperation.INCLUDE);
	}
	
	@Override
	public boolean filter(String fieldValue, Class<?> fieldClazz, Object fieldValueRt) {
		return this.getList(fieldClazz, fieldValue).stream()
			.anyMatch(value -> this.contains(fieldValueRt, value));
	}

	@Override
	protected <T> void buildWrapper(boolean logicOr, Class<T> entityClazz, String field, Class<?> fieldClazz, String fieldValue, QueryWrapper<T> wrapper) {
		if(logicOr) {
			wrapper.or(includeWrapper -> {
				this.getList(fieldClazz, fieldValue).forEach(item -> {
					Filter.Type.LIKE.of(field, item).predicate(entityClazz, includeWrapper.or());
				});
			});
		} else {
			wrapper.and(includeWrapper -> {
				this.getList(fieldClazz, fieldValue).forEach(item -> {
					Filter.Type.LIKE.of(field, item).predicate(entityClazz, includeWrapper.or());
				});
			});
		}
	}
	
}

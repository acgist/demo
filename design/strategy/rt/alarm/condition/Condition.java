package com.acgist.rt.alarm.condition;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.acgist.admin.data.alarm.category.ConditionOperation;
import com.acgist.admin.data.alarm.category.RuleLogic;
import com.acgist.admin.data.alarm.entity.Alarm;
import com.acgist.admin.data.alarm.entity.AlarmRule;
import com.acgist.admin.data.alarm.entity.AlarmRuleCondition;
import com.acgist.boot.utils.DateUtils;
import com.acgist.boot.utils.JSONUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 条件
 * 
 * 注意：不要保留任何状态，保证单例线程安全。
 * 
 * @author acgist
 */
@Slf4j
public abstract class Condition implements ICondition {
	
	/**
	 * 过滤操作类型
	 */
	protected final ConditionOperation operation;
	
	protected Condition(ConditionOperation operation) {
		this.operation = operation;
	}
	
	@Override
	public ConditionOperation getConditionOperation() {
		return this.operation;
	}
	
	@Override
	public boolean filter(Alarm alarm, AlarmRule rule, AlarmRuleCondition condition) {
		final String field = condition.getConditionField();
		final String fieldValue = condition.getConditionValue();
		final Class<?> fieldClazz = this.getType(alarm.getClass(), field);
		final Object fieldValueRt = this.getValue(alarm, field);
		return this.filter(fieldValue, fieldClazz, fieldValueRt);
	}
	
	@Override
	public <T> void buildWrapper(Class<T> entityClazz, QueryWrapper<T> wrapper, AlarmRule rule, AlarmRuleCondition condition) {
		final boolean logicOr = this.getLogic(rule);
		final String field = condition.getConditionField();
		final String fieldValue = condition.getConditionValue();
		final Class<?> fieldClazz = this.getType(entityClazz, field);
		this.buildWrapper(logicOr, entityClazz, field, fieldClazz, fieldValue, wrapper);
	}
	
	/**
	 * 过滤条件
	 * 
	 * @param fieldValue 字段值
	 * @param fieldClazz 字段类型
	 * @param fieldValueRt 字段当前值
	 * 
	 * @return 是否过滤：true-通过；false-不通过；
	 */
	protected abstract boolean filter(String fieldValue, Class<?> fieldClazz, Object fieldValueRt);
	
	/**
	 * 创建条件
	 * 
	 * @param <T> 类型
	 * 
	 * @param logicOr 逻辑运算符号
	 * @param entityClazz 查询类型
	 * @param field 字段名
	 * @param fieldClazz 字段类型
	 * @param fieldValue 字段值
	 * @param wrapper 查询条件
	 */
	protected abstract <T> void buildWrapper(boolean logicOr, Class<T> entityClazz, String field, Class<?> fieldClazz, String fieldValue, QueryWrapper<T> wrapper);

	/**
	 * 获取逻辑运算符号
	 * 
	 * @param rule 规则
	 * 
	 * @return 逻辑运算符号
	 */
	protected boolean getLogic(AlarmRule rule) {
		return rule.getRuleLogic() == RuleLogic.OR;
	}
	
	/**
	 * 获取字段类型
	 * 
	 * @param clazz 类型
	 * @param field 字段
	 * 
	 * @return 字段类型
	 */
	protected Class<?> getType(Class<?> clazz, String field) {
		final Field value = FieldUtils.getField(clazz, field, true);
		Objects.requireNonNull(value, "条件字段不存在：" + field);
		return value.getType();
	}
	
	/**
	 * 读取字段值
	 * 
	 * @param alarm 告警
	 * @param field 字段
	 * 
	 * @return 字段值
	 */
	protected Object getValue(Alarm alarm, String field) {
		final Field value = FieldUtils.getField(alarm.getClass(), field, true);
		Objects.requireNonNull(value, "条件字段不存在：" + field);
		try {
			return value.get(alarm);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			log.error("读取属性异常", e);
		}
		return null;
	}
	
	/**
	 * 获取单值
	 * 
	 * @param clazz 类型
	 * @param value 条件
	 * 
	 * @return 单值
	 */
	protected Object getObject(Class<?> clazz, String value) {
		if(Long.TYPE.isAssignableFrom(clazz)) {
			return Long.valueOf(value);
		}
		if(Integer.TYPE.isAssignableFrom(clazz)) {
			return Integer.valueOf(value);
		}
		if(Number.class.isAssignableFrom(clazz)) {
			return Long.valueOf(value);
		}
		if(Boolean.TYPE.isAssignableFrom(clazz)) {
			return Boolean.valueOf(value);
		}
		if(Date.class.isAssignableFrom(clazz)) {
			return DateUtils.parse(value);
		}
		// 时间、字符串等不用转换
		return value;
	}
	
	/**
	 * 获取数组
	 * 
	 * @param clazz 类型
	 * @param value 条件
	 * 
	 * @return 数组
	 */
	protected <T> List<T> getList(Class<T> clazz, String value) {
		return JSONUtils.toList(value, clazz);
	}
	
	/**
	 * 判断内容是否相等
	 * 
	 * @param source 原始对象
	 * @param target 目标对象
	 * 
	 * @return 是否相等
	 */
	protected boolean equals(Object source, Object target) {
		// 支持配置空值
		return source == target || String.valueOf(source).equals(target);
	}
	
	/**
	 * 判断内容是否包含
	 * 
	 * @param source 原始对象
	 * @param target 包含对象
	 * 
	 * @return 是否包含
	 */
	protected boolean contains(Object source, Object target) {
		return String.valueOf(source).contains(String.valueOf(target));
	}
	
	/**
	 * 比较当前值和配置值
	 * 
	 * @param fieldValue 字段值
	 * @param fieldClazz 字段类型
	 * @param fieldValueRt 字段当前值
	 * 
	 * @return 1：大于；0：等于；-1：小于；
	 */
	protected int compare(String fieldValue, Class<?> fieldClazz, Object fieldValueRt) {
		if(Long.TYPE.isAssignableFrom(fieldClazz)) {
			return ((Long) fieldValueRt).compareTo(Long.valueOf(fieldValue));
		}
		if(Integer.TYPE.isAssignableFrom(fieldClazz)) {
			return ((Integer) fieldValueRt).compareTo(Integer.valueOf(fieldValue));
		}
		// 其他转为Long比较
		if(Number.class.isAssignableFrom(fieldClazz)) {
			return Long.valueOf(String.valueOf(fieldValueRt)).compareTo(Long.valueOf(fieldValue));
		}
		if(Date.class.isAssignableFrom(fieldClazz)) {
			return ((Date) fieldValueRt).compareTo(DateUtils.parse(fieldValue));
		}
		return String.valueOf(fieldValueRt).compareTo(fieldValue);
	}
	
}

package com.api.data.asyn.pojo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.api.data.pojo.entity.BaseEntity;

/**
 * entity - 系统日志
 */
@Entity
@Table(name = "ts_log")
public class LogEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 日志类型
	 */
	public enum LogType {
		system, // 系统
		service, // 服务
		gateway, // 接口
		www // 网页
	}

	/**
	 * 日志类型
	 */
	public enum LogLevel {
		info, // 普通
		warn // 重要
	}

	/**
	 * 日志类型
	 */
	@NotNull
	private LogType type;
	/**
	 * 日志级别
	 */
	@NotNull
	private LogLevel level;
	/**
	 * 操作名称
	 */
	private String name;
	/**
	 * 操作人员
	 */
	private String operator;
	/**
	 * 请求描述：请求IP，请求方法，请求地址，请求参数
	 */
	private String memo;

	public LogType getType() {
		return type;
	}

	public void setType(LogType type) {
		this.type = type;
	}

	public LogLevel getLevel() {
		return level;
	}

	public void setLevel(LogLevel level) {
		this.level = level;
	}

	@Column(length = 20)
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Column(length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Lob
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}

package com.api.data.pojo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * entity - 系统权限
 */
@Entity
@Table(name = "ts_permission", indexes = {
	@Index(name = "index_permission_parent", columnList = "parent")
})
public class PermissionEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 名称
	 */
	@Size(max = 20, message = "系统权限名称长度不能超过20")
	@NotBlank(message = "系统权限名称不能为空")
	private String name;
	/**
	 * 匹配规则
	 */
	@Size(max = 50, message = "系统权限匹配规则长度不能超过50")
	@NotBlank(message = "系统权限匹配规则不能为空")
	private String pattern;
	/**
	 * 描述
	 */
	@Size(max = 100, message = "系统权限描述长度不能超过100")
	private String memo;
	/**
	 * 父级菜单
	 */
	@Size(max = 32, message = "系统权限父级菜单长度不能超过32")
	private String parent;
	/**
	 * 排序
	 */
	private Short sort;

	@Column(length = 20, nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(length = 50, nullable = false)
	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	@Column(length = 100)
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(length = 32)
	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public Short getSort() {
		return sort;
	}

	public void setSort(Short sort) {
		this.sort = sort;
	}

}

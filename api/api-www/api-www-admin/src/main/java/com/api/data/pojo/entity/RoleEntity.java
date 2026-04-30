package com.api.data.pojo.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * entity - 系统角色
 */
@Entity
@Table(name = "ts_role")
public class RoleEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	public static final String PROPERTY_PERMISSIONS = "permissions";
	
	/**
	 * 名称
	 */
	@Size(max = 20, message = "系统角色名称长度不能超过20")
	@NotBlank(message = "系统角色名称不能为空")
	private String name;
	/**
	 * 描述
	 */
	@Size(max = 100, message = "系统角色描述长度不能超过100")
	private String memo;
	/**
	 * 权限
	 */
	@JsonIgnore
	private List<PermissionEntity> permissions;

	@Column(length = 20, nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(length = 100)
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	@JoinTable(
		name = "ts_role_permission",
		joinColumns = @JoinColumn(
			name = "role_id",
			referencedColumnName = "id",
			foreignKey = @ForeignKey(name = "key_role_permission_role_id")
		),
		inverseJoinColumns = @JoinColumn(
			name = "permission_id",
			referencedColumnName = "id",
			foreignKey = @ForeignKey(name = "key_role_permission_permission_id")
		)
	)
	public List<PermissionEntity> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<PermissionEntity> permissions) {
		this.permissions = permissions;
	}

	public boolean hasPermission(PermissionEntity permission) {
		if(permissions == null) {
			return false;
		}
		return permissions.contains(permission);
	}
	
}

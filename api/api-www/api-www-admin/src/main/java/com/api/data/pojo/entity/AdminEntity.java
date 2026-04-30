package com.api.data.pojo.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * entity - 系统用户
 */
@Entity
@Table(name = "ts_admin", indexes = {
	@Index(name = "index_admin_name", columnList = "name", unique = true)
})
public class AdminEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_ROLES = "roles";
	
	/**
	 * 账号
	 */
	@Size(max = 20, message = "系统用户账号长度不能超过20")
	@NotBlank(message = "系统用户账号不能为空")
	private String name;
	/**
	 * 密码
	 */
	@Size(max = 50, message = "系统用户密码长度不能超过50")
	@NotBlank(message = "系统用户密码不能为空")
	private String password;
	/**
	 * 描述
	 */
	@Size(max = 100, message = "系统用户描述长度不能超过100")
	private String memo;
	/**
	 * 角色列表
	 */
	@JsonIgnore
	private List<RoleEntity> roles;

	@Column(length = 20, nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(length = 50, nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
		name = "ts_admin_role",
		joinColumns = @JoinColumn(
			name = "admin_id",
			referencedColumnName = "id",
			foreignKey = @ForeignKey(name = "key_admin_role_admin_id")
		),
		inverseJoinColumns = @JoinColumn(
			name = "role_id",
			referencedColumnName = "id",
			foreignKey = @ForeignKey(name = "key_admin_role_role_id")
		)
	)
	public List<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleEntity> roles) {
		this.roles = roles;
	}
	
	public boolean hasRole(RoleEntity role) {
		if(this.roles == null) {
			return false;
		}
		return roles.contains(role);
	}
	
	public boolean hasPermission(PermissionEntity permission) {
		if(roles == null) {
			return false;
		}
		return roles
			.stream()
			.anyMatch(role -> role.hasPermission(permission));
	}
	
}

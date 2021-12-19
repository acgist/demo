package com.acgist.data.pojo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * <p>entity - 权限</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Entity
@Table(name = "ts_permission", indexes = {
	@Index(name = "index_permission_parent", columnList = "parent")
})
public final class PermissionEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>是否保存记录</p>
	 */
	@NotNull(message = "是否保存记录不能为空")
	private Boolean save;
	/**
	 * <p>权限类型</p>
	 */
	@NotNull(message = "权限类型不能为空")
	private UserEntity.Type type;
	/**
	 * <p>权限名称</p>
	 * <p>命名规范：模块 + 内容 + 操作</p>
	 */
	@Size(max = 20, message = "权限名称长度不能超过20")
	@NotBlank(message = "权限名称不能为空")
	private String name;
	/**
	 * <p>角色标识</p>
	 */
	@Size(max = 40, message = "权限标识长度不能超过40")
	@NotBlank(message = "权限标识不能为空")
	private String token;
	/**
	 * <p>请求地址</p>
	 * <p>前台接口统一使用POST</p>
	 * <p>后台接口支持使用RESTful</p>
	 * <p>用户命名规范：模块 + 内容 + 操作</p>
	 * <p>后台命名规范：{@code /admin} + 模块 + 内容 + 操作</p>
	 * <p>操作：insert/delete/update/select等等</p>
	 */
	@Size(max = 256, message = "请求地址长度不能超过256")
	@NotBlank(message = "请求地址不能为空")
	private String path;
	/**
	 * <p>权限排序</p>
	 */
	@NotNull(message = "权限排序不能为空")
	private Integer sort;
	/**
	 * <p>是否需要异步回调</p>
	 * <p>后台接口不用配置</p>
	 */
	@NotNull(message = "是否需要异步回调不能为空")
	private Boolean notify;
	/**
	 * <p>网关请求类型</p>
	 * <p>后台接口不用配置</p>
	 */
	@Size(max = 256, message = "网关请求类型不能超过256")
	private String requestClazz;
	/**
	 * <p>网关响应类型</p>
	 * <p>后台接口不用配置</p>
	 */
	@Size(max = 256, message = "网关响应类型不能超过256")
	private String responseClazz;
	/**
	 * <p>父级菜单</p>
	 */
	@Size(max = 32, message = "父级菜单长度不能超过32")
	private String parent;

	@Column(nullable = false)
	public Boolean getSave() {
		return save;
	}

	public void setSave(Boolean save) {
		this.save = save;
	}

	@Enumerated(EnumType.STRING)
	@Column(length = 64, nullable = false)
	public UserEntity.Type getType() {
		return type;
	}

	public void setType(UserEntity.Type type) {
		this.type = type;
	}

	@Column(length = 20, nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(length = 40, nullable = false)
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Column(length = 256, nullable = false)
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Column(nullable = false)
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Column(nullable = false)
	public Boolean getNotify() {
		return notify;
	}

	public void setNotify(Boolean notify) {
		this.notify = notify;
	}
	
	@Column(length = 256)
	public String getRequestClazz() {
		return requestClazz;
	}

	public void setRequestClazz(String requestClazz) {
		this.requestClazz = requestClazz;
	}

	@Column(length = 256)
	public String getResponseClazz() {
		return responseClazz;
	}

	public void setResponseClazz(String responseClazz) {
		this.responseClazz = responseClazz;
	}

	@Column(length = 32)
	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}
	
}

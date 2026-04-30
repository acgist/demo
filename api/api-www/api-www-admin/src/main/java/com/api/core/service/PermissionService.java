package com.api.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import com.api.core.pojo.vo.PermissionTree;
import com.api.data.pojo.entity.PermissionEntity;
import com.api.data.pojo.entity.RoleEntity;
import com.api.data.repository.PermissionRepository;
import com.api.data.repository.RoleRepository;
import com.api.data.service.EntityService;

/**
 * service - 系统权限
 * TODO PERMISSION_ROLES、PERMISSION_NAMES集群时清除缓存优化，存入redis
 */
@Service
public class PermissionService extends EntityService<PermissionEntity> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PermissionService.class);
	
	// 权限路径和角色映射
	private static final Map<String, List<String>> PERMISSION_ROLES = new HashMap<>();
	// 权限路径和名称映射
	private static final Map<String, String> PERMISSION_NAMES = new HashMap<>();
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	public PermissionService(PermissionRepository repository) {
		super(repository);
	}

	@PostConstruct
	public void init() {
		initPermissionRoles();
		initPermissionNames();
	}

	/**
	 * 权限树
	 */
	public PermissionTree tree() {
		return tree(null);
	}
	
	/**
	 * 权限树
	 * @param list 当前已有权限，标记权限树中已有权限
	 */
	public PermissionTree tree(List<PermissionEntity> list) {
		PermissionTree root = new PermissionTree();
		root.setName("根节点");
		List<PermissionTree> packages = repository
			.findAll()
			.stream()
			.sorted((a, b) -> {
				return (a.getSort() != null || b.getSort() != null) ? 0 : a.getSort().compareTo(b.getSort());
			})
			.map(value -> {
				PermissionTree permissionTree = new PermissionTree();
				permissionTree.setName(value.getName());
				permissionTree.setEntity(value);
				if(list != null) {
					permissionTree.setChecked(list.stream().anyMatch(entity -> entity.getId().equals(value.getId())));
				} else {
					permissionTree.setChecked(Boolean.FALSE);
				}
				return permissionTree;
			})
			.collect(Collectors.toList());
		packagePermission(root, packages);
		return root;
	}

	/**
	 * 递归打包权限树
	 */
	private void packagePermission(PermissionTree parent, List<PermissionTree> list) {
		list.forEach(permission -> {
			final PermissionEntity parentEntity = parent.getEntity();
			final PermissionEntity permissionEntity = permission.getEntity();
			if(parentEntity == null && StringUtils.isEmpty(permissionEntity.getParent())) {
				parent.addChildren(permission);
				packagePermission(permission, list);
			} else if(parentEntity != null && parentEntity.getId().equals(permissionEntity.getParent())) {
				parent.addChildren(permission);
				packagePermission(permission, list);
			}
		});
	}
	
	/**
	 * 删除前验证权限是否含有子权限，如果有子权限删除失败
	 */
	@Override
	public boolean delete(String id) {
		PermissionRepository permissionRepository = (PermissionRepository) repository;
		List<PermissionEntity> list = permissionRepository.findByParent(id);
		if(list.isEmpty()) {
			return super.delete(id);
		}
		return false;
	}
	
	/**
	 * 初始化权限和角色映射信息
	 */
	public void initPermissionRoles() {
		LOGGER.info("初始化权限和角色映射信息");
		PERMISSION_ROLES.clear();
		final Map<String, List<String>> permissions = new HashMap<>();
		final List<RoleEntity> list = roleRepository.findAll();
		list.forEach(role -> {
			role.getPermissions().stream()
			.filter(permission -> StringUtils.isNotEmpty(permission.getPattern()))
			.forEach(permission -> {
				String pattern = permission.getPattern();
				List<String> roles = permissions.get(pattern);
				if(roles == null) {
					roles = new ArrayList<>();
				}
				roles.add(role.getName());
				permissions.put(pattern, roles);
			});
		});
		PERMISSION_ROLES.putAll(permissions);
		LOGGER.info("权限和角色映射信息：{}", PERMISSION_ROLES);
	}
	
	/**
	 * 根据权限地址获取对应权限的角色名称
	 * @param servletPath 权限地址
	 * @return 角色名称列表
	 */
	public List<String> permissionRoles(String servletPath) {
		Optional<Entry<String, List<String>>> equalMatch = PERMISSION_ROLES.entrySet().stream()
			.filter(entity -> entity.getKey().equalsIgnoreCase(servletPath))
			.findFirst();
		if(equalMatch.isPresent()) {
			equalMatch.get().getValue();
		}
		Optional<Entry<String, List<String>>> antMatch = PERMISSION_ROLES.entrySet().stream()
			.filter(entity -> {
				AntPathMatcher matcher = new AntPathMatcher();
				return matcher.match(entity.getKey(), servletPath);
			})
			.findFirst();
		return antMatch.isPresent() ? antMatch.get().getValue() : null;
	}
	
	/**
	 * 初始化权限路径和权限名称
	 * TODO 修改、删除、新增时重新初始化
	 */
	public void initPermissionNames() {
		LOGGER.info("初始化权限和权限名称映射信息");
		List<PermissionEntity> list = repository.findAll();
		PERMISSION_NAMES.clear();
		list.forEach(entity -> {
			PERMISSION_NAMES.put(entity.getPattern(), entity.getName());
		});
	}
	
	/**
	 * 根据权限地址获取对应权限的名称
	 * @param servletPath 权限地址
	 * @return 权限名称
	 */
	public String permissionName(String servletPath) {
		Optional<Entry<String, String>> equalMatch = PERMISSION_NAMES.entrySet().stream()
			.filter(entity -> entity.getKey().equalsIgnoreCase(servletPath))
			.findFirst();
		if(equalMatch.isPresent()) {
			equalMatch.get().getValue();
		}
		Optional<Entry<String, String>> antMatch = PERMISSION_NAMES.entrySet().stream()
			.filter(entity -> {
				AntPathMatcher matcher = new AntPathMatcher();
				return matcher.match(entity.getKey(), servletPath);
			})
			.findFirst();
		return antMatch.isPresent() ? antMatch.get().getValue() : null;
	}
	
}

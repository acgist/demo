package com.api.core.service;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.data.pojo.entity.PermissionEntity;
import com.api.data.pojo.entity.RoleEntity;
import com.api.data.repository.PermissionRepository;
import com.api.data.repository.RoleRepository;
import com.api.data.service.EntityService;

/**
 * service - 角色
 */
@Service
public class RoleService extends EntityService<RoleEntity> {
	
	@Autowired
	private PermissionRepository permissionRepository;
	
	@Autowired
	public RoleService(RoleRepository repository) {
		super(repository);
	}

	@Override
	public RoleEntity update(RoleEntity t) {
		return repository.update(t, RoleEntity.PROPERTY_PERMISSIONS);
	}
	
	/**
	 * 系统角色授权
	 * @param rid 角色ID
	 * @param pids 权限ID数组
	 */
	public void permission(String rid, String[] pids) {
		if(pids == null) {
			return;
		}
		RoleEntity role = find(rid);
		List<PermissionEntity> list = role.getPermissions();
		list.clear();
		Stream.of(pids).forEach(pid -> {
			list.add(permissionRepository.findOne(pid));
		});
		repository.save(role);
	}
	
}

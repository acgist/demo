package com.api.core.service;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.data.pojo.entity.AdminEntity;
import com.api.data.pojo.entity.RoleEntity;
import com.api.data.repository.AdminRepository;
import com.api.data.repository.RoleRepository;
import com.api.data.service.EntityService;

/**
 * service - 系统用户
 */
@Service
public class AdminService extends EntityService<AdminEntity> {

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	public AdminService(AdminRepository repository) {
		super(repository);
	}

	@Override
	public AdminEntity update(AdminEntity t) {
		return repository.update(t, AdminEntity.PROPERTY_ROLES);
	}
	
	/**
	 * 系统用户设置角色
	 * @param aid 系统用户ID
	 * @param rids 系统角色ID数组
	 */
	public void role(String aid, String[] rids) {
		if(rids == null) {
			return;
		}
		AdminEntity admin = find(aid);
		List<RoleEntity> list = admin.getRoles();
		list.clear();
		Stream.of(rids).forEach(rid -> {
			list.add(roleRepository.findOne(rid));
		});
		repository.save(admin);
	}
	
}

package com.api.data.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.api.data.pojo.entity.AdminEntity;

/**
 * repository - 系统用户
 */
@Repository
public interface AdminRepository extends BaseExtendRepository<AdminEntity> {

	/**
	 * 根据系统用户用户名称查询系统用户
	 * @param name 系统用户名称
	 * @return 系统用户
	 */
	@Query(value = "SELECT * FROM ts_admin admin WHERE admin.name = :name LIMIT 1", nativeQuery = true)
	AdminEntity findByName(String name);
	
}

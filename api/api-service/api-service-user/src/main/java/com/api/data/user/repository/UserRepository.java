package com.api.data.user.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.api.data.repository.BaseExtendRepository;
import com.api.data.user.pojo.entity.UserEntity;

/**
 * repository - 用户
 */
@Repository
public interface UserRepository extends BaseExtendRepository<UserEntity> {

	/**
	 * 根据用户名查询用户实体
	 * @param name 用户名
	 * @return 用户实体
	 */
	@Query(value = "SELECT * FROM tb_user user WHERE user.name = :name LIMIT 1", nativeQuery = true)
	UserEntity findByName(String name);
	
}

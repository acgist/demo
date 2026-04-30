package com.api.data.repository;

import org.springframework.stereotype.Repository;

import com.api.data.user.pojo.entity.UserEntity;

/**
 * repository - 用户
 */
@Repository
public interface UserRepository extends BaseExtendRepository<UserEntity> {

}

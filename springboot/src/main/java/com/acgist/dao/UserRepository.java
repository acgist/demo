package com.acgist.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.acgist.entity.UserEntity;

@Repository
public interface UserRepository extends
	JpaRepository<UserEntity, String>,
	CrudRepository<UserEntity, String>,
	JpaSpecificationExecutor<UserEntity>,
	PagingAndSortingRepository<UserEntity, String> {

	@Query("select user from UserEntity user where user.name = :name")
	public UserEntity findOne(@Param("name") String name);
	
	public UserEntity findByName(String name);
	
}

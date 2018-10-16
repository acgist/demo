package com.acgist.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.acgist.entity.FeelEntity;

@Repository
public interface FeelRepository extends JpaRepository<FeelEntity, String>, JpaSpecificationExecutor<FeelEntity> {
	
	@Modifying
	@Transactional
	@Query("update FeelEntity feel set feel.face = :face where feel.id = :id")
	public void updateFace(@Param("id") String id, @Param("face") String face);
	
	@Query("select feel from FeelEntity feel where feel.uid = :uid and feel.date = :date")
	public FeelEntity findDate(@Param("uid") String uid, @Param("date") String date);
	
}

package com.acgist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acgist.dao.FeelRepository;
import com.acgist.entity.FeelEntity;
import com.acgist.web.tools.DateTool;

@Service
public class FeelService {

	@Autowired
	private FeelRepository feelRepository;
	
	/**
	 * 合并用户数据，如果今天数据已经存在则更新，反之创建新数据
	 */
	public FeelEntity merge(String uid, String face) {
		String date = DateTool.formatNow();
		FeelEntity entity = findDate(uid, date);
		if(entity == null) {
			return save(uid, face);
		} else {
			updateFace(entity.getId(), face);
			entity.setFace(face);
			return entity;
		}
	}
	
	/**
	 * 保存数据
	 */
	private FeelEntity save(String uid, String face) {
		FeelEntity entity = new FeelEntity();
		entity.setUid(uid);
		entity.setFace(face);
		entity.setDate(DateTool.formatNow());
		feelRepository.save(entity);
		return entity;
	}
	
	/**
	 * 更新人脸
	 */
	public void updateFace(String id, String face) {
		feelRepository.updateFace(id, face);
	}
	
	/**
	 * 更新用户感受
	 */
	public void updateFeel(String uid, String feel) {
	}

	/**
	 * 判断用户数据是否存在
	 */
	public FeelEntity findDate(String uid, String date) {
		return feelRepository.findDate(uid, date);
	}
	
	/**
	 * 删除用户数据
	 */
	public void delete(String uid) {
	}
	
}

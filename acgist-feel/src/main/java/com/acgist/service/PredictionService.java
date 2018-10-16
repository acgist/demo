package com.acgist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PredictionService {

	@Autowired
	private FeelService feelService;

	/**
	 * 预测
	 */
	public void prediction(String uid, String face) {
		feelService.merge(uid, face);
	}
	
}

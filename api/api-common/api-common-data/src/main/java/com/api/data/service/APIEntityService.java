package com.api.data.service;

import com.api.core.gateway.APICode;
import com.api.core.pojo.message.ResultMessage;
import com.api.data.pojo.entity.BaseEntity;
import com.api.utils.ValidatorUtils;

/**
 * service - 实体操作基类
 */
public interface APIEntityService {

	/**
	 * 实体验证
	 * @param entity 实体
	 * @param result 验证信息
	 * @return 验证结果：true-成功、false-失败
	 */
	default boolean verifyEntity(BaseEntity entity, ResultMessage result) {
		final String message = ValidatorUtils.verify(entity);
		if(message == null) {
			if(result != null) {
				result.buildSuccess();
			}
			return true;
		} else {
			if(result != null) {
				result.buildMessage(APICode.CODE_3000, message);
			}
			return false;
		}
	}
	
}

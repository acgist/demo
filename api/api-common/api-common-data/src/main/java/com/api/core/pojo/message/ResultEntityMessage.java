package com.api.core.pojo.message;

import com.api.data.pojo.entity.BaseEntity;

/**
 * message - 服务间通信，返回结果和实体
 */
public class ResultEntityMessage<T extends BaseEntity> extends ResultMessage {

	private static final long serialVersionUID = 1L;

	protected T entity;

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

}

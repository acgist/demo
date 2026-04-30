package com.api.core.pojo.message;

import com.api.data.pojo.entity.BaseEntity;

/**
 * message - 服务间通信，返回实体
 */
public class EntityMessage<T extends BaseEntity> extends BaseMessage {

	private static final long serialVersionUID = 1L;

	protected T entity;

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}
	
}

package com.api.data.pojo.entity;

import com.api.core.gateway.request.APIRequest;

/**
 * 请求数据设置到实体对象
 */
public interface ValueOfRequest<T extends APIRequest> {

	/**
	 * 请求数据设置到实体对象
	 */
	void valueOfRequest(T request);
	
}

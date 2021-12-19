package com.acgist.core.gateway.service;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.gateway.gateway.request.UserUpdateRequest;
import com.acgist.core.gateway.gateway.response.UserResponse;
import com.acgist.core.gateway.gateway.response.UserUpdateResponse;
import com.acgist.core.gateway.response.GatewayResponse;
import com.acgist.core.service.GatewayService;
import com.acgist.core.service.IUserService;
import com.acgist.data.pojo.entity.UserEntity;

/**
 * <p>service - 用户</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Service
public class UserService extends GatewayService {
	
	@Reference(version = "${acgist.service.version}")
	private IUserService userService;
	
	/**
	 * <p>用户信息查询</p>
	 * 
	 * @return 查询结果
	 */
	public GatewayResponse select() {
		final var session = this.gatewaySession();
		final var request = session.getRequest();
		final var response = (UserResponse) session.getResponse();
		final var message = this.userService.findByName(request.getUsername());
		if(message.fail()) {
			return session.buildResponse(message);
		} else {
			final var entity = message.getEntity();
			response.setMail(entity.getMail());
			response.setNick(entity.getNick());
			response.setMobile(entity.getMobile());
			return session.buildResponse(AcgistCode.CODE_0000);
		}
	}
	
	/**
	 * <p>用户信息修改</p>
	 * 
	 * @return 修改结果
	 */
	public GatewayResponse update() {
		final var session = this.gatewaySession();
		final var request = (UserUpdateRequest) session.getRequest();
		final var response = (UserUpdateResponse) session.getResponse();
		final UserEntity entity = new UserEntity();
		entity.setNick(request.getNick());
		entity.setName(request.getUsername());
		final var message = this.userService.update(entity);
		if(message.fail()) {
			return session.buildResponse(message);
		} else {
			response.setNick(request.getNick());
			return session.buildResponse(AcgistCode.CODE_0000);
		}
	}
	
}

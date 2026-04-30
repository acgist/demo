package com.api.core.user.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.core.gateway.APICode;
import com.api.core.user.pojo.message.AuthoMessage;
import com.api.core.user.pojo.message.LoginMessage;
import com.api.data.user.pojo.entity.UserEntity;
import com.api.data.user.repository.UserRepository;

/**
 * service - 用户
 */
@Service
public class UserServiceImpl {

	@Autowired
	private UserRepository userRepository;

	/**
	 * 根据用户名称获取授权信息（公钥、私钥）
	 * @param name 用户名称
	 * @return 授权信息
	 */
	public AuthoMessage autho(String name) {
		if(name == null) {
			return null;
		}
		UserEntity user = userRepository.findByName(name);
		if(user == null) {
			return null;
		}
		AuthoMessage message = new AuthoMessage();
		message.setName(name);
		message.setPubilcKey(user.getPublicKey());
		message.setPrivateKey(user.getPrivateKey());
		return message;
	}

	/**
	 * 用户登录
	 * @param name 用户名称
	 * @param password 用户密码
	 * @return 登陆结果
	 */
	public LoginMessage login(String name, String password) {
		LoginMessage message = new LoginMessage();
		if(name == null || password == null) {
			message.buildMessage(APICode.CODE_2000);
			return message;
		}
		UserEntity user = userRepository.findByName(name);
		if(user == null) {
			message.buildMessage(APICode.CODE_9999, "不存在的用户");
			return message;
		}
		if(!StringUtils.equals(user.getPassword(), password)) {
			message.buildMessage(APICode.CODE_2000);
			return message;
		}
		message.buildSuccess();
		message.setId(user.getId());
		message.setName(user.getName());
		return message;
	}

}

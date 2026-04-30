package com.api.core.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.api.core.user.pojo.message.AuthoMessage;
import com.api.core.user.pojo.message.LoginMessage;
import com.api.core.user.service.impl.UserServiceImpl;

/**
 * 服务 - 用户
 */
@RestController
public class UserService implements IUserService {

	@Autowired
	private UserServiceImpl userServiceImpl;
	
	@Override
	public AuthoMessage autho(String name) {
		return userServiceImpl.autho(name);
	}

	@Override
	public LoginMessage login(String name, String password) {
		return userServiceImpl.login(name, password);
	}

}

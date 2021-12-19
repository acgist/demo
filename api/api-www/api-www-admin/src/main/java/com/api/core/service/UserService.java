package com.api.core.service;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.data.repository.UserRepository;
import com.api.data.service.EntityService;
import com.api.data.user.pojo.entity.UserEntity;
import com.api.utils.CAUtils;

/**
 * service - 用户
 */
@Service
public class UserService extends EntityService<UserEntity> {

	@Autowired
	public UserService(UserRepository repository) {
		super(repository);
	}

	/**
	 * 生成证书<br>
	 * 应该生成两对，出于简单只生成一对
	 */
	public Map<String, String> cert() {
		final Map<String, String> data = new HashMap<>(2);
		final KeyPair keyPair = CAUtils.keyPair();
		final PublicKey publicKey = keyPair.getPublic();
		final PrivateKey privateKey = keyPair.getPrivate();
		data.put("publicKey", CAUtils.keyToString(publicKey));
		data.put("privateKey", CAUtils.keyToString(privateKey));
		return data;
	}
	
}

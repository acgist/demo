package com.api.core.service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.core.gateway.API;
import com.api.core.user.pojo.message.AuthoMessage;
import com.api.ribbon.service.UserService;
import com.api.utils.CAUtils;

/**
 * service - 签名工具，公钥私钥生成：http://web.chacuo.net/netrsakeypair
 */
@Service
public class SignService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SignService.class);

	@Autowired
	private UserService userService;
	
	/**
	 * 验签
	 */
	public boolean verify(API api) {
		if (api == null) {
			return false;
		}
		return verify(api.data());
	}

	/**
	 * 验签
	 */
	public boolean verify(Map<String, String> data) {
		if (data == null) {
			return false;
		}
		final String digest = dataToDigest(data);
		final String sign = data.get(API.PROPERTY_SIGN);
		try {
			final PublicKey publicKey = publicKey(data);
			return CAUtils.verify(digest, sign, publicKey);
		} catch (Exception e) {
			LOGGER.error("验签异常", e);
		}
		return false;
	}

	/**
	 * 签名
	 */
	public void sign(API api) {
		if (api == null) {
			return;
		}
		final Map<String, String> data = api.data();
		final String digest = dataToDigest(data);
		final PrivateKey privateKey = privateKey(data);
		final String sign = CAUtils.sign(digest, privateKey);
		api.setSign(sign);
	}

	private PublicKey publicKey(Map<String, String> data) {
		AuthoMessage autho = authoMessage(data);
		if(autho == null) {
			return null;
		}
		return CAUtils.stringToPublicKey(autho.getPubilcKey());
	}
	
	private PrivateKey privateKey(Map<String, String> data) {
		AuthoMessage autho = authoMessage(data);
		if(autho == null) {
			return null;
		}
		return CAUtils.stringToPrivateKey(autho.getPrivateKey());
	}
	
	private AuthoMessage authoMessage(Map<String, String> data) {
		final String username = data.get(API.PROPERTY_USERNAME);
		return userService.autho(username);
	}
	
	/**
	 * 获取签名字符串：按照key排序拼接为字符串
	 */
	public static final String dataToDigest(final Map<String, String> data) {
		final StringBuffer buffer = new StringBuffer();
		final TreeMap<String, String> treeMap = new TreeMap<>();
		data.entrySet().stream().filter(entry -> !API.PROPERTY_SIGN.equals(entry.getKey())).forEach(entry -> {
			treeMap.put(entry.getKey(), entry.getValue());
		});
		treeMap.entrySet().forEach(entry -> {
			buffer.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
		});
		if (buffer.length() == 0) {
			return buffer.toString();
		}
		return buffer.substring(0, buffer.length() - 1);
	}

}

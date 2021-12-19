package com.api.ribbon.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.api.core.config.APIConstApplication;
import com.api.core.config.APIConstCache;
import com.api.core.user.config.APIConstUserURL;
import com.api.core.user.pojo.message.AuthoMessage;
import com.api.utils.HttpEntityUtils;
import com.api.utils.RibbonUtils;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/**
 * 服务调用 - 用户
 */
@Service
public class UserService {

	@Autowired
    public RestTemplate restTemplate;
	
	/**
	 * 获取用户授权信息
	 */
	@Cacheable(cacheNames = APIConstCache.CACHE_KEY_USER_AUTHO, unless = "#result == null")
	@HystrixCommand(fallbackMethod = "authoFallback")
	public AuthoMessage autho(String name) {
		if(name == null) {
			return null;
		}
		final String uri = RibbonUtils.buildService(APIConstApplication.API_SERVICE_USER, APIConstUserURL.URL_SERVICE_USER_AUTHO);
		return restTemplate.postForEntity(uri, HttpEntityUtils.formEntity(Map.of("name", name)), AuthoMessage.class).getBody();
	}
	
	/**
	 * 熔断器
	 */
	public AuthoMessage authoFallback(String name) {
		return null;
	}

}

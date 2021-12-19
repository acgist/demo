package com.api.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.api.data.pojo.entity.AdminEntity;
import com.api.data.repository.AdminRepository;

/**
 * 系统用户
 */
@Service
public class AdminDetailsService implements UserDetailsService {

	@Autowired
	private AdminRepository adminRepository;
	
	/**
	 * 根据系统用户名加载系统用户
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AdminEntity admin = adminRepository.findByName(username);
		if (admin == null) {
			throw new UsernameNotFoundException("用户不存在：" + username);
		}
		return new AdminDetails(admin);
	}

}

package com.api.core.module.freemarker;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.api.core.security.AdminDetails;
import com.api.core.service.PermissionService;

import freemarker.core.Environment;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 标签 - 权限验证
 */
@Component
@RefreshScope
public class AuthoTag implements TemplateDirectiveModel {
	
	private static final String KEY_NAME = "name"; // 菜单名称
	private static final String KEY_PATTERN = "pattern"; // 菜单路径
	
	@Value("${system.security.enable:true}")
	private boolean enable;
	
	@Autowired
	private PermissionService permissionService;
	
	@Override
	@SuppressWarnings("rawtypes")
	public void execute(Environment env, Map params, TemplateModel[] model, TemplateDirectiveBody body) throws TemplateException, IOException {
		if(enable) {
			SimpleScalar nameScalar = (SimpleScalar) params.get(KEY_NAME); // 权限名称
			SimpleScalar patternScalar = (SimpleScalar) params.get(KEY_PATTERN); // 权限地址
			String name = nameScalar == null ? null : nameScalar.getAsString();
			String pattern = patternScalar == null ? null : patternScalar.getAsString();
			if(!(name(name) || pattern(pattern))) {
				return;
			}
		}
		body.render(env.getOut());
	}

	/**
	 * 根据权限名称验证权限
	 * @param name 权限名称
	 * @return 验证结果：true-成功、false-失败
	 */
	private boolean name(String name) {
		if(name == null) {
			return false;
		}
		final AdminDetails adminDetails = AdminDetails.current();
		return adminDetails.hasPermissions(name);
	}
	
	/**
	 * 根据权限地址验证权限
	 * @param pattern 权限地址
	 * @return 验证结果：true-成功、false-失败
	 */
	private boolean pattern(String pattern) {
		if(pattern == null) {
			return false;
		}
		final List<String> roles = permissionService.permissionRoles(pattern);
		if(roles == null) {
			return false;
		}
		final AdminDetails adminDetails = AdminDetails.current();
		return adminDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(role -> roles.contains(role));
	}
	
}

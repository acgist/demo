package com.api.core.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.api.core.security.AdminDetails;
import com.api.core.service.PermissionService;
import com.api.core.stream.LogMessageSender;
import com.api.data.asyn.pojo.entity.LogEntity;
import com.api.utils.RequestUtils;

/**
 * 拦截器 - 订单<br>
 * 没有登陆时访问订单目录以及订单子目录进入到登陆页面
 */
@Component
public class LogInterceptor implements HandlerInterceptor {

	@Autowired
	private LogMessageSender logMessageSender;
	@Autowired
	private PermissionService permissionService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		logging(request);
		return true;
	}
	
	/**
	 * 日志记录
	 */
	private void logging(HttpServletRequest request) {
		LogEntity log = new LogEntity();
		log.setLevel(LogEntity.LogLevel.info);
		log.setType(LogEntity.LogType.system);
		AdminDetails admin = AdminDetails.current();
		if (admin != null) {
			log.setOperator(admin.getName());
		}
		if (request != null) {
			String servletPath = request.getServletPath();
			String name = permissionService.permissionName(servletPath);
			if (name == null) {
				name = servletPath;
			}
			log.setName(name);
			log.setMemo(RequestUtils.requestMessage(request));
		}
		logMessageSender.log(log);
	}
	
}

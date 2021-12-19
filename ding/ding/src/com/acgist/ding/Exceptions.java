package com.acgist.ding;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.statushandlers.StatusManager;

/**
 * 异常工具
 * 
 * @author acgist
 */
public class Exceptions {

	private Exceptions() {
	}
	
	/**
	 * 异常日志
	 * 
	 * @param clazz 类
	 * @param throwable 异常
	 */
	public static final void error(Class<?> clazz, Throwable throwable) {
		StatusManager.getManager().handle(new Status(IStatus.ERROR, clazz, throwable.getMessage(), throwable));
	}
	
}

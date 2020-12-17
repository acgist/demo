package com.acgist.utils;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import com.acgist.gateway.Gateway;

/**
 * 数据校验工具
 */
public class ValidatorUtils {

	private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
	
	/**
	 * 校验数据：
	 * 	成功返回：null
	 * 	失败返回：错误信息
	 */
	public static final String verify(Gateway request) {
		if(request == null) {
			return null;
		}
		final StringBuffer message = new StringBuffer();
		final Set<ConstraintViolation<Gateway>> set = VALIDATOR.validate(request, Default.class);
		if (set != null && !set.isEmpty()) {
			for (ConstraintViolation<Gateway> violation : set) {
				message
					.append(violation.getMessage())
					.append("[")
					.append(violation.getPropertyPath().toString())
					.append("]")
					.append("&");
			}
		}
		if(message.length() == 0) {
			return null;
		}
		return message.substring(0, message.length() - 1);
	}
	
}

package com.acgist.api;

import java.io.Serializable;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import com.acgist.modules.sign.SignService;
import com.acgist.modules.utils.APIUtils;
import com.acgist.modules.utils.JSONUtils;
import com.acgist.modules.utils.ValidatorUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 抽象请求：@JsonIgnoreProperties忽略传入的不需要的信息
 */
@JsonInclude(Include.NON_NULL)
//@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonIgnoreProperties(ignoreUnknown = true, value = {"sign"})
public abstract class API implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String PROPERTY_SIGN = "sign"; // 签名字符串

//	@JsonIgnore // 忽略序列化属性
	@NotBlank(message = "签名内容不能为空")
	protected String sign; // 签名
	protected String reserved; // 原样返回数据

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}
	
	/**
	 * 数据格式校验
	 */
	public String verify() {
		return ValidatorUtils.verify(this);
	}

	/**
	 * 签名同时返回签名后的内容
	 */
	public Map<String, String> sign() {
		SignService.sign(this);
		return data();
	}

	/**
	 * 获取map数据
	 */
	public Map<String, String> data() {
		return APIUtils.beanToMap(this);
	}

	/**
	 * 返回JSON字符串
	 */
	@Override
	public String toString() {
		return JSONUtils.javaToJson(this);
	}
	
}

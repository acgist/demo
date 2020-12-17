package com.acgist.gateway;

import java.io.Serializable;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import com.acgist.gateway.service.SignatureService;
import com.acgist.utils.APIUtils;
import com.acgist.utils.JSONUtils;
import com.acgist.utils.ValidatorUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * <p>抽象请求：@JsonIgnoreProperties忽略传入的不需要的信息</p>
 */
@JsonInclude(Include.NON_NULL)
//@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonIgnoreProperties(ignoreUnknown = true, value = {"signature"})
public abstract class Gateway implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>签名</p>
	 */
	public static final String PROPERTY_SIGNATURE = "signature";

	/**
	 * <p>签名数据</p>
	 */
//	@JsonIgnore // 忽略序列化
	@NotBlank(message = "签名内容不能为空")
	protected String signature;
	/**
	 * <p>请求透传信息</p>
	 */
	protected String reserved;

	public String getSignature() {
		return signature;
	}

	public String getReserved() {
		return reserved;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

	/**
	 * <p>数据格式校验</p>
	 * 
	 * @return 错误信息
	 */
	public String validator() {
		return ValidatorUtils.verify(this);
	}

	/**
	 * <p>签名</p>
	 * 
	 * @return 签名结果
	 */
	public Map<String, String> signature() {
		SignatureService.signature(this);
		return this.toMap();
	}

	/**
	 * <p>转为Map数据</p>
	 * 
	 * @return Map数据
	 */
	public Map<String, String> toMap() {
		return APIUtils.beanToMap(this);
	}

	@Override
	public String toString() {
		return JSONUtils.serialize(this);
	}
	
}

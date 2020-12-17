package com.acgist.gateway;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.acgist.utils.JSONUtils;
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
	 * <p>网关类型</p>
	 * 
	 * @see com.acgist.gateway.config.Gateway
	 */
	@NotBlank(message = "网关类型不能为空")
	protected String gateway;
	/**
	 * <p>签名数据</p>
	 */
//	@JsonIgnore // 忽略序列化
	@NotBlank(message = "签名内容不能为空")
	protected String signature;
	/**
	 * <p>请求透传信息</p>
	 */
	@Size(max = 512, message = "透传信息长度不能超过512")
	protected String reserved;

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

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

	@Override
	public String toString() {
		return JSONUtils.serialize(this);
	}
	
}

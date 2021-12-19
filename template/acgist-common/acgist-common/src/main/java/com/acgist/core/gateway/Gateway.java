package com.acgist.core.gateway;

import java.io.Serializable;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.acgist.core.config.AcgistConst;
import com.acgist.core.pojo.Pojo;
import com.acgist.utils.GatewayUtils;

/**
 * <p>网关</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public abstract class Gateway extends Pojo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>签名：{@value}</p>
	 */
	public static final String PROPERTY_SIGNATURE = "signature";

	/**
	 * <p>用户名称</p>
	 */
	@Size(min = 4, max = 20, message = "用户名称长度不能小于4或者超过20")
	@NotBlank(message = "用户名称不能为空")
	protected String username;
	/**
	 * <p>请求时间</p>
	 */
	@Pattern(regexp = AcgistConst.TIMESTAMP_REGEX, message = "请求时间格式错误")
	@NotBlank(message = "请求时间不能为空")
	protected String requestTime;
	/**
	 * <p>签名</p>
	 * <p>MD5</p>
	 */
	@Size(max = 1024, message = "签名长度不能超过1024")
	@NotBlank(message = "签名不能为空")
	protected String signature;
	/**
	 * <p>请求保留数据</p>
	 * <p>原样返回</p>
	 */
	@Size(max = 256, message = "请求保留数据不能超过256")
	protected String reserved;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}
	
	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	public String getReserved() {
		return reserved;
	}
	
	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

	/**
	 * <p>获取参数Map</p>
	 * 
	 * @return 参数Map
	 */
	public Map<String, String> data() {
		return GatewayUtils.unpack(this);
	}
	
}

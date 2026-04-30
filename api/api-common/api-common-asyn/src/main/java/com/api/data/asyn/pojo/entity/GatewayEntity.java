package com.api.data.asyn.pojo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.api.data.pojo.entity.BaseEntity;

/**
 * entity - 网关信息
 */
@Entity
@Table(name = "tb_gateway")
public class GatewayEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	public static final String PROPERTY_TYPE = "type"; // 类型
	public static final String PROPERTY_CODE = "code"; // 状态码
	public static final String PROPERTY_QUERY_ID = "queryId"; // 请求ID
	public static final String PROPERTY_SEND = "send"; // 请求报文
	public static final String PROPERTY_RECEIVE = "receive"; // 响应报文
	
	/**
	 * 请求类型，参考：{@link APIType}
	 */
	@Size(max = 20, message = "请求类型长度不能超过20")
	@NotBlank(message = "请求类型不能为空")
	private String type;
	/**
	 * 响应状态
	 */
	@Size(max = 4, message = "响应状态长度不能超过4")
	private String code;
	/**
	 * 请求ID
	 */
	@Size(max = 20, message = "请求ID长度不能超过20")
	private String queryId;
	/**
	 * 响应报文
	 */
	private String send;
	/**
	 * 请求报文
	 */
	private String receive;

	@Column(length = 20, nullable = false)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(length = 4)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(length = 20)
	public String getQueryId() {
		return queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	@Lob
	public String getSend() {
		return send;
	}

	public void setSend(String send) {
		this.send = send;
	}

	@Lob
	public String getReceive() {
		return receive;
	}

	public void setReceive(String receive) {
		this.receive = receive;
	}

}

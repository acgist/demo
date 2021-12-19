package com.api.core.pojo.vo;

/**
 * vo - 端点
 */
public class EndpointVo {

	private String key;
	private String name;
	private String uri;

	public EndpointVo() {
	}

	public EndpointVo(String key, String name, String uri) {
		this.key = key;
		this.name = name;
		this.uri = uri;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

}

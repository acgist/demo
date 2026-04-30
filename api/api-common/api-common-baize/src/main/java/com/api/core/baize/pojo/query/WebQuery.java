package com.api.core.baize.pojo.query;

/**
 * 网站查询语句
 */
public class WebQuery {

	private String urlTemplate; // URL模板
	private Integer min; // 开始页数
	private Integer max; // 结束页数

	public String getUrlTemplate() {
		return urlTemplate;
	}

	public void setUrlTemplate(String urlTemplate) {
		this.urlTemplate = urlTemplate;
	}

	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public String uri() {
		return null;
	}
	
}

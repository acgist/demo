package com.acgist.fund.pojo.entity;

import java.io.Serializable;

/**
 * <p>占比：资产、持仓、行业、债券</p>
 * 
 * TODO：排序名称、排序占比
 * 
 * @author acgist
 */
public class PositionEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>名称</p>
	 */
	private String name;
	/**
	 * <p>数值</p>
	 */
	private Double value;
	/**
	 * <p>百分比</p>
	 */
	private Double percent;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Double getPercent() {
		return percent;
	}

	public void setPercent(Double percent) {
		this.percent = percent;
	}
	
}

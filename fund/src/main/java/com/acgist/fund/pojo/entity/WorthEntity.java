package com.acgist.fund.pojo.entity;

import java.io.Serializable;

/**
 * <p>价值</p>
 * 
 * @author acgist
 */
public class WorthEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>当前净值</p>
	 */
	private Double netWorth;
	/**
	 * <p>累计净值</p>
	 */
	private Double totalNetWorth;
	/**
	 * <p>基金规模：市值</p>
	 */
	private String marketValue;

	public Double getNetWorth() {
		return netWorth;
	}

	public void setNetWorth(Double netWorth) {
		this.netWorth = netWorth;
	}

	public Double getTotalNetWorth() {
		return totalNetWorth;
	}

	public void setTotalNetWorth(Double totalNetWorth) {
		this.totalNetWorth = totalNetWorth;
	}

	public String getMarketValue() {
		return marketValue;
	}

	public void setMarketValue(String marketValue) {
		this.marketValue = marketValue;
	}

}

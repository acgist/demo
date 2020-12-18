package com.acgist.fund.pojo.entity;

import java.io.Serializable;

/**
 * <p>收益</p>
 * 
 * @author acgist
 */
public class EarningsEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>最近一周</p>
	 */
	private Double lastWeek;
	/**
	 * <p>最近一个月</p>
	 */
	private Double lastMonth;
	/**
	 * <p>最近三个月</p>
	 */
	private Double lastThreeMonth;
	/**
	 * <p>最近六个月</p>
	 */
	private Double lastSixMonth;
	/**
	 * <p>最近一年</p>
	 */
	private Double lastYear;
	/**
	 * <p>最近两年</p>
	 */
	private Double lastTwoYear;
	/**
	 * <p>最近三年</p>
	 */
	private Double lastThreeYear;
	/**
	 * <p>成立以来</p>
	 */
	private Double total;

	public Double getLastWeek() {
		return lastWeek;
	}

	public void setLastWeek(Double lastWeek) {
		this.lastWeek = lastWeek;
	}

	public Double getLastMonth() {
		return lastMonth;
	}

	public void setLastMonth(Double lastMonth) {
		this.lastMonth = lastMonth;
	}

	public Double getLastThreeMonth() {
		return lastThreeMonth;
	}

	public void setLastThreeMonth(Double lastThreeMonth) {
		this.lastThreeMonth = lastThreeMonth;
	}

	public Double getLastSixMonth() {
		return lastSixMonth;
	}

	public void setLastSixMonth(Double lastSixMonth) {
		this.lastSixMonth = lastSixMonth;
	}

	public Double getLastYear() {
		return lastYear;
	}

	public void setLastYear(Double lastYear) {
		this.lastYear = lastYear;
	}

	public Double getLastTwoYear() {
		return lastTwoYear;
	}

	public void setLastTwoYear(Double lastTwoYear) {
		this.lastTwoYear = lastTwoYear;
	}

	public Double getLastThreeYear() {
		return lastThreeYear;
	}

	public void setLastThreeYear(Double lastThreeYear) {
		this.lastThreeYear = lastThreeYear;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}
	
}

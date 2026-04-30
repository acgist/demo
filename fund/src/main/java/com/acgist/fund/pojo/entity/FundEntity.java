package com.acgist.fund.pojo.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <p>基金</p>
 * 
 * TODO：费率
 * 
 * @author acgist
 */
public class FundEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>自选</p>
	 */
	private Boolean select;
	/**
	 * <p>基金编号</p>
	 */
	private String code;
	/**
	 * <p>基金名称</p>
	 */
	private String name;
	/**
	 * <p>基金类型：股票、债券等等</p>
	 */
	private String type;
	/**
	 * <p>基金经理</p>
	 */
	private String manager;
	/**
	 * <p>基金公司</p>
	 */
	private String company;
	/**
	 * <p>成立时间</p>
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date establishedDate;
	/**
	 * <p>价值</p>
	 */
	private WorthEntity worth;
	/**
	 * <p>收益</p>
	 */
	private EarningsEntity earnings;
	/**
	 * <p>资产占比</p>
	 * <p>资产：股票、债券、现金</p>
	 */
	private List<PositionEntity> assetPositions;
	/**
	 * <p>行业占比</p>
	 * <p>行业：制造业、金融业、建筑业、采掘业、房地产业、信息技术业、农林牧渔业、批发零售业、交通运输业、社会服务业</p>
	 */
	private List<PositionEntity> tradePositions;
	/**
	 * <p>股票占比</p>
	 * <p>股票：每个股票占比</p>
	 */
	private List<PositionEntity> stockPositions;
	/**
	 * <p>债券占比</p>
	 * <p>债券：每个债券占比</p>
	 */
	private List<PositionEntity> bondPositions;

	public Boolean getSelect() {
		return select;
	}

	public void setSelect(Boolean select) {
		this.select = select;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Date getEstablishedDate() {
		return establishedDate;
	}

	public void setEstablishedDate(Date establishedDate) {
		this.establishedDate = establishedDate;
	}

	public WorthEntity getWorth() {
		return worth;
	}

	public void setWorth(WorthEntity worth) {
		this.worth = worth;
	}

	public EarningsEntity getEarnings() {
		return earnings;
	}

	public void setEarnings(EarningsEntity earnings) {
		this.earnings = earnings;
	}

	public List<PositionEntity> getAssetPositions() {
		return assetPositions;
	}

	public void setAssetPositions(List<PositionEntity> assetPositions) {
		this.assetPositions = assetPositions;
	}

	public List<PositionEntity> getTradePositions() {
		return tradePositions;
	}

	public void setTradePositions(List<PositionEntity> tradePositions) {
		this.tradePositions = tradePositions;
	}

	public List<PositionEntity> getStockPositions() {
		return stockPositions;
	}

	public void setStockPositions(List<PositionEntity> stockPositions) {
		this.stockPositions = stockPositions;
	}

	public List<PositionEntity> getBondPositions() {
		return bondPositions;
	}

	public void setBondPositions(List<PositionEntity> bondPositions) {
		this.bondPositions = bondPositions;
	}

}

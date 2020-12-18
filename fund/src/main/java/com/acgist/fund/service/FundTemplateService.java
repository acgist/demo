package com.acgist.fund.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.acgist.fund.pojo.entity.EarningsEntity;
import com.acgist.fund.pojo.entity.FundEntity;
import com.acgist.fund.pojo.entity.PositionEntity;
import com.acgist.fund.pojo.entity.WorthEntity;
import com.acgist.fund.utils.JSONUtils;

/**
 * <p>基金数据提取</p>
 * 
 * @author acgist
 *
 */
@Service
@PropertySource("classpath:fund.template.properties")
public class FundTemplateService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FundTemplateService.class);

	@Value("${fund.name:}")
	private String fundName;
	@Value("${fund.type:}")
	private String fundType;
	@Value("${fund.manager:}")
	private String fundManager;
	@Value("${fund.company:}")
	private String fundCompany;
	@Value("${fund.established.date:}")
	private String fundEstablishedDate;
	@Value("${fund.worth.net.worth:}")
	private String fundWorthNetWorth;
	@Value("${fund.worth.total.net.worth:}")
	private String fundWorthTotalNetWorth;
	@Value("${fund.worth.market.value:}")
	private String fundWortMarketValue;
	@Value("${fund.earnings.last.week:}")
	private String fundEarningsLastWeek;
	@Value("${fund.earnings.last.month:}")
	private String fundEarningsLastMonth;
	@Value("${fund.earnings.last.three.month:}")
	private String fundEarningsLastThreeMonth;
	@Value("${fund.earnings.last.six.month:}")
	private String fundEarningsLastSixMonth;
	@Value("${fund.earnings.last.year:}")
	private String fundEarningsLastYear;
	@Value("${fund.earnings.last.two.year:}")
	private String fundEarningsLastTwoYear;
	@Value("${fund.earnings.last.three.year:}")
	private String fundEarningsLastThreeYear;
	@Value("${fund.earnings.total:}")
	private String fundEarningsTotal;
	@Value("${fund.asset.stock:}")
	private String fundAssetStock;
	@Value("${fund.asset.bond:}")
	private String fundAssetBond;
	@Value("${fund.asset.cash:}")
	private String fundAssetCash;
	@Value("${fund.stock:}")
	private String fundStock;
	@Value("${fund.bond:}")
	private String fundBond;
	
	/**
	 * <p>读取数据</p>
	 * 
	 * @param baseDocument 基本文档
	 * @param assetDocument 资产文档
	 * @param tradeJson 行业文档
	 * 
	 * @return 基金
	 */
	public FundEntity format(String code, Document baseDocument, Document assetDocument, String tradeJson) {
		final FundEntity entity = new FundEntity();
		entity.setCode(code);
		this.formatBase(entity, baseDocument);
		this.formatWorth(entity, baseDocument);
		this.formatEarnings(entity, baseDocument);
		this.formatAsset(entity, assetDocument);
		this.formatTrade(entity, tradeJson);
		this.formatStock(entity, baseDocument);
		this.formatBond(entity, baseDocument);
		return entity;
	}
	
	/**
	 * <p>读取基本信息</p>
	 * 
	 * @param entity 基金
	 * @param document 文档
	 */
	private void formatBase(FundEntity entity, Document document) {
		entity.setName(this.getString(document, this.fundName));
		entity.setType(this.getString(document, this.fundType));
		entity.setManager(this.getString(document, this.fundManager));
		entity.setCompany(this.getString(document, this.fundCompany));
		String fundEstablishedDate = this.getString(document, this.fundEstablishedDate);
		if(fundEstablishedDate != null) {
			fundEstablishedDate = fundEstablishedDate.substring(fundEstablishedDate.length() - 10);
			try {
				entity.setEstablishedDate(DateUtils.parseDate(fundEstablishedDate, "yyyy-MM-dd"));
			} catch (ParseException e) {
				LOGGER.error("时间转换异常", e);
			}
		}
	}
	
	/**
	 * <p>读取市值信息</p>
	 * 
	 * @param entity 基金
	 * @param document 文档
	 */
	private void formatWorth(FundEntity entity, Document document) {
		final WorthEntity worth = new WorthEntity();
		worth.setNetWorth(this.getDouble(document, this.fundWorthNetWorth));
		worth.setTotalNetWorth(this.getDouble(document, this.fundWorthTotalNetWorth));
		final String marketValue = this.getString(document, this.fundWortMarketValue);
		worth.setMarketValue(marketValue.substring(marketValue.indexOf('：') + 1, marketValue.indexOf('（')));
		entity.setWorth(worth);
	}
	
	/**
	 * <p>读取收益信息</p>
	 * 
	 * @param entity 基金
	 * @param document 文档
	 */
	private void formatEarnings(FundEntity entity, Document document) {
		final EarningsEntity earnings = new EarningsEntity();
		earnings.setLastWeek(this.getPercent(document, this.fundEarningsLastWeek));
		earnings.setLastMonth(this.getPercent(document, this.fundEarningsLastMonth));
		earnings.setLastThreeMonth(this.getPercent(document, this.fundEarningsLastThreeMonth));
		earnings.setLastSixMonth(this.getPercent(document, this.fundEarningsLastSixMonth));
		earnings.setLastYear(this.getPercent(document, this.fundEarningsLastYear));
		earnings.setLastTwoYear(this.getPercent(document, this.fundEarningsLastTwoYear));
		earnings.setLastThreeYear(this.getPercent(document, this.fundEarningsLastThreeYear));
		earnings.setTotal(this.getPercent(document, this.fundEarningsTotal));
		entity.setEarnings(earnings);
	}
	
	/**
	 * <p>读取资产信息</p>
	 * 
	 * @param entity 基金
	 * @param document 文档
	 */
	private void formatAsset(FundEntity entity, Document document) {
		final List<PositionEntity> list = new ArrayList<>();
		final PositionEntity stock = new PositionEntity();
		stock.setName("股票");
		stock.setPercent(this.getPercent(document, this.fundAssetStock));
		list.add(stock);
		final PositionEntity bond = new PositionEntity();
		bond.setName("债券");
		bond.setPercent(this.getPercent(document, this.fundAssetBond));
		list.add(bond);
		final PositionEntity cash = new PositionEntity();
		cash.setName("现金");
		cash.setPercent(this.getPercent(document, this.fundAssetCash));
		list.add(cash);
		entity.setAssetPositions(list);
	}
	
	/**
	 * <p>读取行业信息</p>
	 * 
	 * @param entity 基金
	 * @param document 文档
	 */
	private void formatTrade(FundEntity entity, String tradeJson) {
		if(StringUtils.isEmpty(tradeJson)) {
			return;
		}
		final Map<?, ?> map = JSONUtils.unserialize(tradeJson, Map.class);
		final Map<?, ?> data = (Map<?, ?>) map.get("Data");
		final List<?> quarterInfos = (List<?>) data.get("QuarterInfos");
		final Map<?, ?> quarterInfo = (Map<?, ?>) quarterInfos.get(0);
		final List<?> infos = (List<?>) quarterInfo.get("HYPZInfo");
		final List<PositionEntity> list = new ArrayList<>();
		for (Object info : infos) {
			final PositionEntity position = new PositionEntity();
			final Map<?, ?> infoMap = (Map<?, ?>) info;
			final var hymc = infoMap.get("HYMC");
			if(hymc instanceof String) {
				position.setName((String) hymc);
			} else {
				LOGGER.warn("数据错误（HYMC）：{}", hymc);
			}
			final var sz = infoMap.get("SZ");
			if(sz instanceof String) {
				position.setValue(this.getDouble((String) sz));
			} else if(sz instanceof Double) {
					position.setValue((Double) sz);
			} else {
				LOGGER.warn("数据错误（SZ）：{}", hymc);
			}
			final var zjzbl = infoMap.get("ZJZBL");
			if(zjzbl instanceof String) {
				position.setPercent(this.getDouble((String) zjzbl));
			} else if(zjzbl instanceof Double) {
				position.setPercent((Double) zjzbl);
			} else {
				LOGGER.warn("数据错误（ZJZBL）：{}", hymc);
			}
			list.add(position);
		}
		entity.setTradePositions(list);
	}
	
	/**
	 * <p>读取股票信息</p>
	 * 
	 * @param entity 基金
	 * @param document 文档
	 */
	private void formatStock(FundEntity entity, Document document) {
		final var elements = document.select(this.fundStock);
		if(elements == null || elements.isEmpty()) {
			return;
		}
		elements.remove(0);
		final List<PositionEntity> list = new ArrayList<>();
		for (Element element : elements) {
			final PositionEntity position = new PositionEntity();
			position.setName(this.getString(element, ".alignLeft"));
			position.setPercent(this.getPercent(element, ".alignRight"));
			list.add(position);
		}
		entity.setStockPositions(list);
	}
	
	/**
	 * <p>读取债券信息</p>
	 * 
	 * @param entity 基金
	 * @param document 文档
	 */
	private void formatBond(FundEntity entity, Document document) {
		final var elements = document.select(this.fundBond);
		if(elements == null || elements.isEmpty()) {
			return;
		}
		elements.remove(0);
		final List<PositionEntity> list = new ArrayList<>();
		for (Element element : elements) {
			final PositionEntity position = new PositionEntity();
			position.setName(this.getString(element, ".alignLeft"));
			position.setPercent(this.getPercent(element, ".alignRight"));
			list.add(position);
		}
		entity.setBondPositions(list);
	}
	
	/**
	 * <p>读取文本数据</p>
	 * 
	 * @param document 文档
	 * @param selector 选择器
	 * 
	 * @return 文本
	 */
	private String getString(Element document, String selector) {
		return this.getString(document, selector, 0);
	}
	
	/**
	 * <p>读取文本数据</p>
	 * 
	 * @param document 文档
	 * @param selector 选择器
	 * @param index 序号
	 * 
	 * @return 文本
	 */
	private String getString(Element document, String selector, int index) {
		final var elements = document.select(selector);
		if(elements == null || elements.isEmpty()) {
			LOGGER.error("读取数据错误：{}", selector);
			return null;
		}
		if(elements.size() < index) {
			LOGGER.error("读取数据错误：{}-{}-{}", elements.size(), index, selector);
			return null;
		}
		return elements.get(index).text().trim();
	}
	
	/**
	 * <p>读取数值数据</p>
	 * 
	 * @param document 文档
	 * @param selector 选择器
	 * 
	 * @return 数值
	 */
	private Double getDouble(Element document, String selector) {
		final String value = this.getString(document, selector);
		return this.getDouble(value);
	}
	
	/**
	 * <p>读取数值数据</p>
	 * 
	 * @param value 字符串
	 * 
	 * @return 数值
	 */
	private Double getDouble(String value) {
		if(value == null) {
			return null;
		}
		if(NumberUtils.isCreatable(value)) {
			return Double.valueOf(value);
		}
		return 0D;
	}

	/**
	 * <p>读取百分比数据</p>
	 * 
	 * @param document 文档
	 * @param selector 选择器
	 * 
	 * @return 百分比
	 */
	private Double getPercent(Element document, String selector) {
		final String value = this.getString(document, selector);
		return this.getPercent(value);
	}
	
	/**
	 * <p>读取百分比数据</p>
	 * 
	 * @param value 字符串
	 * 
	 * @return 百分比
	 */
	private Double getPercent(String value) {
		if(value == null) {
			return null;
		}
		if(value.length() > 0) {
			String percent = value.substring(0, value.length() - 1);
			if(NumberUtils.isCreatable(percent)) {
				return Double.valueOf(percent);
			}
		}
		return 0D;
	}
	
}

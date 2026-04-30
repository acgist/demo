package com.acgist.fund.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.acgist.fund.pojo.entity.FundEntity;
import com.acgist.fund.utils.HTTPClient;
import com.acgist.fund.utils.JSONUtils;

@Service
public class FundService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FundService.class);
	
	@Value("${fund.data.base.url:}")
	private String fundDataBaseUrl;
	@Value("${fund.data.asset.url:}")
	private String fundDataAssetUrl;
	@Value("${fund.data.trade.url:}")
	private String fundDataTradeUrl;
	@Value("${fund.data.trade.referer:}")
	private String fundDataTradeReferer;
	@Value("${fund.data.serialize.path:}")
	private String fundDataSerializePath;
	
	@Autowired
	private FundCodeService fundCodeService;
	@Autowired
	private FundTemplateService fundTemplateService;
	
	private List<FundEntity> list = new ArrayList<>();

	@PostConstruct
	private void init() {
		this.unserialize();
	}
	
	/**
	 * <p>获取所有基金</p>
	 * 
	 * @return 所有基金
	 */
	public List<FundEntity> list() {
		return this.list;
	}
	
	/**
	 * <p>更新所有基金</p>
	 */
	public void update() {
		this.fundCodeService.loadFundCode().forEach((code, select) -> this.update(code));
	}

	/**
	 * <p>更新指定基金</p>
	 * 
	 * @param code 基金编号
	 */
	public void update(String code) {
		LOGGER.info("更新指定基金：{}", code);
		final String baseHtml = HTTPClient.get(String.format(this.fundDataBaseUrl, code));
		final String assetHtml = HTTPClient.get(String.format(this.fundDataAssetUrl, code));
		final String tradeJson = HTTPClient.get(String.format(this.fundDataTradeUrl, code), this.fundDataTradeReferer);
		try {
			final var baseDocument = Jsoup.parse(baseHtml);
			final var assetDocument = Jsoup.parse(assetHtml);
			final var entity = this.fundTemplateService.format(code, baseDocument, assetDocument, tradeJson);
			entity.setSelect(this.fundCodeService.select(code)); // 自选
			final var old = this.list.stream().filter(value -> value.getCode().equals(entity.getCode())).findFirst();
			if(old.isPresent()) {
				this.list.remove(old.get());
			}
			this.list.add(entity);
			this.serialize(entity);
		} catch (Exception e) {
			LOGGER.error("更新基金异常：{}", code, e);
		}
	}
	
	/**
	 * <p>获取基金链接</p>
	 * 
	 * @param code 基金编号
	 * 
	 * @return 基金链接
	 */
	public String link(String code) {
		return String.format(this.fundDataBaseUrl, code);
	}
	
	/**
	 * <p>序列化数据</p>
	 */
	private void serialize(FundEntity entity) {
		final String json = JSONUtils.serialize(entity);
		try {
			final var parent = Paths.get(this.fundDataSerializePath).toFile();
			if(!parent.exists()) {
				parent.mkdirs();
			}
			Files.writeString(Paths.get(this.fundDataSerializePath, entity.getCode() + ".json"), json, StandardOpenOption.CREATE);
		} catch (IOException e) {
			LOGGER.error("序列化数据异常", e);
		}
	}

	/**
	 * <p>反序列化数据</p>
	 */
	private void unserialize() {
		this.fundCodeService.loadFundCode().forEach((code, select) -> {
			try {
				final Path path = Paths.get(this.fundDataSerializePath, code + ".json");
				if(path.toFile().exists()) {
					final String json = Files.readString(path);
					this.list.add(JSONUtils.unserialize(json, FundEntity.class));
				}
			} catch (IOException e) {
				LOGGER.error("反序列化数据异常：{}", code, e);
			}
		});
	}
	
}

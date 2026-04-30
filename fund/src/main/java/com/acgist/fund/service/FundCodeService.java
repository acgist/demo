package com.acgist.fund.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * <p>基金编号管理</p>
 * 
 * @author acgist
 */
@Service
public class FundCodeService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FundCodeService.class);

	@Value("${fund.code:}")
	private String fundCode;
	
	private Map<String, Boolean> fundCodeMap;
	
	/**
	 * <p>加载基金编号</p>
	 * 
	 * @return 基金编号
	 */
	public Map<String, Boolean> loadFundCode() {
		if(this.fundCodeMap != null) {
			return this.fundCodeMap;
		}
		final Map<String, Boolean> map = new LinkedHashMap<>();
		try (final var input = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(this.fundCode), "UTF-8"))) {
			String line;
			while((line = input.readLine()) != null) {
				if(
					StringUtils.isNotEmpty(line) &&
					!StringUtils.startsWith(line, "#")
				) {
					if(StringUtils.endsWith(line, "*")) {
						map.put(line.substring(0, 6), true);
					} else {
						map.put(line, false);
					}
				}
			}
		} catch (IOException e) {
			LOGGER.error("加载基金编码异常", e);
		}
		return this.fundCodeMap = map;
	}
	
	/**
	 * <p>是否自选</p>
	 * 
	 * @param code 基金编号
	 * 
	 * @return 是否自选
	 */
	public Boolean select(String code) {
		return this.fundCodeMap.getOrDefault(code, false);
	}

}

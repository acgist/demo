package com.acgist.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 编号生成
 */
@Service
public class UniqueIdService {

	@Value("${server.sn:1001}")
	private String serverSN;

	private static final int MIN_INDEX = 10000;
	private static final int MAX_INDEX = 99999;
	private static int ID_INDEX = MIN_INDEX;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UniqueIdService.class);
	
	public void init() {
		LOGGER.info("系统序号：{}", this.serverSN);
	}
	
	/**
	 * 系统唯一ID
	 */
	public synchronized String id() {
		final StringBuffer builder = new StringBuffer();
		final SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmmss");
		builder.append(formater.format(new Date()));
		builder.append(ID_INDEX);
		ID_INDEX++;
		if(ID_INDEX > MAX_INDEX) {
			ID_INDEX = MIN_INDEX;
		}
		return builder.toString();
	}
	
}

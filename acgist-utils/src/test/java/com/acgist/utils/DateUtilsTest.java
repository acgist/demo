package com.acgist.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.Date;

import org.junit.jupiter.api.Test;

public class DateUtilsTest extends Performance {

	@Test
	public void testFormat() {
		final String value = DateUtils.format(1000000);
		this.log(value);
		assertNotNull(value);
	}
	
	@Test
	public void testDateToString() {
		String value = DateUtils.dateToString(new Date());
		this.log(value);
		assertNotNull(value);
		value = DateUtils.dateToString(new Date(), "yyyyMMdd");
		this.log(value);
		assertNotNull(value);
	}

	@Test
	public void testTimestamp() {
		assertNotNull(DateUtils.javaTimestamp());
		assertNotNull(DateUtils.unixTimestamp());
		assertNotNull(DateUtils.javaToUnixTimestamp(DateUtils.javaTimestamp()));
		assertNotNull(DateUtils.unixToJavaDate(DateUtils.unixTimestamp()));
		assertNotNull(DateUtils.unixToJavaTimestamp(DateUtils.unixTimestamp()));
	}
	
	@Test
	public void testDiff() {
		assertEquals(2, DateUtils.diff(LocalDateTime.of(2020, 12, 12, 12, 12, 12), LocalDateTime.of(2020, 12, 12, 14, 12, 12)).toHours());
	}
	
}

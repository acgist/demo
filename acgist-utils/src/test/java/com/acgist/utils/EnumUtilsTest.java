package com.acgist.utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.OS;

public class EnumUtilsTest extends Performance {

	@Test
	public void testUnpack() {
		this.log(EnumUtils.unpack(OS.class, "WINDOWS").getClass());
		assertNotNull(EnumUtils.unpack(OS.class, "WINDOWS"));
	}
	
}

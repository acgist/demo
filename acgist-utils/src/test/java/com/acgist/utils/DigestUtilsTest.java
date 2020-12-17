package com.acgist.utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class DigestUtilsTest extends Performance {

	@Test
	public void testDigest() {
		assertNotNull(DigestUtils.md5());
		assertNotNull(DigestUtils.sha1());
	}
	
}

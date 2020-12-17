package com.acgist.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

public class MapUtilsTest extends Performance {

	@Test
	public void testEmpty() {
		assertTrue(MapUtils.isEmpty(new HashMap<>()));
		assertFalse(MapUtils.isEmpty(new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;
			{
				put("1", "1");
			}
		}));
		assertFalse(MapUtils.isNotEmpty(new HashMap<>()));
		assertTrue(MapUtils.isNotEmpty(new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;
			{
				put("1", "1");
			}
		}));
	}
	
	
}

package com.acgist.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class CollectionUtilsTest extends Performance {

	@Test
	public void testEmpty() {
		assertTrue(CollectionUtils.isEmpty(new ArrayList<Object>()));
		assertFalse(CollectionUtils.isEmpty(new ArrayList<Object>() {
			private static final long serialVersionUID = 1L;
			{
				add("1");
			}
		}));
		assertFalse(CollectionUtils.isNotEmpty(new ArrayList<Object>()));
		assertTrue(CollectionUtils.isNotEmpty(new ArrayList<Object>() {
			private static final long serialVersionUID = 1L;
			{
				add("1");
			}
		}));
	}
	
}

package com.acgist.utils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ArrayUtilsTest extends Performance {

	@Test
	public void testXor() {
		final byte[] sources = "1234".getBytes();
		final byte[] targets = "1234".getBytes();
		final byte[] result = ArrayUtils.xor(sources, targets);
		this.log(result);
		assertArrayEquals(new byte[] {0, 0, 0, 0}, result);
	}
	
	@Test
	public void testDiffIndex() {
		byte[] sources = "1X34".getBytes();
		byte[] targets = "1234".getBytes();
		int index = ArrayUtils.diffIndex(sources, targets);
		this.log(index);
		assertEquals(1, index);
		sources = "1234".getBytes();
		targets = "1234".getBytes();
		index = ArrayUtils.diffIndex(sources, targets);
		this.log(index);
		assertEquals(4, index);
	}
	
	@Test
	public void testEmpty() {
		assertTrue(ArrayUtils.isEmpty((byte[]) null));
		assertTrue(ArrayUtils.isEmpty("".getBytes()));
		assertFalse(ArrayUtils.isEmpty("1234".getBytes()));
		assertTrue(ArrayUtils.isEmpty((Object[]) null));
		assertTrue(ArrayUtils.isEmpty(new Object[] {}));
		assertFalse(ArrayUtils.isEmpty(new Object[] {1, 2, 3, 4}));
		assertFalse(ArrayUtils.isNotEmpty((byte[]) null));
		assertFalse(ArrayUtils.isNotEmpty("".getBytes()));
		assertTrue(ArrayUtils.isNotEmpty("1234".getBytes()));
		assertFalse(ArrayUtils.isNotEmpty((Object[]) null));
		assertFalse(ArrayUtils.isNotEmpty(new Object[] {}));
		assertTrue(ArrayUtils.isNotEmpty(new Object[] {1, 2, 3, 4}));
	}
	
	@Test
	public void testRandom() {
		final byte[] random = ArrayUtils.random(10);
		this.log(random);
		assertNotNull(random);
	}
	
	@Test
	public void testIndexOf() {
		int index = ArrayUtils.indexOf("1234".getBytes(), (byte) '1');
		assertEquals(0, index);
		index = ArrayUtils.indexOf("1231".getBytes(), 1, 4, (byte) '1');
		assertEquals(3, index);
		index = ArrayUtils.indexOf("1234".getBytes(), 1, 6, (byte) '1');
		assertEquals(-1, index);
		index = ArrayUtils.indexOf(new char[] {1, 2, 3, 4}, (char) 1);
		assertEquals(0, index);
		index = ArrayUtils.indexOf(new char[] {1, 2, 3, 1}, 1, 4, (char) 1);
		assertEquals(3, index);
		index = ArrayUtils.indexOf(new char[] {1, 2, 3, 4}, 1, 6, (char) 1);
		assertEquals(-1, index);
		index = ArrayUtils.indexOf(new int[] {1, 2, 3, 4}, 1);
		assertEquals(0, index);
		index = ArrayUtils.indexOf(new int[] {1, 2, 3, 1}, 1, 4, 1);
		assertEquals(3, index);
		index = ArrayUtils.indexOf(new int[] {1, 2, 3, 4}, 1, 6, 1);
		assertEquals(-1, index);
		index = ArrayUtils.indexOf(new String[] {"1", "2", "3", "4"}, "1");
		assertEquals(0, index);
		index = ArrayUtils.indexOf(new String[] {"1", "2", "3", "1"}, 1, 4, "1");
		assertEquals(3, index);
		index = ArrayUtils.indexOf(new String[] {"1", "2", "3", "4"}, 1, 6, "1");
		assertEquals(-1, index);
	}
	
}
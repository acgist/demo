package com.acgist.utils;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import net.coobird.thumbnailator.Thumbnails;

public class ImageTest {

	@Test
	public void testScale() throws IOException {
		Thumbnails.of("E://tmp/google.jpg").scale(0.5F).toFile("E://tmp/google.scale.jpg");
	}
	
}

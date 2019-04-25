package com.acgist.common.image;

import java.io.IOException;

import net.coobird.thumbnailator.Thumbnails;

public class ImageUtils {

	public static void main(String[] args) throws IOException {
		Thumbnails.of("e://demo.png").scale(0.5F).toFile("e://test.jpg");
	}
	
}

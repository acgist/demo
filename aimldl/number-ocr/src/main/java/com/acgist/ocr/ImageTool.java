package com.acgist.ocr;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mortennobel.imagescaling.ResampleOp;

/**
 * 图片处理工具
 */
public class ImageTool {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageTool.class);

	/**
	 * 灰度化
	 */
	public static final void gray(String filePath, String newFilePath) {
		File file = new File(filePath);
		try {
			BufferedImage image = ImageIO.read(file);
			File newFile = new File(newFilePath);
			ImageIO.write(gray(image), "jpg", newFile);
		} catch (IOException e) {
			LOGGER.error("图片灰度化异常", e);
		}
	}
	
	/**
	 * 灰度化
	 */
	public static final BufferedImage gray(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY); // 参数：BufferedImage.TYPE_BYTE_GRAY
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int rgb = image.getRGB(i, j);
				grayImage.setRGB(i, j, rgb);
			}
		}
		return grayImage;
	}
	
	/**
	 * 二值化
	 */
	public static final void binary(String filePath, String newFilePath) {
		File file = new File(filePath);
		try {
			BufferedImage image = ImageIO.read(file);
			File newFile = new File(newFilePath);
			ImageIO.write(binary(image), "jpg", newFile);
		} catch (IOException e) {
			LOGGER.error("图片二值化异常", e);
		}
	}
	
	/**
	 * 二值化
	 */
	public static final BufferedImage binary(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage binaryImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY); // 参数：BufferedImage.TYPE_BYTE_BINARY
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int rgb = image.getRGB(i, j);
				binaryImage.setRGB(i, j, rgb);
			}
		}
		return binaryImage;
	}

	/**
	 * 反色
	 */
	public static final void inverse(String filePath, String newFilePath) {
		File file = new File(filePath);
		try {
			BufferedImage image = ImageIO.read(file);
			File newFile = new File(newFilePath);
			ImageIO.write(inverse(image), "jpg", newFile);
		} catch (IOException e) {
			LOGGER.error("图片反色异常", e);
		}
	}
	
	/**
	 * 反色
	 */
	public static final BufferedImage inverse(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage inverseImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int pixel = image.getRGB(j, i);
				inverseImage.setRGB(j, i, 0xFFFFFF - pixel);
			}
		}
		return inverseImage;
	}
	
	/**
	 * 添加背景色：白色
	 */
	public static final void background(String filePath, String newFilePath) {
		File file = new File(filePath);
		try {
			BufferedImage image = ImageIO.read(file);
			File newFile = new File(newFilePath);
			ImageIO.write(background(image), "jpg", newFile);
		} catch (IOException e) {
			LOGGER.error("图片添加背景色异常", e);
		}
	}
	
	/**
	 * 添加背景色：白色
	 */
	public static final BufferedImage background(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage inverseImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int pixel = image.getRGB(j, i);
				if(pixel == 0) {
					pixel = 0xFFFFFF;
				}
				inverseImage.setRGB(j, i, pixel);
			}
		}
		return inverseImage;
	}

	/**
	 * PNG前缀
	 */
	private static final String BASE64IMAGE_HEADER_PNG = "data:image/png;base64,";
	
	/**
	 * JPG前缀
	 */
	private static final String BASE64IMAGE_HEADER_JPEG = "data:image/jpeg;base64,";
	
	/**
	 * BASE64图片文本转图片流
	 */
	public static final byte[] base64Content2image(String base64Content) {
		if (base64Content.startsWith(BASE64IMAGE_HEADER_PNG)) {
			base64Content = base64Content.substring(BASE64IMAGE_HEADER_PNG.length());
		} else if (base64Content.startsWith(BASE64IMAGE_HEADER_JPEG)) {
			base64Content = base64Content.substring(BASE64IMAGE_HEADER_JPEG.length());
		} else {
			throw new IllegalArgumentException("不支持的图片数据");
		}
		Base64 base64 = new Base64();
		ByteArrayInputStream input = new ByteArrayInputStream(base64.decode(base64Content));
		byte[] bytes = new byte[input.available()];
		try {
			input.read(bytes);
		} catch (IOException e) {
			LOGGER.error("BASE64图片读取异常", e);
		} finally {
			IOUtils.closeQuietly(input);
		}
		for (int index = 0; index < bytes.length; index++) {
			if (bytes[index] < 0) {
				bytes[index] += 256;
			}
		}
		return bytes;
	}
	
	/**
	 * 修改图片大小
	 */
	public static final BufferedImage resize(BufferedImage image, int width, int height) {
		ResampleOp resampleOp = new ResampleOp(width, height);
		BufferedImage resizeImage = resampleOp.filter(image, null);
		// 优化图片效果，使效果更加明显
		int rwidth = resizeImage.getWidth();
		int rheight = resizeImage.getHeight();
		BufferedImage inverseImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		long total = 0;
		int size = 0;
		for (int i = 0; i < rheight; i++) {
			for (int j = 0; j < rwidth; j++) {
				int pixel = resizeImage.getRGB(j, i);
				if(pixel != -16777216) {
					size++;
					total+=pixel;
				}
			}
		}
		int avg = (int) (total / size); // 注意这里使用的是平均数
		for (int i = 0; i < rheight; i++) {
			for (int j = 0; j < rwidth; j++) {
				int pixel = resizeImage.getRGB(j, i);
				if(pixel != -16777216) {
					if(pixel > avg) {
						pixel = -1;
					} else {
						pixel = -16777216;
					}
				}
				inverseImage.setRGB(j, i, pixel);
			}
		}
//		return resizeImage;
		return inverseImage;
	}
	
}

package com.acgist.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>文件工具</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public final class FileUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);
	
	/**
	 * <p>工具类禁止实例化</p>
	 */
	private FileUtils() {
	}
	
	/**
	 * <p>获取文件后缀</p>
	 * 
	 * @param fileName 文件名称
	 * 
	 * @return 文件后缀
	 */
	public static final String fileExt(String fileName) {
		if(StringUtils.isEmpty(fileName)) {
			return null;
		}
		final int index = fileName.lastIndexOf(".");
		if(index != -1) {
			return fileName.substring(index + 1);
		}
		return null;
	}
	
	/**
	 * <p>文件写入</p>
	 * 
	 * @param filePath 文件路径
	 * @param bytes 文件数据
	 */
	public static final void write(String filePath, byte[] bytes) {
		buildFolder(filePath, true); // 创建目录
		try(final var output = new FileOutputStream(filePath)) {
			output.write(bytes);
		} catch (IOException e) {
			LOGGER.error("文件写入异常", e);
		}
	}
	
	/**
	 * <p>创建目录</p>
	 * 
	 * @param path 文件路径
	 * @param isFile {@code path}是否是文件：{@code true}-文件；{@code false}-目录；
	 * 
	 * @see #buildFolder(File, boolean)
	 */
	public static final void buildFolder(String path, boolean isFile) {
		final File file = new File(path);
		buildFolder(file, isFile);
	}
	
	/**
	 * <p>创建目录</p>
	 * <p>如果{@code file}是文件：创建父目录</p>
	 * <p>如果{@code file}是目录：创建目录</p>
	 * 
	 * @param file 文件
	 * @param isFile {@code opt}是否是文件：{@code true}-文件；{@code false}-目录；
	 */
	public static final void buildFolder(File file, boolean isFile) {
		if(file.exists()) {
			return;
		}
		if(isFile) {
			file = file.getParentFile();
		}
		if(!file.exists()) {
			file.mkdirs();
		}
	}
	
}

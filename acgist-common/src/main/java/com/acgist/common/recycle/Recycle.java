package com.acgist.common.recycle;

import java.io.File;

import com.acgist.common.StringUtils;

/**
 * <p>回收站</p>
 * 
 * @author acgist
 * @since 1.1.0
 */
public abstract class Recycle {

	/**
	 * <p>原始文件路径</p>
	 */
	protected final String path;
	/**
	 * <p>原始文件</p>
	 */
	protected final File file;
	
	/**
	 * <p>回收站构造器</p>
	 * 
	 * @param path 文件路径：必须是完整路径（不能填写相对路径）
	 */
	protected Recycle(String path) {
		if(StringUtils.isEmpty(path)) {
			throw new IllegalArgumentException("路径格式错误：" + path);
		}
		this.path = path;
		this.file = new File(path);
	}
	
	/**
	 * <p>删除文件</p>
	 * 
	 * @return {@code true}-成功；{@code false}-失败；
	 */
	public abstract boolean delete();

}

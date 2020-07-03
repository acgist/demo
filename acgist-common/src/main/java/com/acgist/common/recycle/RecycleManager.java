package com.acgist.common.recycle;

import com.acgist.common.recycle.windows.WindowsRecycle;

/**
 * <p>回收站管理器</p>
 * 
 * @author acgist
 * @since 1.1.0
 */
public final class RecycleManager {

	public static void main(String[] args) {
		final Recycle recycle = new WindowsRecycle("e://test.txt");
		recycle.delete();
	}
	
}

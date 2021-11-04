package com.acgist.netty;

import java.io.Closeable;
import java.io.IOException;

public class IoUtils {

	private IoUtils() {
	}
	
	public static final void close(Closeable closeable) {
		if(closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				// TODO：log
			}
		}
	}
	
}

package com.acgist.hadoop.fs;

import java.io.InputStream;
import java.net.URL;

import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.io.IOUtils;

public class URLMain {

	public static void main(String[] args) {
		URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
		try (InputStream input = new URL("hdfs://192.168.1.222:9000/home/tianqi/2016").openStream()) {
			IOUtils.copyBytes(input, System.out, 1024);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

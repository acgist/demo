package com.acgist.hadoop.fs;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

public class FSMain {

	public static void main(String[] args) throws IOException {
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create("hdfs://192.168.1.222:9000"), conf);
		FSDataInputStream input = fs.open(new Path("/home/tianqi/2016"));
//		IOUtils.copyBytes(input, System.out, 1024);
		FSDataOutputStream output = fs.create(new Path("/home/tianqi/2020"));
		IOUtils.copyBytes(input, output, 2014);
	}

}

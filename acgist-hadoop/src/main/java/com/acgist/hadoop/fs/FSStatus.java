package com.acgist.hadoop.fs;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class FSStatus {

	public static void main(String[] args) throws IOException {
		System.setProperty("HADOOP_USER_NAME", "root"); // 权限
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create("hdfs://192.168.1.222:9000"), conf);
//		FileStatus[] files = fs.listStatus(new Path("/home/"));
//		for (FileStatus fileStatus : files) {
//			System.out.println(fileStatus);
//		}
//		RemoteIterator<LocatedFileStatus> files = fs.listFiles(new Path("/home/"), true);
//		while(files.hasNext()) {
//			System.out.println(files.next());
//		}
		FileStatus[] files = fs.globStatus(new Path("/home/*/*"));
		for (FileStatus fileStatus : files) {
			System.out.println(fileStatus);
		}
	}
	
}

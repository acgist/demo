package com.acgist.hadoop.fs;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.util.StringUtils;

public class Seriz {

	public static void main(String[] args) throws IOException {
		IntWritable writable = new IntWritable(16);
//		BytesWritable writable = new BytesWritable(new byte[] {3, 5});
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(bytes);
		writable.write(out);
		System.out.println(StringUtils.byteToHexString(bytes.toByteArray()));
	}
	
}

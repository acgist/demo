package com.acgist.hadoop.fs;

import org.apache.hadoop.io.Text;

public class Writeable {

	public static void main(String[] args) {
//		IntWritable i = new IntWritable();
//		System.out.println(i.get());
		String value = "1234";
		Text text = new Text(value);
		System.out.println(value.length());
		System.out.println(value.getBytes().length);
		System.out.println(text.getLength());
		System.out.println(text.getBytes().length);
		System.out.println("---------");
		value = "你们好！";
		text = new Text(value);
		System.out.println(value.length());
		System.out.println(value.getBytes().length);
		System.out.println(text.getLength());
		System.out.println(text.getBytes().length);
		System.out.println("---------");
		value = "text";
		text = new Text(value);
		System.out.println(value.length());
		System.out.println(value.getBytes().length);
		System.out.println(text.getLength());
		System.out.println(text.getBytes().length);
	}
	
}

package com.acgist.hadoop.fs;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.apache.hadoop.io.SequenceFile.Writer;
import org.apache.hadoop.io.Text;
import org.junit.Test;

public class SeqTest {

	@Test
	public void write() throws IOException {
		System.setProperty("HADOOP_USER_NAME", "root"); // 权限
		Configuration conf = new Configuration();
		Path path = new Path("hdfs://192.168.1.222:9000/home/acgist/seq");
		IntWritable key = new IntWritable();
		Text value = new Text();
		SequenceFile.Writer writer = SequenceFile.createWriter(
			conf,
			Writer.file(path),
			Writer.keyClass(key.getClass()),
			Writer.valueClass(value.getClass())
		);
//		SequenceFile.Writer writer = SequenceFile.createWriter(fs, conf, path, key.getClass(), value.getClass());
		for (int i = 0; i < 100; i++) {
			key.set(i);
			value.set("index=" + i);
			writer.append(key, value);
		}
		writer.close();
	}
	
	@Test
	public void read() throws IOException {
		System.setProperty("HADOOP_USER_NAME", "root"); // 权限
		Configuration conf = new Configuration();
		Path path = new Path("hdfs://192.168.1.222:9000/home/acgist/seq");
		IntWritable key = new IntWritable();
		Text value = new Text();
		SequenceFile.Reader reader = new SequenceFile.Reader(
			conf,
			Reader.file(path)
		);
//		reader.seek(10);
		while(reader.next(key, value)) {
			System.out.println(key + "----" + value);
		}
		reader.close();
	}
	
}

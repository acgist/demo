package com.acgist.hadoop.wordcount;
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {

	@Override
	protected void map(Object key, Text value, Mapper<Object, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException {
		final Text token = new Text();
		final IntWritable one = new IntWritable(1);
		final StringTokenizer tokenizer = new StringTokenizer(value.toString());
		while(tokenizer.hasMoreTokens()) {
			token.set(tokenizer.nextToken());
			context.write(token, one);
		}
	}
	
}

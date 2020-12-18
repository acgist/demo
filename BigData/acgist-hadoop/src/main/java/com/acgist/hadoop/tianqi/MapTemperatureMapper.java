package com.acgist.hadoop.tianqi;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * 最高气温获取
 */
public class MapTemperatureMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException {
		String data = value.toString();
		String month = data.substring(0, 4);
		String temperature = data.substring(4).trim();
		context.write(new Text(month), new IntWritable(Integer.valueOf(temperature)));
	}

}

package com.acgist.hadoop.time;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class TimeMain {
	
	private static final String REGEX_DATA = ".*(\\d{20})交易时间(\\d{1})=======================(\\d{13}).*";
	private static final Pattern PATTERN = Pattern.compile(REGEX_DATA);

	public static class TimeMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

		@Override
		protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, LongWritable>.Context context) throws IOException, InterruptedException {
			Matcher matcher = PATTERN.matcher(value.toString());
			if(matcher.matches()) {
				String timeSn = matcher.group(1);
//				String index = matcher.group(2);
				String time = matcher.group(3);
				context.write(new Text(timeSn), new LongWritable(Long.valueOf(time)));
			}
		}

	}

	public static class TimeReducer extends Reducer<Text, LongWritable, Text, LongWritable> {

		@Override
		protected void reduce(Text key, Iterable<LongWritable> values, Reducer<Text, LongWritable, Text, LongWritable>.Context context) throws IOException, InterruptedException {
			int size = 0;
			long minValue = Long.MAX_VALUE;
			long maxValue = Long.MIN_VALUE;
			for (LongWritable value : values) {
				size++;
				minValue = Math.min(minValue, value.get());
				maxValue = Math.max(maxValue, value.get());
			}
			if(size == 7) {
				context.write(key, new LongWritable(maxValue - minValue));
			}
		}
		
	}

	public static void main(String[] args) throws Exception {
		Job job = Job.getInstance();
		job.setJarByClass(TimeMain.class); // 不用指定JAR名称
		job.setJobName("时间差分析");
		
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		job.setMapperClass(TimeMapper.class);
		job.setReducerClass(TimeReducer.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);
		
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
	
}

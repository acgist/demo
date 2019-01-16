package com.acgist.hadoop.sort;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.partition.InputSampler;
import org.apache.hadoop.mapreduce.lib.partition.TotalOrderPartitioner;

public class AllSortMain {

	public static class MaxTempMapper extends Mapper<LongWritable, Text, LongWritable, LongWritable> {
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			if(StringUtils.isNumeric(value.toString())) {
				context.write(new LongWritable(Long.valueOf(value.toString())), new LongWritable(Long.valueOf(value.toString())));
			}
		}
	}

	public static class MaxTempReducer extends Reducer<LongWritable, LongWritable, LongWritable, LongWritable> {
		protected void reduce(LongWritable key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
//			long max = Long.MIN_VALUE;
			for (LongWritable value : values) {
//				max = max > value.get() ? max : value.get();
				context.write(key, value);
			}
//			context.write(key, new LongWritable(max));
		}
	}

	public static class SortPartitioner extends Partitioner<LongWritable, LongWritable> {
		public int getPartition(LongWritable value, LongWritable temp, int numPartitions) {
			long part = value.get();
			if (part < 25L) {
				return 0;
			} else if (part < 50L) {
				return 1;
			} else if (part < 75L) {
				return 2;
			} else {
				return 3;
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);
		if (fs.exists(new Path(args[1]))) {
			fs.delete(new Path(args[1]), true);
		}
		Job job = Job.getInstance(conf);
		job.setJobName("最大气温计算");
		job.setJarByClass(AllSortMain.class);
		job.setMapperClass(MaxTempMapper.class);
		job.setReducerClass(MaxTempReducer.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
//		job.setInputFormatClass(SequenceFileInputFormat.class);
		job.setNumReduceTasks(4); // reduce个数
		job.setMapOutputKeyClass(LongWritable.class);
		job.setMapOutputValueClass(LongWritable.class);
		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(LongWritable.class);
		// 自定义分区
//		job.setPartitionerClass(SortPartitioner.class);
		// hadoop采样分区
		job.setPartitionerClass(TotalOrderPartitioner.class);
		TotalOrderPartitioner.setPartitionFile(job.getConfiguration(), new Path(args[2]));
		// 采样比例：最大采样数量：最大切割数量
		InputSampler.Sampler<LongWritable, LongWritable> sampler = new InputSampler.RandomSampler<LongWritable, LongWritable>(1F, 40000, 4);
		InputSampler.writePartitionFile(job, sampler);
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}

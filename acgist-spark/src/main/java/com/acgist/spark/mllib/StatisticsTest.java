package com.acgist.spark.mllib;

import java.util.stream.Stream;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.stat.MultivariateStatisticalSummary;
import org.apache.spark.mllib.stat.Statistics;

/**
 * 摘要统计
 */
public class StatisticsTest {

	public static void main(String[] args) {
//		SparkConf conf = new SparkConf().setMaster("local").setAppName("StatisticsTest");
		SparkConf conf = new SparkConf().setMaster("spark://master:7077").setAppName("StatisticsTest");
		conf.set("spark.driver.host", "192.168.1.100");
		conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
		JavaSparkContext sc = new JavaSparkContext(conf);
//		JavaRDD<Integer> rdd = sc.parallelize(Arrays.asList(1, 2, 3, 4));
		JavaRDD<Vector> rdd = sc.textFile("hdfs://master:9000/home/ml/nums.txt")
		.flatMap(value -> {
			String[] values = value.split(" ");
			return Stream.of(values).map(Double::valueOf).map(Vectors::dense).iterator();
		});
		System.out.println(rdd.collect());
		MultivariateStatisticalSummary result = Statistics.colStats(rdd.rdd());
		System.out.println(result.max());
		System.out.println(result.min());
		System.out.println(result.mean());
		System.out.println(result.count());
		sc.close();
	}
	
}

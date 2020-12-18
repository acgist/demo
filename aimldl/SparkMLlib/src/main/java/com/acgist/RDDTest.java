package com.acgist;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.linalg.Matrix;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.stat.MultivariateStatisticalSummary;
import org.apache.spark.mllib.stat.Statistics;
import org.apache.spark.mllib.util.KMeansDataGenerator;
import org.apache.spark.rdd.RDD;
import org.junit.Before;
import org.junit.Test;

public class RDDTest {
	
	private SparkConf conf;
	private JavaSparkContext context;
	private List<String> list;

	@Before
	public void init() {
		conf = new SparkConf().setAppName("test").setMaster("local");
		context = new JavaSparkContext(conf);
		list = Arrays.asList("1", "2", "3");
	}
	
	@Test
	public void parallelizeTest() {
		JavaRDD<String> rdd = context.parallelize(list, 2);
		System.out.println(rdd.count());
	}

	@Test
	public void map() {
		JavaRDD<String> rdd = context.parallelize(list, 2);
		List<Integer> list = rdd.map(value -> Integer.valueOf(value + "0")).collect();
		System.out.println(list);
	}
	
	/**
	 * 拉链操作
	 */
	@Test
	public void zip() {
		JavaRDD<String> rdda = context.parallelize(list, 2);
		JavaRDD<String> rddb = context.parallelize(list, 2);
		JavaPairRDD<String, String> rddc = rdda.zip(rddb);
		System.out.println(rddc.collect());
	}
	
	@Test
	public void stats() {
		JavaRDD<String> rdd = context.parallelize(list, 2);
		JavaRDD<Vector> values = rdd.map(value -> Vectors.dense(Long.valueOf(value)));
		MultivariateStatisticalSummary stats = Statistics.colStats(values.rdd());
		System.out.println(stats.max());
		System.out.println(stats.mean());
		System.out.println(stats.variance());
	}
	
	@Test
	public void corr() {
		JavaRDD<String> rdd = context.parallelize(list, 2);
		JavaRDD<Vector> values = rdd.map(value -> Vectors.dense(Long.valueOf(value)));
		Matrix matrix = Statistics.corr(values.rdd(), "pearson");
		System.out.println(matrix);
		matrix = Statistics.corr(values.rdd(), "spearman");
		System.out.println(matrix);
	}
	
	@Test
	public void generate() {
		RDD<double[]> rdd = KMeansDataGenerator.generateKMeansRDD(context.sc(), 40, 5, 3, 1.0D, 2);
		System.out.println(rdd.collect());
	}
	
}

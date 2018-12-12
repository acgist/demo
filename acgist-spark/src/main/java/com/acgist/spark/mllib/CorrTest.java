package com.acgist.spark.mllib;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.linalg.Matrix;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.stat.Statistics;

/**
 * 相关性
 */
public class CorrTest {

	public static void main(String[] args) {
		List<Integer> list = Arrays.asList(100, 4, 200, 3);
//		Collections.reverse(list);
		SparkConf conf = new SparkConf().setMaster("local").setAppName("StatisticsTest");
//		SparkConf conf = new SparkConf().setMaster("spark://master:7077").setAppName("StatisticsTest");
		conf.set("spark.driver.host", "192.168.1.100");
		conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaRDD<Vector> rdd = sc.parallelize(list)
		.flatMap(value -> {
			return Stream.of(value).map(Double::valueOf).map(Vectors::dense).iterator();
		});
//		rdd.sample(withReplacement, fraction); // 分层抽样
		Matrix result = Statistics.corr(rdd.rdd(), "pearson"); // 皮尔斯相关系数
//		Matrix result = Statistics.corr(rdd.rdd(), "spearman"); // 斯皮尔森秩相关系数
		System.out.println(result);
		sc.close();
	}
	
}

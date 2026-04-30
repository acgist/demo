package com.acgist.spark.rdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class RDDTest {

	public static void main(String[] args) {
//		SparkConf conf = new SparkConf().setMaster("local").setAppName("RDDTest");
		SparkConf conf = new SparkConf().setMaster("spark://master:7077").setAppName("RDDTest");
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaRDD<String> rdd = sc.textFile("hdfs://master:9000/WordCount/WordCount.txt");
		rdd.map(value -> value.length()).saveAsTextFile("hdfs://master:9000/WordCount/result");
//		rdd.map(value -> value.length()).collect().forEach(System.out::println);
		sc.close();
	}

}

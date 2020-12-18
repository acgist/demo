package com.acgist.cluster;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.junit.Before;
import org.junit.Test;

/**
 * KMeans聚类
 */
public class KMeansTest {

	private SparkConf conf;
	private JavaSparkContext context;
	private List<Vector> list;

	@Before
	public void init() {
		conf = new SparkConf().setAppName("test").setMaster("local");
		context = new JavaSparkContext(conf);
		list = new ArrayList<>();
	}
	
	@Test
	public void kmeans() {
		list.add(Vectors.dense(1D, 1D));
		list.add(Vectors.dense(1D, 2D));
		list.add(Vectors.dense(100D, 100D));
		list.add(Vectors.dense(101D, 100D));
		list.add(Vectors.dense(200D, 200D));
		list.add(Vectors.dense(201D, 200D));
		list.add(Vectors.dense(300D, 300D));
		list.add(Vectors.dense(301D, 300D));
		
		JavaRDD<Vector> data = context.parallelize(list);
		KMeansModel model = KMeans.train(data.rdd(), 4, 20); // 数据，类型K数量，迭代次数
		
		System.out.println(model.toPMML());
		
		int result = model.predict(Vectors.dense(102D, 100D));
		System.out.println(result);
	}
	
}

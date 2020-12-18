package com.acgist.cluster;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.clustering.LDA;
import org.apache.spark.mllib.clustering.LDAModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.junit.Before;
import org.junit.Test;

import scala.Tuple2;

/**
 * LDA
 */
@Deprecated
public class LDATest {

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
	public void lda() {
		list.add(Vectors.dense(1D, 1D, 2D, 1D)); // 词频
		list.add(Vectors.dense(2D, 2D, 2D, 3D));
		
		JavaRDD<Vector> documents = context.parallelize(list);;
		JavaRDD<Tuple2<Long, Vector>> runs = documents.zipWithIndex().map(item -> {
			return item.swap();
		}).cache();
		
		LDA lda = new LDA();
		LDAModel model = lda.setK(3)
			.setDocConcentration(5)
			.setTopicConcentration(5)
			.setMaxIterations(20)
			.setSeed(0)
			.setCheckpointInterval(10)
			.setOptimizer("em")
			.run(JavaPairRDD.fromJavaRDD(runs));
		
		for (int index = 0; index < model.describeTopics().length; index++) {
			System.out.println("--------------------------------");
			model.describeTopics(index);
		}
	}
	
}

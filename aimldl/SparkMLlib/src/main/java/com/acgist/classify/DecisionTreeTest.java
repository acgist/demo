package com.acgist.classify;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.DecisionTree;
import org.apache.spark.mllib.tree.configuration.Algo;
import org.apache.spark.mllib.tree.configuration.Strategy;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;
import org.junit.Before;
import org.junit.Test;

/**
 * 决策树
 */
public class DecisionTreeTest {

	private SparkConf conf;
	private JavaSparkContext context;
	private List<LabeledPoint> list;

	@Before
	public void init() {
		conf = new SparkConf().setAppName("test").setMaster("local");
		context = new JavaSparkContext(conf);
		list = new ArrayList<>();
	}
	
	@Test
	public void tree() {
//		RandomForest // 随机森林
//		DecisionTree.train
		
		list.add(new LabeledPoint(0D, Vectors.dense(1D, 0D, 0D, 0D)));
		list.add(new LabeledPoint(0D, Vectors.dense(0D, 1D, 0D, 1D)));
		list.add(new LabeledPoint(1D, Vectors.dense(1D, 1D, 0D, 1D)));
		list.add(new LabeledPoint(1D, Vectors.dense(0D, 1D, 1D, 1D)));
		list.add(new LabeledPoint(0D, Vectors.dense(1D, 0D, 0D, 1D)));
		list.add(new LabeledPoint(0D, Vectors.dense(0D, 0D, 0D, 0D)));
		list.add(new LabeledPoint(1D, Vectors.dense(1D, 1D, 1D, 0D)));
		list.add(new LabeledPoint(1D, Vectors.dense(0D, 1D, 1D, 1D)));
		list.add(new LabeledPoint(1D, Vectors.dense(1D, 1D, 1D, 1D)));
		list.add(new LabeledPoint(1D, Vectors.dense(0D, 0D, 1D, 1D)));
		list.add(new LabeledPoint(0D, Vectors.dense(1D, 1D, 0D, 0D)));
		list.add(new LabeledPoint(0D, Vectors.dense(1D, 1D, 0D, 0D)));
		
//		NaiveBayes trainer = new NaiveBayes();
		
		JavaRDD<LabeledPoint> data = context.parallelize(list);
		Strategy strategy = Strategy.defaultStrategy(Algo.Classification()); // 分类
		DecisionTreeModel model = DecisionTree.train(data.rdd(), strategy);
		double result = model.predict(Vectors.dense(1D, 0D, 1D, 1D));
		System.out.println(result);
	}
	
}

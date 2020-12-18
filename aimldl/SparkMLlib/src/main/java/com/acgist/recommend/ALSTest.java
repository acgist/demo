package com.acgist.recommend;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.recommendation.ALS;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;
import org.apache.spark.rdd.RDD;
import org.junit.Before;
import org.junit.Test;

import scala.Tuple2;

/**
 * ALS交替最小二乘算法
 */
public class ALSTest {

	private SparkConf conf;
	private JavaSparkContext context;
	private List<Rating> list;

	@Before
	public void init() {
		conf = new SparkConf().setAppName("test").setMaster("local");
		context = new JavaSparkContext(conf);
		list = new ArrayList<>();
	}
	
	@Test
	public void als() {
		list.add(Rating.apply(1, 1, 5.0)); // user、product、评分
		list.add(Rating.apply(1, 2, 5.0));
		list.add(Rating.apply(1, 3, 2.0));
		list.add(Rating.apply(1, 4, 2.0));
		list.add(Rating.apply(1, 5, 2.0));
		list.add(Rating.apply(2, 1, 2.0));
		list.add(Rating.apply(2, 2, 2.0));
		list.add(Rating.apply(2, 3, 5.0));
		list.add(Rating.apply(2, 4, 5.0));
		list.add(Rating.apply(2, 5, 5.0));

		JavaRDD<Rating> data = context.parallelize(list);
		
		MatrixFactorizationModel model = ALS.train(data.rdd(), 10, 10); // 特征数量、迭代次数
//		double result = model.predict(1, 3);
//		System.out.println(result);
//		model.recommendProducts(user, num)
//		model.recommendProductsForUsers(num)
//		model.recommendUsers(product, num)
		RDD<Tuple2<Object, Rating[]>> result = model.recommendUsersForProducts(1); // num：返回预测用户数量
		result.toJavaRDD().foreach(value -> {
			System.out.println(value._1);
			for (Rating rating : value._2) {
				System.out.println(rating.user() + " " + rating.product() + " " + rating.rating());
			}
		});
	}
	
}

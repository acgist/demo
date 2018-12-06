package com.acgist.spark.als;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.recommendation.ALS;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;

public class MovieTest {

	public static void main(String[] args) {
//		SparkConf conf = new SparkConf().setMaster("spark://master:7077").setAppName("MovieTest");
		SparkConf conf = new SparkConf().setMaster("local").setAppName("MovieTest");
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaRDD<String> users = sc.textFile("hdfs://master:9000/movie/u.data");
//		JavaRDD<String> users = sc.textFile("src/main/resources/u.data");
		JavaRDD<Rating> ratings = users.map(user -> user.split("\t")).map(values -> {
			return Rating.apply(Integer.valueOf(values[0]), Integer.valueOf(values[1]), Integer.valueOf(values[2]));
		});
		MatrixFactorizationModel model = ALS.train(ratings.rdd(), 10, 10);
		Rating[] result = model.recommendProducts(13, 10);
		for (Rating rating : result) {
			System.out.println(rating.user() + "-" + rating.product() + "-" + rating.rating());
		}
		sc.close();
	}

}

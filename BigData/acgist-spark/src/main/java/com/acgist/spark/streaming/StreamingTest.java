package com.acgist.spark.streaming;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;

import scala.Tuple2;

public class StreamingTest {

	public static void main(String[] args) throws InterruptedException {
		SparkConf conf = new SparkConf().setMaster("spark://master:7077").setAppName("StreamingTest");
		conf.set("spark.driver.host", "192.168.1.100");
		conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
		conf.setJars(new String[] {
			"hdfs://master:9000/home/jars/kafka-clients-2.0.0.jar",
			"hdfs://master:9000/home/jars/spark-streaming-kafka-0-10_2.11-2.4.0.jar"
		});
		JavaStreamingContext sc = new JavaStreamingContext(conf, Durations.seconds(5));
		Map<String, Object> kafkaParams = new HashMap<>();
		kafkaParams.put("bootstrap.servers", "master:9092");
		kafkaParams.put("key.deserializer", StringDeserializer.class);
		kafkaParams.put("value.deserializer", StringDeserializer.class);
		kafkaParams.put("group.id", "test_group");
		kafkaParams.put("auto.offset.reset", "latest");
		kafkaParams.put("enable.auto.commit", false);
		Collection<String> topics = Arrays.asList("test");
		JavaInputDStream<ConsumerRecord<String, String>> stream = KafkaUtils.createDirectStream(
			sc,
			LocationStrategies.PreferConsistent(),
			ConsumerStrategies.<String, String>Subscribe(topics, kafkaParams)
		);
		stream.mapToPair(record -> {
			System.out.println(record.key() + "----" + record.value());
			return new Tuple2<>(record.key(), record.value());
		});
		stream.print();
		sc.start();
		sc.awaitTermination();
	}

}

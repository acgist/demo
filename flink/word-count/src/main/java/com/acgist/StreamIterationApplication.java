package com.acgist;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.IterativeStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class StreamIterationApplication {

	public static void main(String[] args) throws Exception {
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		// 缓存文件
//		env.registerCachedFile(null, null);
		IterativeStream<Integer> loop = env.fromElements(1).iterate();
		// 增量迭代
//			.iterateDelta(null, 0, null)
		SingleOutputStreamOperator<Integer> body = loop.map(value -> value);
		DataStream<Integer> feedback = body.filter(value -> value == null);
		loop.closeWith(feedback);
	}
	
}

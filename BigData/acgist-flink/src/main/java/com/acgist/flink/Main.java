package com.acgist.flink;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * ncat -l 8888：打开服务
 * ncat localhost 8888：连接服务
 * linux：yum install nmap-ncat
 * windows：https://nmap.org/ncat/
 * eclipse：org.eclipse.jdt.core.prefs：org.eclipse.jdt.core.compiler.codegen.lambda.genericSignature=generate
 */
public class Main {

	public static void main(String[] args) throws Exception {
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		DataStream<String> stream = env.socketTextStream("localhost", 8888, "\n");
		DataStream<Tuple2<String, Integer>> wordCounts = stream.flatMap((value, collector) -> {
			String[] values = value.split("\\s");
			for (String item : values) {
				collector.collect(Tuple2.of(item, 1));
			}
		});
		DataStream<Tuple2<String, Integer>> windowCounts = wordCounts.keyBy(0).timeWindow(Time.seconds(5)).sum(1);
		windowCounts.print().setParallelism(1);
		env.execute("Socket Window WordCount");
	}

}

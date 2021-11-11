package com.acgist;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

/**
 * flink单词个数统计
 */
public class SocketWordCount {

	public static void main(String[] args) throws Exception {
//		final ParameterTool params = ParameterTool.fromArgs(args);
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		final DataStream<String> text = env.socketTextStream("localhost", 9000, "\n");
		final DataStream<WordCount> windowCounts = text.flatMap(new FlatMapFunction<String, WordCount>() {
			
			private static final long serialVersionUID = 1L;

			public void flatMap(String value, Collector<WordCount> out) throws Exception {
				for (String word : value.split("//s")) {
					out.collect(new WordCount(word, 1));
				}
			}
		})
			.keyBy("word").timeWindow(Time.seconds(5), Time.seconds(1)).reduce(new ReduceFunction<WordCount>() {
			
			private static final long serialVersionUID = 1L;

			public WordCount reduce(WordCount source, WordCount target) throws Exception {
				return new WordCount(source.getWord(), source.getCount() + target.getCount());
			}
		});
		windowCounts.print().setParallelism(1);
		env.execute("Socket Window WordCount");
	}

}

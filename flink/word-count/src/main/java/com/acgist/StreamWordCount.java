package com.acgist;

import java.util.stream.Stream;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

/**
 * flink单词个数统计
 */
public class StreamWordCount {

	public static void main(String[] args) throws Exception {
//		final ParameterTool params = ParameterTool.fromArgs(args);
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//		env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
//		env.getConfig().setAutoWatermarkInterval(100);
//		env.getConfig().enableForceAvro();
//		env.getConfig().enableForceKryo();
//		env.getConfig().addDefaultKryoSerializer(null, null);
		final DataStream<String> data = env.socketTextStream("localhost", 9000, "\n");
//		泛型注解：@TypeInfo
		data.flatMap((String value, Collector<WordCount> out) -> {
			Stream.of(value.split("\\W+")).forEach(word -> out.collect(new WordCount(word, 1)));
		})
		.returns(WordCount.class)
		// 窗口没有关系
		// 有界无序
//		.assignTimestampsAndWatermarks(WatermarkStrategy.<WordCount>forBoundedOutOfOrderness(Duration.ofSeconds(2)))
		// 不推荐使用
//		.keyBy(0)
//		.keyBy("word")
		.keyBy(word -> word.getWord())
		// 窗口：WindowAssigner
		// 基于时间：滚动窗口（Tumbling）、滑动窗口（Sliding）、会话窗口（Session）
		// 基于数量：
		// 不推荐使用
//		.timeWindow(Time.seconds(5), Time.seconds(1))
		// 注意时间区别：处理时间、事件时间
//		.countWindow(5)
		// 时间类型
		.window(TumblingEventTimeWindows.of(Time.seconds(5)))
//		.window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
		// 设置开始位置
//		.window(TumblingEventTimeWindows.of(Time.seconds(5), Time.seconds(2)))
		// 窗口函数
		.reduce((source, target) -> new WordCount(source.getWord(), source.getCount() + target.getCount()))
		.print()
//		.writeAsText("E://tmp/word-count.txt")
		.setParallelism(1);
		// 流计算需要显示调用
		env.execute("StreamWordCount");
	}

}

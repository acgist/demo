package com.acgist;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple1;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.GlobalWindows;

public class StreamApplication {

	public static void main(String[] args) throws Exception {
//		final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
//		final DataSource<Tuple1<Integer>> data = env.fromElements(Tuple1.of(1), Tuple1.of(2), Tuple1.of(1));
//		data.groupBy(0).sum(0).print();
//		data.groupBy(0).sum(0).print();
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		final DataStreamSource<Tuple1<Integer>> data = env.fromElements(Tuple1.of(1), Tuple1.of(2), Tuple1.of(1));
		DataStream<Tuple1<Integer>> stream = data
			.join(data)
			.where(v -> v.f0).equalTo(v -> v.f0)
			.window(GlobalWindows.create())
//			.window(SlidingProcessingTimeWindows.of(Time.seconds(5), Time.seconds(1)))
			.apply((source, target, out) -> out.collect(Tuple1.of(source.f0 + target.f0)), Types.TUPLE(Types.INT));
		// 不会打印：
		stream.print();
		env.execute();
		// 异步IO
//		ExecutionContextExecutor executor = ExecutionContext.fromExecutor(Executors.directExecutor());
		// 顺序一致
//		AsyncDataStream.orderedWait(env.addSource(new SourceFunction<String>() {
//			
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void run(SourceContext<String> ctx) throws Exception {
//			}
//
//			@Override
//			public void cancel() {
//			}
//			
//		}), new AsyncFunction<String, Integer>() {
//
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void asyncInvoke(String input, ResultFuture<Integer> resultFuture) throws Exception {
//				resultFuture.complete(null);
//			}
//			
//		}, 10L, TimeUnit.SECONDS);
		// 顺序乱序
//		AsyncDataStream.unorderedWait(null, null, 0, null)
		// 任务结束资源释放
	}
	
}

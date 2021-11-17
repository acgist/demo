package com.acgist;

import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.util.Collector;

public class SetJoinApplication {

	public static void main(String[] args) throws Exception {
		final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
		final DataSource<Tuple2<Integer, String>> source = env.fromElements(Tuple2.of(1, "a"), Tuple2.of(2, "b"));
		final DataSource<Tuple2<Integer, String>> target = env.fromElements(Tuple2.of(1, "a"), Tuple2.of(3, "c"));
//		source.joinWithTiny(target)
//		source.joinWithHuge(target)
//		source.join(target, JoinHint.BROADCAST_HASH_FIRST)
//		((1,a),(1,a))
		source.join(target).where(0).equalTo(0).print();
//		(1,a,1,a)
//		(2,b,0,)
		source.leftOuterJoin(target).where(0).equalTo(0).with((a, b) -> Tuple4.of(a.f0, a.f1, b == null ? 0 : b.f0, b == null ? "" : b.f1))
			.returns(Types.TUPLE(Types.INT, Types.STRING, Types.INT, Types.STRING)).print();
//		(0,,3,c)
//		(1,a,1,a)
		source.rightOuterJoin(target).where(0).equalTo(0).with((a, b) -> Tuple4.of(a == null ? 0 : a.f0, a == null ? "" : a.f1, b.f0, b.f1))
			.returns(Types.TUPLE(Types.INT, Types.STRING, Types.INT, Types.STRING)).print();
//		(0,,3,c)
//		(1,a,1,a)
//		(2,b,0,)
		source.fullOuterJoin(target).where(0).equalTo(0).with((a, b) -> Tuple4.of(a == null ? 0 : a.f0, a == null ? "" : a.f1, b == null ? 0 : b.f0, b == null ? "" : b.f1))
			.returns(Types.TUPLE(Types.INT, Types.STRING, Types.INT, Types.STRING)).print();
		// ...
		source.coGroup(target).where(0).equalTo(0).with((Iterable<Tuple2<Integer, String>> a, Iterable<Tuple2<Integer, String>> b, Collector<Tuple2<Integer, String>> out) -> {
			// 相同Key
			a.forEach(out::collect);
			b.forEach(out::collect);
		})
		.returns(Types.TUPLE(Types.INT, Types.STRING))
		.print();
//		((1,a),(1,a))
//		((1,a),(3,c))
//		((2,b),(1,a))
//		((2,b),(3,c))
		source.cross(target).print();
//		(1,a)
//		(1,a)
//		(2,b)
//		(3,c)
		source.union(target).print();
		source.union(target)
//			.sortPartition(0, Order.DESCENDING)
//			.first(2)
			.groupBy(0)
			.sortGroup(0, Order.DESCENDING)
			// 每组一条
			.first(1)
			.print();
	}
	
}

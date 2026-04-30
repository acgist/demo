package com.acgist;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple3;

public class ForwardedApplication {

	public static void main(String[] args) throws Exception {
		ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
		DataSource<Tuple3<Integer, String, String>> work = env.fromElements(Tuple3.of(1, "a", "d"), Tuple3.of(2, "b", "e"));
		work.print();
		work
			.map(value -> Tuple3.of(value.f0, value.f1, value.f2))
			.returns(Types.TUPLE(Types.INT, Types.STRING, Types.STRING))
			.withForwardedFields("f0;f1")
			.print();
		work
			.map(value -> Tuple3.of(value.f0 + 3, value.f1 + "-", value.f2 + "-"))
			.returns(Types.TUPLE(Types.INT, Types.STRING, Types.STRING))
			.withForwardedFields("f0")
			.print();
		work
			.map(value -> Tuple3.of(value.f0 + 3, value.f1 + "-", value.f2 + "-"))
			.returns(Types.TUPLE(Types.INT, Types.STRING, Types.STRING))
			.withForwardedFields("f1->f2")
			.print();
	}
	
}

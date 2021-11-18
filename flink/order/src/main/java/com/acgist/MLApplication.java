package com.acgist;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.ml.classification.SVM;

public class MLApplication {

	public static void main(String[] args) {
		ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
		SVM svm = new SVM();
		svm.setBlocks(env.getParallelism()).setIterations(100).setRegularization(0.001).setStepsize(0.1).setSeed(42);
		svm.fit(null, null, null);
		svm.predict(null, null, null);
		svm.evaluate(null, null, null);
	}

}

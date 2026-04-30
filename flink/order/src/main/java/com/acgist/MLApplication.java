//package com.acgist;
//
//import org.apache.commons.math3.stat.regression.MultipleLinearRegression;
//import org.apache.flink.api.java.ExecutionEnvironment;
//
//public class MLApplication {
//
//	public static void main(String[] args) {
//		ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
//		// 支持向量机
//		SVM svm = new SVM();
//		svm.setBlocks(env.getParallelism()).setIterations(100).setRegularization(0.001).setStepsize(0.1).setSeed(42);
//		// 训练
//		svm.fit(null, null, null);
//		// 预测
//		svm.predict(null, null, null);
//		svm.evaluate(null, null, null);
////		MLUtils.writeLibSVM(null, null);
//		// 多元线性回归
//		MultipleLinearRegression mlr = new MultipleLinearRegression();
//	}
//
//}

package com.acgist.dl4j;

import java.io.File;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.RBM;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;

public class DL4JExample {

	@SuppressWarnings({ "deprecation" })
	public static void main(String[] args) throws Exception {
//		// 初始化用户界面后端
//		UIServer uiServer = UIServer.getInstance();
//		// 设置网络信息（随时间变化的梯度、分值等）的存储位置。这里将其存储于内存。
//		StatsStorage statsStorage = new InMemoryStatsStorage(); // 或者： new FileStatsStorage(File)，用于后续的保存和载入
//		// 将StatsStorage实例连接至用户界面，让StatsStorage的内容能够被可视化
//		uiServer.attach(statsStorage);
//		// 然后添加StatsListener来在网络定型时收集这些信息
//		net.setListeners(new StatsListener(statsStorage));
		// 神经网络配置
		MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
			.seed(4) // 
			.iterations(1) // Epoch数量和迭代次数
			.learningRate(1E-6F) // 学习速率
			.optimizationAlgo(OptimizationAlgorithm.CONJUGATE_GRADIENT) // 优化代价函数
			.l1(1E-1).regularization(true).l2(2E-4) // 正则化：防止过拟合
			.useDropConnect(true)
			.list() // 多层网络和单层网络
			.layer(0, new RBM.Builder(RBM.HiddenUnit.RECTIFIED, RBM.VisibleUnit.GAUSSIAN)
				.nIn(4)
				.nOut(3)
				.weightInit(WeightInit.XAVIER) // 权重初始化
				.k(4)
				.activation(Activation.RELU) // 激活函数
//				.lossFunction(LossFunction.RMSE_XENT) // 损失函数
				.lossFunction(LossFunction.NEGATIVELOGLIKELIHOOD) // 损失函数
				.updater(Updater.ADAGRAD) // 更新器和优化算法：动量
				.dropOut(0.5D)
				.build()
			)
			.layer(1, new RBM.Builder(RBM.HiddenUnit.RECTIFIED, RBM.VisibleUnit.GAUSSIAN)
				.nIn(3)
				.nOut(2)
				.weightInit(WeightInit.XAVIER)
				.k(4)
				.activation(Activation.RELU)
				.lossFunction(LossFunction.NEGATIVELOGLIKELIHOOD)
				.updater(Updater.ADAGRAD)
				.dropOut(0.5D)
				.build()
			)
			.layer(2, new OutputLayer.Builder(LossFunction.MCXENT)
				.nIn(2)
				.nOut(4)
				.activation(Activation.SOFTMAX)
				.build()
			)
			.build();
		// 神经网络
		MultiLayerNetwork model = new MultiLayerNetwork(conf);
		model.init();
//		// 数据读取
		RecordReader reader = new CSVRecordReader(0, ",");
		reader.initialize(new FileSplit(new File("data.txt")));
		DataSetIterator dataSetIterator = new RecordReaderDataSetIterator(reader, 4);
		DataSet dataSet = dataSetIterator.next();
		SplitTestAndTrain testAndTrain = dataSet.splitTestAndTrain(0.6);
		DataSet test = testAndTrain.getTest(); // 测试数据
		DataSet train = testAndTrain.getTrain(); // 训练数据
		// 训练
		model.fit(train);
		// 测试
		Evaluation eval = new Evaluation(3);
		INDArray output = model.output(test.getFeatureMatrix());
		eval.eval(test.getLabels(), output);
		System.out.println(eval.stats());
	}
	
}

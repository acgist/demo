package com.acgist.dl4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.SubsamplingLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;

public class CNNExample {

	public static void main(String[] args) throws Exception {
//		int numRows = 28;
//		int numCloumns = 28;
		int nChannels = 1; // MNIST图片都是灰度数据，通道的数量设置为1
		int outputNum = 10;
		int numSamples = 2000;
		int batchSize = 500;
		int iterations = 10;
		int splitTrainNum = (int) (batchSize * 0.8);
		int seed = 123;
		int listenerFreq = iterations / 5;
		MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
			.seed(seed)
			.iterations(1)
			.learningRate(1E-6F)
			.optimizationAlgo(OptimizationAlgorithm.CONJUGATE_GRADIENT)
			.l1(1E-1).regularization(true).l2(2E-4)
			.useDropConnect(true)
			.list()
			.layer(0, new ConvolutionLayer.Builder(10, 10) // 10：核的大小
				.stride(2, 2) // 核的步长，值越大花费的时间越少，减小了卷积所需要计算的数量，但是同时降低模型
				.nIn(nChannels)
				.nOut(6) // 核的个数
				.weightInit(WeightInit.XAVIER)
				.activation(Activation.RELU)
				.build()
			)
			// 亚采样层
			.layer(1, new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX, new int[] {2, 2}) // {2, 2}池化窗口大小
				.build()
			)
			// 输出层
			.layer(2, new OutputLayer.Builder(LossFunction.NEGATIVELOGLIKELIHOOD)
				.nOut(outputNum)
				.weightInit(WeightInit.XAVIER)
				.activation(Activation.SOFTMAX)
				.build()
			)
			.backprop(true)
			.pretrain(false)
			.build();
		// 训练
		List<INDArray> testInput = new ArrayList<>();
		List<INDArray> testLabels = new ArrayList<>();
		MultiLayerNetwork model = new MultiLayerNetwork(conf);
		model.setListeners(new ScoreIterationListener(listenerFreq));
		DataSetIterator dataSetIterator = new MnistDataSetIterator(batchSize, numSamples, true);
		while(dataSetIterator.hasNext()) {
			DataSet dataSet = dataSetIterator.next();
			SplitTestAndTrain testAndTrain = dataSet.splitTestAndTrain(splitTrainNum, new Random(seed));
			DataSet train = testAndTrain.getTrain();
			testInput.add(testAndTrain.getTest().getFeatureMatrix());
			testLabels.add(testAndTrain.getTest().getLabels());
			model.fit(train);
		}
		Evaluation eval = new Evaluation(3);
		for (int index = 0; index < testInput.size(); index++) {
			INDArray output = model.output(testInput.get(index));
			eval.eval(testLabels.get(index), output);
		}
		System.out.println(eval.stats());
	}
	
}

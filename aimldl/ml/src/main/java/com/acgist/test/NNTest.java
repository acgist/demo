package com.acgist.test;

import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.SubsamplingLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;

public class NNTest {

	public static void main(String[] args) {
		MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
			.seed(10)
			.iterations(1)
			.rmsDecay(0.95) // 加速衰减系数，防止梯度变化过大，训练过早结束的参数更新方法
			.learningRate(1E-6F)
			.l1(1E-1).regularization(true).l2(2E-4) // 正则化：防止过拟合
			.optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT) // 优化代价函数
			.weightInit(WeightInit.XAVIER) // 权值初始化
            .updater(Updater.NESTEROVS).momentum(0.9) // 优化方法
			.list() // 多层神经网络
			.layer(0, new ConvolutionLayer.Builder(10, 10)
				.nIn(1)
				.nOut(20) // 核的个数
				.stride(2, 2) // 核的步长，值越大花费的时间越少，减小了卷积所需要计算的数量，但是同时降低模型
				.weightInit(WeightInit.XAVIER)
				.activation(Activation.IDENTITY)
				.build()
			)
			.layer(1, new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
				.kernelSize(2, 2)
				.stride(2, 2)
				.build()
			)
			.layer(2, new ConvolutionLayer.Builder(10, 10)
				.nIn(1)
				.nOut(50)
				.stride(2, 2)
				.activation(Activation.IDENTITY)
				.build()
			)
			.layer(3, new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
				.kernelSize(2, 2)
				.stride(2, 2)
				.build()
			)
			.layer(4, new OutputLayer.Builder(LossFunction.NEGATIVELOGLIKELIHOOD)
				.nIn(1)
				.nOut(10)
				.weightInit(WeightInit.XAVIER)
				.activation(Activation.SOFTMAX)
				.build()
			)
			.build();
		MultiLayerNetwork model = new MultiLayerNetwork(conf);
		model.init();
	}
	
}

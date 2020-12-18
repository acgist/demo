package com.acgist.ocr;

import java.io.File;

import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.PoolingType;
import org.deeplearning4j.nn.conf.layers.SubsamplingLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 生成模型
 */
public class ModelBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(ModelBuilder.class);

	public static void main(String[] args) throws Exception {
		int nChannels = 1;
		int outputNum = 10;
		int batchSize = 64;
		int nEpochs = 1;
		int seed = 123456;
		LOGGER.info("加载数据");
		DataSetIterator mnistTrain = new MnistDataSetIterator(batchSize, true, seed);
		DataSetIterator mnistTest = new MnistDataSetIterator(batchSize, false, seed);
		LOGGER.info("构建模型");
		MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
			.seed(seed)
			.l2(0.0006)
//			.regularization(true)
			.momentum(0.9)
			.learningRate(0.02)
			.updater(Updater.NESTEROVS)
			.weightInit(WeightInit.XAVIER)
			.list()
			.layer(0, new ConvolutionLayer.Builder(5, 5)
				.nIn(nChannels)
				.stride(1, 1)
				.nOut(32)
				.activation(Activation.IDENTITY)
				.build()
			)
			.layer(1, new SubsamplingLayer.Builder(PoolingType.MAX)
				.kernelSize(2, 2)
				.stride(2, 2)
				.build()
			)
			.layer(2, new ConvolutionLayer.Builder(5, 5)
				.stride(1, 1)
				.nOut(64)
				.activation(Activation.IDENTITY)
				.build()
			)
			.layer(3, new SubsamplingLayer.Builder(PoolingType.MAX)
				.kernelSize(2, 2)
				.stride(2, 2)
				.build()
			)
			.layer(4, new DenseLayer.Builder()
				.activation(Activation.RELU)
				.nOut(400)
				.build()
			)
			.layer(5, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
				.nOut(outputNum)
				.activation(Activation.SOFTMAX)
				.build()
			)
			.setInputType(InputType.convolutionalFlat(28, 28, 1))
			.backprop(true)
			.pretrain(false)
			.build();
		MultiLayerNetwork model = new MultiLayerNetwork(conf);
		model.init();
		LOGGER.info("训练模型");
		model.setListeners(new ScoreIterationListener(10));
		for (int i = 0; i < nEpochs; i++) {
			model.fit(mnistTrain);
			LOGGER.info("评估模型");
			ModelSerializer.writeModel(model, new File("./number.model"), true);
			Evaluation eval = model.evaluate(mnistTest);
			LOGGER.info(eval.stats());
			mnistTest.reset();
		}
		LOGGER.info("完成");
	}
}

package com.acgist.dl4j.gender;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.lossfunctions.LossFunctions;

public class PredictGenderTrain {

	public String filePath;
	public String testFilePath;

	public static void main(String args[]) throws URISyntaxException {
		PredictGenderTrain train = new PredictGenderTrain();
		train.filePath = PredictGenderTrain.class.getResource("/gender/train").toURI().getPath();
		train.testFilePath = PredictGenderTrain.class.getResource("/gender/test").toURI().getPath();
		System.out.println(train.filePath);
		train.train();
	}

	public void train() {
		int seed = 123456;
		double learningRate = 0.01;
		int batchSize = 100;
		int nEpochs = 100;
		int numInputs = 0;
		int numOutputs = 0;
		int numHiddenNodes = 0;
		try (GenderRecordReader reader = new GenderRecordReader(new ArrayList<String>() {
			private static final long serialVersionUID = 1L;
			{
				add("M");
				add("F");
			}
		})) {
			reader.initialize(new FileSplit(new File(this.filePath))); // 初始化读取器
			numInputs = reader.maxLengthName * 5; // 00000 // 每个字符用5个二进制表示，输入大小就是最长名字的5倍
			numOutputs = 2; // 输出大小为2
			numHiddenNodes = 2 * numInputs + numOutputs; // 隐含层大小
			GenderRecordReader testReader = new GenderRecordReader(new ArrayList<String>() {
				private static final long serialVersionUID = 1L;
				{
					add("M");
					add("F");
				}
			}); // 又搞了一个读取器
			testReader.initialize(new FileSplit(new File(this.testFilePath)));
			DataSetIterator trainIterator = new RecordReaderDataSetIterator(reader, batchSize, numInputs, 2); // 训练迭代器
			DataSetIterator testIterator = new RecordReaderDataSetIterator(testReader, batchSize, numInputs, 2); // 测试迭代器
			MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder() // 网络还是一样，假装自己是老司机
				.seed(seed).biasInit(1).regularization(true).l2(1e-4).iterations(1)
				.optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT).learningRate(learningRate)
				.updater(Updater.NESTEROVS).momentum(0.9) // 采用梯度修正的参数修正方法
				.list()
				.layer(0, new DenseLayer.Builder()
						.nIn(numInputs)
						.nOut(numHiddenNodes)
						.weightInit(WeightInit.XAVIER)
						.activation(Activation.RELU)
						.build()
				)
				.layer(1,
						new DenseLayer.Builder()
						.nIn(numHiddenNodes)
						.nOut(numHiddenNodes)
						.weightInit(WeightInit.XAVIER)
						.activation(Activation.RELU)
						.build())
				.layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
						.nIn(numHiddenNodes)
						.nOut(numOutputs)
						.weightInit(WeightInit.XAVIER)
						.activation(Activation.SOFTMAX)
						.build()
				)
				.pretrain(false).backprop(true).build();
			MultiLayerNetwork model = new MultiLayerNetwork(conf);
			model.init();
			// 初始化用户界面后端
//			UIServer uiServer = UIServer.getInstance();
//			StatsStorage statsStorage = new InMemoryStatsStorage(); // 或者： new FileStatsStorage(File)，用于后续的保存和载入
//			uiServer.attach(statsStorage);
//			model.setListeners(new StatsListener(statsStorage));
			model.setListeners(new ScoreIterationListener(10));
			for (int n = 0; n < nEpochs; n++) { // 按步走
				while (trainIterator.hasNext()) { // 按批走
					model.fit(trainIterator.next()); // 训练模型
				}
				trainIterator.reset(); // 每步走完数据重新来
			}
			ModelSerializer.writeModel(model, this.filePath + "PredictGender.net", true); // 通过模型序列化方法把模型写到指定路径
			System.out.println("评估模型：");
			Evaluation eval = new Evaluation(numOutputs); // 评价模型，这个也是老套路了,最终打印评价矩阵
			while (testIterator.hasNext()) {
				DataSet dataSet = testIterator.next();
				INDArray features = dataSet.getFeatures();
				INDArray lables = dataSet.getLabels();
				INDArray predicted = model.output(features, false);
				eval.eval(lables, predicted);
			}
			System.out.println(eval.stats());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

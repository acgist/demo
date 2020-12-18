package com.acgist.dl;

import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ImageDrawer extends Application {

	private DataSet ds;
	private INDArray xyOut;
	private Image originalImage;
	private MultiLayerNetwork nn;
	private WritableImage composition;

	private void onCalc() {
		nn.fit(ds); // 训练神经网络
		drawImage(); // 再次输出测试
		Platform.runLater(this::onCalc);
	}

	@Override
	public void init() {
		originalImage = new Image("csol.jpg");
		final int w = (int) originalImage.getWidth();
		final int h = (int) originalImage.getHeight();
		composition = new WritableImage(w, h);
		ds = generateDataSet(originalImage);
		nn = createNN();
		int numPoints = h * w;
		xyOut = Nd4j.zeros(numPoints, 2);
		for (int i = 0; i < w; i++) {
			double xp = (double) i / (double) (w - 1);
			for (int j = 0; j < h; j++) {
				int index = i + w * j;
				double yp = (double) j / (double) (h - 1);
				xyOut.put(index, 0, xp);
				xyOut.put(index, 1, yp);
			}
		}
		drawImage();
	}

	@Override
	public void start(Stage primaryStage) {
		final int w = (int) originalImage.getWidth();
		final int h = (int) originalImage.getHeight();
		final int zoom = 1;
		ImageView iv1 = new ImageView();
		iv1.setImage(originalImage);
		iv1.setFitHeight(zoom * h);
		iv1.setFitWidth(zoom * w);
		ImageView iv2 = new ImageView();
		iv2.setImage(composition);
		iv2.setFitHeight(zoom * h);
		iv2.setFitWidth(zoom * w);
		HBox root = new HBox();
		Scene scene = new Scene(root);
		root.getChildren().addAll(iv1, iv2);
		primaryStage.setTitle("画图");
		primaryStage.setScene(scene);
		primaryStage.show();
		Platform.setImplicitExit(true);
		Platform.runLater(this::onCalc);
	}

	private static MultiLayerNetwork createNN() {
		int seed = 2345;
		int iterations = 25;
		double learningRate = 0.1;
		int numInputs = 2;
		int numHiddenNodes = 25;
		int numOutputs = 3;
		MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder().seed(seed).iterations(iterations)
				.optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT).learningRate(learningRate)
				.weightInit(WeightInit.XAVIER).updater(Updater.NESTEROVS).momentum(0.9).list()
				.layer(0, new DenseLayer.Builder().nIn(numInputs).nOut(numHiddenNodes).activation(Activation.IDENTITY).build())
				.layer(1, new DenseLayer.Builder().nIn(numHiddenNodes).nOut(numHiddenNodes).activation(Activation.RELU).build())
				.layer(2, new DenseLayer.Builder().nIn(numHiddenNodes).nOut(numHiddenNodes).activation(Activation.RELU).build())
				.layer(3, new DenseLayer.Builder().nIn(numHiddenNodes).nOut(numHiddenNodes).activation(Activation.RELU).build())
				.layer(4, new OutputLayer.Builder(LossFunctions.LossFunction.L2).activation(Activation.IDENTITY).nIn(numHiddenNodes).nOut(numOutputs).build())
				.pretrain(false).backprop(true).build();
		MultiLayerNetwork net = new MultiLayerNetwork(conf);
		net.init();
		return net;
	}

	private static DataSet generateDataSet(Image img) {
		int w = (int) img.getWidth();
		int h = (int) img.getHeight();
		int numPoints = h * w;
		PixelReader reader = img.getPixelReader();
		INDArray xy = Nd4j.zeros(numPoints, 2);
		INDArray out = Nd4j.zeros(numPoints, 3);
		for (int i = 0; i < w; i++) {
			double xp = (double) i / (double) (w - 1);
			for (int j = 0; j < h; j++) {
				Color c = reader.getColor(i, j);
				int index = i + w * j;
				double yp = (double) j / (double) (h - 1);
				xy.put(index, 0, xp);
				xy.put(index, 1, yp);
				out.put(index, 0, c.getRed());
				out.put(index, 1, c.getGreen());
				out.put(index, 2, c.getBlue());
			}
		}
		return new DataSet(xy, out);
	}

	private void drawImage() {
		int w = (int) composition.getWidth();
		int h = (int) composition.getHeight();
		INDArray out = nn.output(xyOut);
		PixelWriter writer = composition.getPixelWriter();
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				int index = i + w * j;
				double red = capNNOutput(out.getDouble(index, 0));
				double green = capNNOutput(out.getDouble(index, 1));
				double blue = capNNOutput(out.getDouble(index, 2));
				Color c = new Color(red, green, blue, 1.0);
				writer.setColor(i, j, c);
			}
		}
	}

	private static double capNNOutput(double x) {
		double tmp = (x < 0.0) ? 0.0 : x;
		return (tmp > 1.0) ? 1.0 : tmp;
	}

	public static void main(String[] args) {
		launch(args);
	}

}
